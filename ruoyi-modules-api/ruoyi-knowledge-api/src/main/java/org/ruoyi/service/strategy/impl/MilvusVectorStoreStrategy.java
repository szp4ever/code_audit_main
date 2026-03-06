package org.ruoyi.service.strategy.impl;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Slf4j
@Component
public class MilvusVectorStoreStrategy extends AbstractVectorStoreStrategy {

    public MilvusVectorStoreStrategy(VectorStoreProperties vectorStoreProperties, EmbeddingModelFactory embeddingModelFactory) {
        super(vectorStoreProperties, embeddingModelFactory);
    }

    // 缓存不同集合与 autoFlush 配置的 Milvus 连接
    private final Map<String, EmbeddingStore<TextSegment>> storeCache = new ConcurrentHashMap<>();

    /**
     * 获取 Milvus Store，支持动态维度
     */
    private EmbeddingStore<TextSegment> getMilvusStore(String collectionName, int dimension, boolean autoFlushOnInsert) {
        String key = collectionName + "|" + dimension + "|" + autoFlushOnInsert;
        return storeCache.computeIfAbsent(key, k ->
                MilvusEmbeddingStore.builder()
                        .uri(vectorStoreProperties.getMilvus().getUrl())
                        .collectionName(collectionName)
                        .dimension(dimension)
                        .indexType(IndexType.IVF_FLAT)
                        .metricType(MetricType.L2)
                        .autoFlushOnInsert(autoFlushOnInsert)
                        .idFieldName("id")
                        .textFieldName("text")
                        .metadataFieldName("metadata")
                        .vectorFieldName("vector")
                        .build()
        );
    }
    
    /**
     * 获取 embedding 模型的实际维度
     */
    private int getModelDimension(String modelName) {
        try {
            EmbeddingModel model = getEmbeddingModel(modelName, null);
            // 使用一个测试文本获取向量维度
            Embedding testEmbedding = model.embed("test").content();
            int dimension = testEmbedding.dimension();
            log.info("Detected embedding model dimension: {} for model: {}", dimension, modelName);
            return dimension;
        } catch (Exception e) {
            log.warn("Failed to detect model dimension for: {}, using default 1024", modelName, e);
            return 1024; // 默认使用 1024 (bge-m3 的维度)
        }
    }

    @Override
    public void createSchema(String kid, String modelName) {
        try {
            String collectionName = vectorStoreProperties.getMilvus().getCollectionname() + kid;
            int dimension = getModelDimension(modelName);
            // 使用缓存获取连接以确保只初始化一次
            EmbeddingStore<TextSegment> store = getMilvusStore(collectionName, dimension, true);
            log.info("Milvus集合初始化完成: {}, dimension: {}", collectionName, dimension);
        } catch (Exception e) {
            log.error("Milvus集合初始化失败: kid={}, modelName={}, error={}", kid, modelName, e.getMessage(), e);
            throw new ServiceException("Milvus集合初始化失败: " + e.getMessage());
        }
    }

    @Override
    public void storeEmbeddings(StoreEmbeddingBo storeEmbeddingBo) {
        storeEmbeddings(storeEmbeddingBo, null);
    }

    @Override
    public void storeEmbeddings(StoreEmbeddingBo storeEmbeddingBo, org.ruoyi.service.IAttachProcessService attachProcessService) {
        int dimension = getModelDimension(storeEmbeddingBo.getEmbeddingModelName());
        EmbeddingModel embeddingModel = getEmbeddingModel(storeEmbeddingBo.getEmbeddingModelName(), dimension);

        List<String> chunkList = storeEmbeddingBo.getChunkList();
        List<String> fidList = storeEmbeddingBo.getFids();
        String kid = storeEmbeddingBo.getKid();
        String docId = storeEmbeddingBo.getDocId();
        String processId = storeEmbeddingBo.getProcessId();
        String collectionName = vectorStoreProperties.getMilvus().getCollectionname() + kid;
        int totalCount = chunkList.size();

        log.info("Milvus向量存储条数记录: {}", totalCount);
        long startTime = System.currentTimeMillis();

        // 复用连接，写入场景使用 autoFlush=false 以提升批量插入性能
        EmbeddingStore<TextSegment> embeddingStore = getMilvusStore(collectionName, dimension, false);

        //使用普通for循环以便更新进度
        for (int i = 0; i < totalCount; i++) {
            String text = chunkList.get(i);
            String fid = fidList.get(i);
            Metadata metadata = new Metadata();
            metadata.put("fid", fid);
            metadata.put("kid", kid);
            metadata.put("docId", docId);

            TextSegment textSegment = TextSegment.from(text, metadata);
            Embedding embedding = embeddingModel.embed(text).content();
            embeddingStore.add(embedding, textSegment);
            
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
        log.info("Milvus向量存储完成消耗时间：{}秒", (endTime - startTime) / 1000);
    }

    @Override
    public List<String> getQueryVector(QueryVectorBo queryVectorBo) {
        try {
            int dimension = getModelDimension(queryVectorBo.getEmbeddingModelName());
            EmbeddingModel embeddingModel = getEmbeddingModel(queryVectorBo.getEmbeddingModelName(), dimension);

            Embedding queryEmbedding = embeddingModel.embed(queryVectorBo.getQuery()).content();
            String collectionName = vectorStoreProperties.getMilvus().getCollectionname() + queryVectorBo.getKid();

            // 查询复用连接，autoFlush 对查询无影响，此处保持 true
            EmbeddingStore<TextSegment> embeddingStore = getMilvusStore(collectionName, dimension, true);

            List<String> resultList = new ArrayList<>();
            EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(queryVectorBo.getMaxResults())
                    .build();
            List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(request).matches();
            for (EmbeddingMatch<TextSegment> match : matches) {
                TextSegment segment = match.embedded();
                if (segment != null) {
                    resultList.add(segment.text());
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("Milvus向量查询失败: kid={}, query={}, error={}", queryVectorBo.getKid(), queryVectorBo.getQuery(), e.getMessage(), e);
            // 返回空列表而不是抛出异常，允许相似度匹配继续处理其他片段
            return new ArrayList<>();
        }
    }

    @Override
    @SneakyThrows
    public void removeById(String id, String modelName) {
        // 注意：此处原逻辑使用 collectionname + id，保持现状
        try {
            int dimension = getModelDimension(modelName);
            EmbeddingStore<TextSegment> embeddingStore = getMilvusStore(vectorStoreProperties.getMilvus().getCollectionname() + id, dimension, false);
            embeddingStore.remove(id);
            log.info("Milvus成功删除向量数据: id={}", id);
        } catch (Exception e) {
            log.warn("Milvus删除向量数据失败: id={}, error={}", id, e.getMessage());
        }
    }

    @Override
    public void removeByDocId(String docId, String kid) {
        try {
            String collectionName = vectorStoreProperties.getMilvus().getCollectionname() + kid;
            // 使用默认维度，因为删除操作不需要精确的维度信息
            EmbeddingStore<TextSegment> embeddingStore = getMilvusStore(collectionName, 1024, false);
            Filter filter = MetadataFilterBuilder.metadataKey("docId").isEqualTo(docId);
            embeddingStore.removeAll(filter);
            log.info("Milvus成功删除 docId={} 的所有向量数据", docId);
        } catch (Exception e) {
            log.warn("Milvus删除向量数据失败: docId={}, kid={}, error={}", docId, kid, e.getMessage());
        }
    }

    @Override
    public void removeByFid(String fid, String kid) {
        try {
            String collectionName = vectorStoreProperties.getMilvus().getCollectionname() + kid;
            // 使用默认维度，因为删除操作不需要精确的维度信息
            EmbeddingStore<TextSegment> embeddingStore = getMilvusStore(collectionName, 1024, false);
            Filter filter = MetadataFilterBuilder.metadataKey("fid").isEqualTo(fid);
            embeddingStore.removeAll(filter);
            log.info("Milvus成功删除 fid={} 的所有向量数据", fid);
        } catch (Exception e) {
            log.warn("Milvus删除向量数据失败: fid={}, kid={}, error={}", fid, kid, e.getMessage());
        }
    }

    @Override
    public String getVectorStoreType() {
        return "milvus";
    }
}
