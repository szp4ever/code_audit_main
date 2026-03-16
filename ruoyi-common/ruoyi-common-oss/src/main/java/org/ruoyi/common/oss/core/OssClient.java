package org.ruoyi.common.oss.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.ruoyi.common.core.utils.DateUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.oss.constant.OssConstant;
import org.ruoyi.common.oss.entity.UploadResult;
import org.ruoyi.common.oss.enumd.AccessPolicyType;
import org.ruoyi.common.oss.enumd.PolicyType;
import org.ruoyi.common.oss.exception.OssException;
import org.ruoyi.common.oss.properties.OssProperties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * S3 存储协议 所有兼容S3协议的云厂商均支持
 * 阿里云 腾讯云 七牛云 minio
 *
 * @author Lion Li
 */
public class OssClient {

    private final String configKey;

    private final OssProperties properties;

    private final AmazonS3 client;

    public OssClient(String configKey, OssProperties ossProperties) {
        this.configKey = configKey;
        this.properties = ossProperties;
        try {
            AwsClientBuilder.EndpointConfiguration endpointConfig =
                    new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion());

            AWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
            ClientConfiguration clientConfig = new ClientConfiguration();
            // 忽略由于环境变量或系统属性导致的代理配置，避免出现 127.0.0.1 代理导致的连接失败
            clientConfig.setProxyHost(null);
            clientConfig.setProxyPort(-1);
            if (OssConstant.IS_HTTPS.equals(properties.getIsHttps())) {
                clientConfig.setProtocol(Protocol.HTTPS);
            } else {
                clientConfig.setProtocol(Protocol.HTTP);
            }
            // 合并配置：main 的稳定性 + feature 的吞吐与超时
            clientConfig.setMaxConnections(200);
            clientConfig.setConnectionTimeout(60000);
            clientConfig.setSocketTimeout(60000);
            clientConfig.setRequestTimeout(120_000);
            clientConfig.setConnectionTTL(300000);
            clientConfig.setMaxErrorRetry(5);
            clientConfig.setUseTcpKeepAlive(true);
            AmazonS3ClientBuilder build = AmazonS3Client.builder()
                    .withEndpointConfiguration(endpointConfig)
                    .withClientConfiguration(clientConfig)
                    .withCredentials(credentialsProvider)
                    .disableChunkedEncoding();
            if (!StringUtils.containsAny(properties.getEndpoint(), OssConstant.CLOUD_SERVICE)) {
                // minio 使用https限制使用域名访问 需要此配置 站点填域名
                build.enablePathStyleAccess();
            }
            this.client = build.build();

            // createBucket();
        } catch (Exception e) {
            if (e instanceof OssException) {
                throw e;
            }
            throw new OssException("配置错误! 请检查系统配置:[" + e.getMessage() + "]");
        }
    }

    private static String getPolicy(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\"Statement\": [\n{\n\"Action\": [\n");
        builder.append(switch (policyType) {
            case WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucketMultipartUploads\"\n";
            case READ_WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucket\",\n\"s3:ListBucketMultipartUploads\"\n";
            default -> "\"s3:GetBucketLocation\"\n";
        });
        builder.append("],\n\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n},\n");
        if (policyType == PolicyType.READ) {
            builder.append("{\n\"Action\": [\n\"s3:ListBucket\"\n],\n\"Effect\": \"Deny\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n},\n");
        }
        builder.append("{\n\"Action\": ");
        builder.append(switch (policyType) {
            case WRITE ->
                    "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n";
            case READ_WRITE ->
                    "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:GetObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n";
            default -> "\"s3:GetObject\",\n";
        });
        builder.append("\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n}\n],\n\"Version\": \"2012-10-17\"\n}\n");
        return builder.toString();
    }

    public void createBucket() {
        try {
            String bucketName = properties.getBucketName();
            if (client.doesBucketExistV2(bucketName)) {
                return;
            }
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            AccessPolicyType accessPolicy = getAccessPolicy();
            createBucketRequest.setCannedAcl(accessPolicy.getAcl());
            client.createBucket(createBucketRequest);
            client.setBucketPolicy(bucketName, getPolicy(bucketName, accessPolicy.getPolicyType()));
        } catch (Exception e) {
            throw new OssException("创建Bucket失败, 请核对配置信息:[" + e.getMessage() + "]");
        }
    }

    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data), path, contentType);
    }

    public UploadResult upload(InputStream inputStream, String path, String contentType) {
        if (!(inputStream instanceof ByteArrayInputStream)) {
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(inputStream.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), path, inputStream, metadata);
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.setCannedAcl(getAccessPolicy().getAcl());
            client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build();
    }

    public void delete(String path) {
        path = path.replace(getUrl() + "/", "");
        try {
            client.deleteObject(properties.getBucketName(), path);
        } catch (Exception e) {
            throw new OssException("删除文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }

    public UploadResult uploadSuffix(byte[] data, String suffix, String contentType) {
        return upload(data, getPath(properties.getPrefix(), suffix), contentType);
    }

    public UploadResult uploadSuffix(InputStream inputStream, String suffix, String contentType) {
        return upload(inputStream, getPath(properties.getPrefix(), suffix), contentType);
    }

    /**
     * 获取文件元数据
     * 使用 HEAD 请求（getObjectMetadata），不下载文件内容
     *
     * @param path 完整文件路径
     */
    public ObjectMetadata getObjectMetadata(String path) {
        path = path.replace(getUrl() + "/", "");
        return client.getObjectMetadata(properties.getBucketName(), path);
    }

    /**
     * 批量获取指定前缀下所有对象的大小（一次 LIST 请求）
     * 返回 Map<objectKey, sizeBytes>
     *
     * @param prefix 对象键前缀（如 "2026/03/"），传 null 或空串则列出整个桶
     */
    public java.util.Map<String, Long> listObjectSizes(String prefix) {
        java.util.Map<String, Long> result = new java.util.HashMap<>();
        com.amazonaws.services.s3.model.ListObjectsV2Request req =
            new com.amazonaws.services.s3.model.ListObjectsV2Request()
                .withBucketName(properties.getBucketName())
                .withMaxKeys(1000);
        if (prefix != null && !prefix.isEmpty()) {
            req.withPrefix(prefix);
        }
        com.amazonaws.services.s3.model.ListObjectsV2Result listing;
        do {
            listing = client.listObjectsV2(req);
            for (com.amazonaws.services.s3.model.S3ObjectSummary summary : listing.getObjectSummaries()) {
                result.put(summary.getKey(), summary.getSize());
            }
            req.setContinuationToken(listing.getNextContinuationToken());
        } while (listing.isTruncated());
        return result;
    }

    /**
     * 批量获取指定对象键列表的大小
     * 先用 listObjects 拿到桶内所有对象大小，再按 keys 过滤
     * 适用于一次性查询多个文件大小的场景（比逐个 HEAD 快得多）
     *
     * @param objectKeys 需要查询的对象键列表
     */
    public java.util.Map<String, Long> batchGetObjectSizes(java.util.Collection<String> objectKeys) {
        if (objectKeys == null || objectKeys.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        // 提取公共前缀以缩小 LIST 范围
        String commonPrefix = findCommonPrefix(objectKeys);
        java.util.Map<String, Long> allSizes = listObjectSizes(commonPrefix);
        java.util.Set<String> keySet = new java.util.HashSet<>(objectKeys);
        java.util.Map<String, Long> result = new java.util.HashMap<>();
        for (java.util.Map.Entry<String, Long> entry : allSizes.entrySet()) {
            if (keySet.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private String findCommonPrefix(java.util.Collection<String> keys) {
        if (keys.isEmpty()) return "";
        String first = keys.iterator().next();
        String prefix = first;
        for (String key : keys) {
            while (!key.startsWith(prefix)) {
                int lastSlash = prefix.lastIndexOf('/');
                if (lastSlash <= 0) return "";
                prefix = prefix.substring(0, lastSlash + 1);
            }
        }
        // 只保留到最后一个 / 的部分
        int lastSlash = prefix.lastIndexOf('/');
        return lastSlash > 0 ? prefix.substring(0, lastSlash + 1) : "";
    }

    public InputStream getObjectContent(String path) {
        path = path.replace(getUrl() + "/", "");
        S3Object object = client.getObject(properties.getBucketName(), path);
        return object.getObjectContent();
    }

    public String getUrl() {
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = OssConstant.IS_HTTPS.equals(properties.getIsHttps()) ? "https://" : "http://";
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return header + domain;
            }
            return header + properties.getBucketName() + "." + endpoint;
        }
        // minio 单独处理
        if (StringUtils.isNotBlank(domain)) {
            return header + domain + "/" + properties.getBucketName();
        }
        return header + endpoint + "/" + properties.getBucketName();
    }

    public String getPath(String prefix, String suffix) {
        // 生成uuid
        String uuid = IdUtil.fastSimpleUUID();
        // 文件路径
        String path = DateUtils.datePath() + "/" + uuid;
        if (StringUtils.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }
        return path + suffix;
    }

    public String getConfigKey() {
        return configKey;
    }

    /**
     * 获取私有URL链接
     *
     * @param objectKey 对象KEY
     * @param second    授权时间
     */
    public String getPrivateUrl(String objectKey, Integer second) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(properties.getBucketName(), objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + 1000L * second));
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    /**
     * 检查配置是否相同
     */
    public boolean checkPropertiesSame(OssProperties properties) {
        return this.properties.equals(properties);
    }

    /**
     * 获取当前桶权限类型
     *
     * @return 当前桶权限类型code
     */
    public AccessPolicyType getAccessPolicy() {
        return AccessPolicyType.getByType(properties.getAccessPolicy());
    }

}
