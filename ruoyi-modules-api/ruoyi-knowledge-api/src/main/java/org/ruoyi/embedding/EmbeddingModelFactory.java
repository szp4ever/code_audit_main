package org.ruoyi.embedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.domain.vo.ChatModelVo;
import org.ruoyi.service.IChatModelService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 嵌入模型工厂服务类
 * 负责创建和管理各种嵌入模型实例
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingModelFactory {

    private final ApplicationContext applicationContext;

    private final IChatModelService chatModelService;

    // 模型缓存，使用ConcurrentHashMap保证线程安全
    private final Map<String, BaseEmbedModelService> modelCache = new ConcurrentHashMap<>();

    /**
     * 创建嵌入模型实例
     * 如果模型已存在于缓存中，则直接返回；否则创建新的实例
     *
     * @param embeddingModelName 嵌入模型名称
     * @param dimension          模型维度大小
     */
    public BaseEmbedModelService createModel(String embeddingModelName, Integer dimension) {
        return modelCache.computeIfAbsent(embeddingModelName, name -> {
            ChatModelVo modelConfig = chatModelService.selectModelByName(embeddingModelName);
            if (modelConfig == null) {
                throw new IllegalArgumentException("未找到模型配置，name=" + name);
            }
            
            // 验证模型配置
            validateModelConfig(modelConfig, "向量化");
            
            if (modelConfig.getDimension() != null) {
                modelConfig.setDimension(dimension);
            }
            return createModelInstance(modelConfig.getProviderName(), modelConfig);
        });
    }

    /**
     * 检查模型是否支持多模态
     *
     * @param embeddingModelName 嵌入模型名称
     * @return boolean 如果模型支持多模态则返回true，否则返回false
     */
    public boolean isMultimodalModel(String embeddingModelName) {
        return createModel(embeddingModelName, null) instanceof MultiModalEmbedModelService;
    }

    /**
     * 创建多模态嵌入模型实例
     *
     * @param embeddingModelName 嵌入模型名称
     * @return MultiModalEmbedModelService 多模态嵌入模型服务实例
     * @throws IllegalArgumentException 当模型不支持多模态时抛出
     */
    public MultiModalEmbedModelService createMultimodalModel(String embeddingModelName) {
        BaseEmbedModelService model = createModel(embeddingModelName, null);
        if (model instanceof MultiModalEmbedModelService) {
            return (MultiModalEmbedModelService) model;
        }
        throw new IllegalArgumentException("该模型不支持多模态");
    }

    /**
     * 刷新模型缓存
     * 根据给定的嵌入模型ID从缓存中移除对应的模型
     *
     * @param embeddingModelId 嵌入模型的唯一标识ID
     */
    public void refreshModel(Long embeddingModelId) {
        // 从模型缓存中移除指定ID的模型
        modelCache.remove(embeddingModelId);
    }

    /**
     * 获取所有支持模型工厂的列表
     *
     * @return List<String> 支持的模型工厂名称列表
     */
    public List<String> getSupportedFactories() {
        return new ArrayList<>(applicationContext.getBeansOfType(BaseEmbedModelService.class)
                .keySet());
    }

    /**
     * 创建具体的模型实例
     * 根据提供的工厂名称和配置信息创建并配置模型实例
     *
     * @param factory 工厂名称，用于标识模型类型
     * @param config  模型配置信息
     * @return BaseEmbedModelService 配置好的模型实例
     * @throws IllegalArgumentException 当无法获取指定的模型实例时抛出
     */
    private BaseEmbedModelService createModelInstance(String factory, ChatModelVo config) {
        try {
            // 从Spring上下文中获取模型实例
            BaseEmbedModelService model = applicationContext.getBean(factory, BaseEmbedModelService.class);
            // 配置模型参数
            model.configure(config);
            log.info("成功创建嵌入模型: factory={}, modelId={}", config.getProviderName(), config.getId());

            return model;
        } catch (NoSuchBeanDefinitionException e) {
            throw new IllegalArgumentException("获取不到嵌入模型: " + factory, e);
        }
    }

    /**
     * 验证模型配置是否有效
     * 检查API key、api_host、provider_name等关键配置
     */
    private void validateModelConfig(ChatModelVo modelConfig, String operationName) {
        if (modelConfig == null) {
            throw new IllegalArgumentException(operationName + "失败：模型配置不存在");
        }
        
        // 检查API Host
        if (org.ruoyi.common.core.utils.StringUtils.isBlank(modelConfig.getApiHost())) {
            throw new IllegalArgumentException(String.format(
                "%s失败：模型 %s 的API地址(api_host)未配置。请在chat_model表中为该模型设置正确的api_host。",
                operationName, modelConfig.getModelName()
            ));
        }
        
        // 检查API Key
        if (org.ruoyi.common.core.utils.StringUtils.isBlank(modelConfig.getApiKey())) {
            throw new IllegalArgumentException(String.format(
                "%s失败：模型 %s 的API密钥(api_key)未配置。请在chat_model表中为该模型设置正确的api_key。",
                operationName, modelConfig.getModelName()
            ));
        }
        
        // 检查API Key是否为占位符
        String apiKey = modelConfig.getApiKey().trim();
        if (isPlaceholderApiKey(apiKey)) {
            throw new IllegalArgumentException(String.format(
                "%s失败：模型 %s 的API密钥(api_key)为占位符，需要配置真实的API密钥。\n" +
                "请在chat_model表中将模型 %s 的api_key字段更新为有效的API密钥。\n" +
                "当前值: %s",
                operationName, modelConfig.getModelName(), modelConfig.getModelName(), apiKey
            ));
        }
        
        // 检查Provider Name（对于Embedding模型很重要）
        if (org.ruoyi.common.core.utils.StringUtils.isBlank(modelConfig.getProviderName())) {
            log.warn("模型 {} 的provider_name未配置，可能影响模型实例创建。建议在chat_model表中设置provider_name（如：openai、zhipu、qwen等）",
                modelConfig.getModelName());
        }
        
        // 检查模型名称
        if (org.ruoyi.common.core.utils.StringUtils.isBlank(modelConfig.getModelName())) {
            throw new IllegalArgumentException(String.format(
                "%s失败：模型配置的model_name为空。请在chat_model表中检查模型配置。",
                operationName
            ));
        }
    }

    /**
     * 判断API Key是否为占位符
     */
    private boolean isPlaceholderApiKey(String apiKey) {
        if (org.ruoyi.common.core.utils.StringUtils.isBlank(apiKey)) {
            return true;
        }
        String lowerKey = apiKey.toLowerCase();
        // 常见的占位符模式
        return lowerKey.equals("sk-xx") 
            || lowerKey.equals("sk-xxx")
            || lowerKey.equals("your-api-key")
            || lowerKey.equals("api-key")
            || lowerKey.startsWith("sk-placeholder")
            || lowerKey.matches("sk-[a-z]{1,3}") // sk-a, sk-xx, sk-xxx等短占位符
            || (lowerKey.length() < 10 && lowerKey.startsWith("sk-")); // 过短的sk-开头的key
    }
}