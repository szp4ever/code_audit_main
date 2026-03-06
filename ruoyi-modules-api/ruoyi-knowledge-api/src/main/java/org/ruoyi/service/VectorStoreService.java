package org.ruoyi.service;

import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.domain.bo.QueryVectorBo;
import org.ruoyi.domain.bo.StoreEmbeddingBo;

import java.util.List;

/**
 * 向量库管理
 *
 * @author ageer
 */
public interface VectorStoreService {

    void storeEmbeddings(StoreEmbeddingBo storeEmbeddingBo) throws ServiceException;

    /**
     * 存储向量（带进度更新支持）
     * @param storeEmbeddingBo 向量存储参数
     * @param attachProcessService 处理服务（用于进度更新，可为null）
     */
    default void storeEmbeddings(StoreEmbeddingBo storeEmbeddingBo, org.ruoyi.service.IAttachProcessService attachProcessService) throws ServiceException {
        //默认实现：调用原方法（向后兼容）
        storeEmbeddings(storeEmbeddingBo);
    }

    List<String> getQueryVector(QueryVectorBo queryVectorBo);

    void createSchema(String kid, String embeddingModelName);

    void removeById(String id, String modelName) throws ServiceException;

    void removeByDocId(String docId, String kid) throws ServiceException;

    void removeByFid(String fid, String kid) throws ServiceException;
}
