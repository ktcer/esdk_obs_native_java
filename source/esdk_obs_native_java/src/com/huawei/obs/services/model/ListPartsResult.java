package com.huawei.obs.services.model;

import java.util.List;

/**
 * 列出段的返回结果
 */
public class ListPartsResult
{
    /**
     * 桶名
     */
    private String bucket;
    /**
     * 对象名
     */
    private String key;
    /**
     * 上传Id
     */
    private String uploadId;
    
    /**
     * 上传任务的创建者
     */
    private Owner initiator;
    
    /**
     * 和Initiator相同（和S3兼容，  S3在使用IAM的情况下， Initiator和Owner可能不同）
     */
    private Owner owner;
    
    /**
     * 存储类型
     */
    private String storageClass;
    
    /**
     * 上传任务中的段
     */
    private List<Multipart> multipartList;
    
    /**
     * 请求中最大的Part数目
     */
    private Integer maxParts;
    
    /**
     * 返回桶名
     * @return 上传任务所属的桶名
     */
    public String getBucket()
    {
        return bucket;
    }
    
    /**
     * 设置桶名
     * @param bucket 上传任务所属的桶名
     */
    public void setBucket(String bucket)
    {
        this.bucket = bucket;
    }
    
    /**
     * 返回对象名
     * @return 上传任务所属的对象名
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * 设置对象名
     * @param key 上传任务所属的对象名
     */
    public void setKey(String key)
    {
        this.key = key;
    }
    
    /**
     * 返回上传任务id
     * @return 上传任务id
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /**
     * 设置上传任务id
     * @param uploadId 上传任务id
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**
     * 返回任务发起者
     * @return 任务的发起者
     */
    public Owner getInitiator()
    {
        return initiator;
    }
    
    /**
     * 设置任务的发起者
     * @param initiator 任务的发起者
     */
    public void setInitiator(Owner initiator)
    {
        this.initiator = initiator;
    }
    
    /**
     * 返回所有者
     * @return 桶的所有者
     */
    public Owner getOwner()
    {
        return owner;
    }
    
    /**
     * 设置所有者
     * @param owner 桶的所有者
     */
    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }
    
    /**
     * 返回存储类型
     * @return 存储类型
     */
    public String getStorageClass()
    {
        return storageClass;
    }
    
    /**
     * 设置存储类型
     * @param storageClass 存储类型
     */
    public void setStorageClass(String storageClass)
    {
        this.storageClass = storageClass;
    }
    
    /**
     * 获取返回的Part的最大数目
     * @return 返回的Part的最大数目
     */
    public Integer getMaxParts()
    {
        return maxParts;
    }
    
    /**
     * 设置最大返回的Part的最大数目
     * @param maxParts 请求中最大的Part数目
     */
    public void setMaxParts(Integer maxParts)
    {
        this.maxParts = maxParts;
    }
    
    /**
     * 返回上传任务中的段
     * @return 上传任务中的段
     */
    public List<Multipart> getMultipartList()
    {
        return multipartList;
    }
    
    /**
     * 设置上传任务中的段
     * @param multipartList 上传任务中的段
     */
    public void setMultipartList(List<Multipart> multipartList)
    {
        this.multipartList = multipartList;
    }
}
