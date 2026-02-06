package org.ruoyi.service.strategy.impl;

import cn.hutool.json.JSONObject;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.batch.api.ObjectsBatchDeleter;
import io.weaviate.client.v1.batch.model.BatchDeleteResponse;
import io.weaviate.client.v1.filters.Operator;
import io.weaviate.client.v1.filters.WhereFilter;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.v1.schema.model.Schema;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.config.VectorStoreProperties;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.domain.bo.QueryVectorBo;
import org.ruoyi.domain.bo.StoreEmbeddingBo;
import org.ruoyi.embedding.EmbeddingModelFactory;
import org.ruoyi.service.strategy.AbstractVectorStoreStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Weaviate向量库策略实现
 *
 * @author Yzm
 */
@Slf4j
@Component
public class WeaviateVectorStoreStrategy extends AbstractVectorStoreStrategy {

    private WeaviateClient client;

    public WeaviateVectorStoreStrategy(VectorStoreProperties vectorStoreProperties, EmbeddingModelFactory embeddingModelFactory) {
        super(vectorStoreProperties, embeddingModelFactory);
    }

    @Override
    public String getVectorStoreType() {
        return "weaviate";
    }

    @Override
    public void createSchema(String kid, String embeddingModelName) {
        String protocol = vectorStoreProperties.getWeaviate().getProtocol();
        String host = vectorStoreProperties.getWeaviate().getHost();
        String className = vectorStoreProperties.getWeaviate().getClassname() + kid;
        // 创建 Weaviate 客户端
        client = new WeaviateClient(new Config(protocol, host));
        // 检查类是否存在，如果不存在就创建 schema
        Result<Schema> schemaResult = client.schema().getter().run();
        if (schemaResult.hasErrors()) {
            log.error("获取Weaviate schema失败: kid={}, className={}, error={}", kid, className, schemaResult.getError());
            throw new ServiceException("获取Weaviate schema失败: " + schemaResult.getError());
        }
        
        Schema schema = schemaResult.getResult();
        if (schema == null) {
            log.warn("Weaviate schema为null，可能是服务未启动或配置错误: kid={}, className={}", kid, className);
            throw new ServiceException("Weaviate schema为null，请检查服务是否正常运行");
        }
        
        boolean classExists = false;
        if (schema.getClasses() != null) {
            for (WeaviateClass weaviateClass : schema.getClasses()) {
                if (weaviateClass.getClassName().equals(className)) {
                    classExists = true;
                    break;
                }
            }
        }
        
        if (!classExists) {
            // 类不存在，创建 schema
            WeaviateClass build = WeaviateClass.builder()
                    .className(className)
                    .vectorizer("none")
                    .properties(
                            List.of(Property.builder().name("text").dataType(Collections.singletonList("text")).build(),
                                    Property.builder().name("fid").dataType(Collections.singletonList("text")).build(),
                                    Property.builder().name("kid").dataType(Collections.singletonList("text")).build(),
                                    Property.builder().name("docId").dataType(Collections.singletonList("text")).build())
                    )
                    .build();
            Result<Boolean> createResult = client.schema().classCreator().withClass(build).run();
            if (createResult.hasErrors()) {
                log.error("Schema 创建失败: kid={}, className={}, error={}", kid, className, createResult.getError());
                throw new ServiceException("Schema 创建失败: " + createResult.getError());
            } else {
                log.info("Schema 创建成功: {}", className);
            }
        }
    }

    @Override
    public void storeEmbeddings(StoreEmbeddingBo storeEmbeddingBo) {
        storeEmbeddings(storeEmbeddingBo, null);
    }

    @Override
    public void storeEmbeddings(StoreEmbeddingBo storeEmbeddingBo, org.ruoyi.service.IAttachProcessService attachProcessService) {
        createSchema(storeEmbeddingBo.getKid(), storeEmbeddingBo.getEmbeddingModelName());
        EmbeddingModel embeddingModel = getEmbeddingModel(storeEmbeddingBo.getEmbeddingModelName(), null);
        List<String> chunkList = storeEmbeddingBo.getChunkList();
        List<String> fidList = storeEmbeddingBo.getFids();
        String kid = storeEmbeddingBo.getKid();
        String docId = storeEmbeddingBo.getDocId();
        String processId = storeEmbeddingBo.getProcessId();
        int totalCount = chunkList.size();
        log.info("向量存储条数记录: " + totalCount);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < totalCount; i++) {
            String text = chunkList.get(i);
            String fid = fidList.get(i);
            Embedding embedding = embeddingModel.embed(text).content();
            Map<String, Object> properties = Map.of(
                    "text", text,
                    "fid", fid,
                    "kid", kid,
                    "docId", docId
            );
            Float[] vector = toObjectArray(embedding.vector());
            String className = vectorStoreProperties.getWeaviate().getClassname() + kid;
            client.data().creator()
                    .withClassName(className)
                    .withProperties(properties)
                    .withVector(vector)
                    .run();
            
            //实时更新向量化进度（每5个或最后一个）
            if (attachProcessService != null && processId != null && (i % 5 == 0 || i == totalCount - 1)) {
                try {
                    Map<String, Object> progressData = new HashMap<>();
                    progressData.put("currentIndex", i + 1);
                    progressData.put("totalCount", totalCount);
                    progressData.put("stage", "VECTORIZING");
                    attachProcessService.updateProgress(processId, progressData);
                } catch (Exception e) {
                    log.warn("更新向量化进度失败: processId={}, error={}", processId, e.getMessage());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("向量存储完成消耗时间：" + (endTime - startTime) / 1000 + "秒");
    }


    @Override
    public List<String> getQueryVector(QueryVectorBo queryVectorBo) {
        createSchema(queryVectorBo.getKid(), queryVectorBo.getEmbeddingModelName());
        EmbeddingModel embeddingModel = getEmbeddingModel(queryVectorBo.getEmbeddingModelName(), null);
        Embedding queryEmbedding = embeddingModel.embed(queryVectorBo.getQuery()).content();
        float[] vector = queryEmbedding.vector();
        List<String> vectorStrings = new ArrayList<>();
        for (float v : vector) {
            vectorStrings.add(String.valueOf(v));
        }
        String vectorStr = String.join(",", vectorStrings);
        String className = vectorStoreProperties.getWeaviate().getClassname();

        // 构建 GraphQL 查询
        String graphQLQuery = String.format(
                "{\n" +
                        "  Get {\n" +
                        "    %s(nearVector: {vector: [%s]} limit: %d) {\n" +
                        "      text\n" +
                        "      fid\n" +
                        "      kid\n" +
                        "      docId\n" +
                        "      _additional {\n" +
                        "        distance\n" +
                        "        id\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                className + queryVectorBo.getKid(),
                vectorStr,
                queryVectorBo.getMaxResults()
        );

        Result<GraphQLResponse> result = client.graphQL().raw().withQuery(graphQLQuery).run();
        List<String> resultList = new ArrayList<>();
        if (result != null && !result.hasErrors()) {
            Object data = result.getResult().getData();
            JSONObject entries = new JSONObject(data);
            Map<String, cn.hutool.json.JSONArray> entriesMap = entries.get("Get", Map.class);
            cn.hutool.json.JSONArray objects = entriesMap.get(className + queryVectorBo.getKid());
            if (objects.isEmpty()) {
                return resultList;
            }
            for (Object object : objects) {
                Map<String, String> map = (Map<String, String>) object;
                String content = map.get("text");
                resultList.add(content);
            }
            return resultList;
        } else {
            log.error("GraphQL 查询失败: {}", result.getError());
            return resultList;
        }
    }

    @Override
    @SneakyThrows
    public void removeById(String id, String modelName) {
        String protocol = vectorStoreProperties.getWeaviate().getProtocol();
        String host = vectorStoreProperties.getWeaviate().getHost();
        String className = vectorStoreProperties.getWeaviate().getClassname();
        String finalClassName = className + id;
        WeaviateClient client = new WeaviateClient(new Config(protocol, host));
        
        // 先检查类是否存在
        Result<Schema> schemaResult = client.schema().getter().run();
        if (schemaResult.hasErrors()) {
            log.warn("获取Weaviate schema失败: id={}, error={}", id, schemaResult.getError());
            return;
        }
        
        Schema schema = schemaResult.getResult();
        boolean classExists = false;
        if (schema != null && schema.getClasses() != null) {
            for (WeaviateClass weaviateClass : schema.getClasses()) {
                if (weaviateClass.getClassName().equals(finalClassName)) {
                    classExists = true;
                    break;
                }
            }
        }
        
        // 如果类不存在，视为成功（目标已达成）
        if (!classExists) {
            log.info("向量类不存在，无需删除: id={}, className={}", id, finalClassName);
            return;
        }
        
        // 类存在，执行删除
        Result<Boolean> result = client.schema().classDeleter().withClassName(finalClassName).run();
        if (result.hasErrors()) {
            log.warn("删除向量类失败: id={}, className={}, error={}", id, finalClassName, result.getError());
        } else {
            log.info("成功删除向量类: id={}, className={}", id, finalClassName);
        }
    }

    @Override
    public void removeByDocId(String docId, String kid) {
        String className = vectorStoreProperties.getWeaviate().getClassname() + kid;
        // 构建 Where 条件
        WhereFilter whereFilter = WhereFilter.builder()
                .path("docId")
                .operator(Operator.Equal)
                .valueText(docId)
                .build();
        ObjectsBatchDeleter deleter = client.batch().objectsBatchDeleter();
        Result<BatchDeleteResponse> result = deleter.withClassName(className)
                .withWhere(whereFilter)
                .run();
        if (result != null && !result.hasErrors()) {
            log.info("成功删除 docId={} 的所有向量数据", docId);
        } else {
            log.error("删除失败: {}", result.getError());
        }
    }

    @Override
    @SneakyThrows
    public void removeByFid(String fid, String kid) {
        String protocol = vectorStoreProperties.getWeaviate().getProtocol();
        String host = vectorStoreProperties.getWeaviate().getHost();
        String className = vectorStoreProperties.getWeaviate().getClassname() + kid;
        WeaviateClient client = new WeaviateClient(new Config(protocol, host));
        
        // 先检查类是否存在
        Result<Schema> schemaResult = client.schema().getter().run();
        if (schemaResult.hasErrors()) {
            log.warn("获取Weaviate schema失败: fid={}, kid={}, error={}", fid, kid, schemaResult.getError());
            return;
        }
        
        Schema schema = schemaResult.getResult();
        boolean classExists = false;
        if (schema != null && schema.getClasses() != null) {
            for (WeaviateClass weaviateClass : schema.getClasses()) {
                if (weaviateClass.getClassName().equals(className)) {
                    classExists = true;
                    break;
                }
            }
        }
        
        // 如果类不存在，视为成功（目标已达成）
        if (!classExists) {
            log.info("向量类不存在，无需删除对象: fid={}, kid={}, className={}", fid, kid, className);
            return;
        }
        
        // 类存在，执行删除对象
        WhereFilter whereFilter = WhereFilter.builder()
                .path("fid")
                .operator(Operator.Equal)
                .valueText(fid)
                .build();
        ObjectsBatchDeleter deleter = client.batch().objectsBatchDeleter();
        Result<BatchDeleteResponse> result = deleter.withClassName(className)
                .withWhere(whereFilter)
                .run();
        if (result != null && !result.hasErrors()) {
            log.info("成功删除 fid={} 的所有向量数据", fid);
        } else {
            log.warn("删除向量对象失败: fid={}, kid={}, error={}", fid, kid, result != null ? result.getError() : "result is null");
        }
    }

}