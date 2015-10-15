package com.huawei.obs.services.model;

import java.util.Arrays;

/**
 * 列出对象返回结果
 */
public class ListVersionsResult
{
    /**
     * 桶名
     */
    private String buketName;
    
    /**
     * 对象名前缀
     */
    private String prefix;
    
    /**
     * 列举对象时的起始位置
     */
    private String keyMaker;
    
    /**
     * 请求消息中设置的versionIdMarker值，
     * 与key-marker配合使用，
     * 返回的对象列表将是按照字典顺序排序后在该标识符以后的所有对象。
     */
    private String versionIdMarker;
    
    /**
     * 返回的最大对象数
     */
    private String maxKeys;
    
    /**
     * 表明是否本次返回的ListVersionsResult结果列表被截断。
     * “ true”表示本次没有返回全部结果；
     * “ false”表示本次已经返回了全部结果。
     */
    private boolean isTruncated;
    
    /**
     * 对象的版本信息或者对象被删除的信息
     */
    private VersionOrDeleteMarker[] versions;

    /**
     * 返回桶名
     * @return 所属桶的桶名
     */
    public String getBuketName()
    {
        return buketName;
    }

    /**
     * 设置桶名
     * @param buketName 所属桶的桶名
     */
    public void setBuketName(String buketName)
    {
        this.buketName = buketName;
    }

    /**
     * 返回对象名前缀
     * @return prefix 对象名前缀
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * 设置对象名前缀
     * @param prefix 对象名前缀
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * 返回对象列表的标识符
     * @return 一个标识符，返回的对象列表将是按照字典顺序排序后在这个标识符以后的所有对象。
     */
    public String getKeyMaker()
    {
        return keyMaker;
    }

    /**
     * 设置对象列表的标识符
     * @param keyMaker 一个标识符，返回的对象列表将是按照字典顺序排序后在这个标识符以后的所有对象。
     */
    public void setKeyMaker(String keyMaker)
    {
        this.keyMaker = keyMaker;
    }

    /**
     * 返回一个版本号标识符
     * @return versionIdMarker 与key-marker配合使用，返回的对象列表将是按照字典顺序排序后在该标识符以后的所有对象。
     * 如果version-id-marker不是key-marker的一个版本号，则该参数无效。
     */
    public String getVersionIdMarker()
    {
        return versionIdMarker;
    }

    /**
     * 设置一个版本号标志符
     * @param versionIdMarker 与key-marker配合使用， 返回的对象列表将是按照字典顺序排序后在该标识符以后的所有对象。
     * 如果version-id-marker不是key-marker的一个版本号，则该参数无效。
     */
    public void setVersionIdMarker(String versionIdMarker)
    {
        this.versionIdMarker = versionIdMarker;
    }

    /**
     * 获取返回的最大对象数
     * @return maxKeys 返回的最大对象数
     */
    public String getMaxKeys()
    {
        return maxKeys;
    }

    /**
     * 设置返回的最大对象数
     * @param maxKeys 返回的最大对象数
     */
    public void setMaxKeys(String maxKeys)
    {
        this.maxKeys = maxKeys;
    }

    /**
     * 表明是否本次返回的ListVersionsResult结果列表被截断。
     * @return true 表示本次没有返回全部结果；false 表示本次已经返回了全部结果。
     */
    public boolean isTruncated()
    {
        return isTruncated;
    }

    /**
     * 设置Truncated的值，表明是否本次返回的ListVersionsResult结果列表被截断。
     * @param truncated true表示本次没有返回全部结果；false表示本次已经返回了全部结果。 
     */
    public void setTruncated(boolean truncated)
    {
        this.isTruncated = truncated;
    }

    /**
     * 返回桶内的对象及版本信息
     * @return VersionOrDeleteMarker[] 桶内对象的版本信息和删除信息，详细描述见{@link VersionOrDeleteMarker}
     */
    public VersionOrDeleteMarker[] getVersions()
    {
        return versions;
    }

    /**
     * 设置桶内的对象及版本信息
     * @param versions  桶内对象的版本信息和删除信息，详细描述见{@link VersionOrDeleteMarker}
     */
    public void setVersions(VersionOrDeleteMarker[] versions)
    {
        this.versions = versions;
    }

    @Override
    public String toString()
    {
        return "ListVersionsResult [buketName=" + buketName + ", prefix=" + prefix + ", keyMaker=" + keyMaker
            + ", versionIdMarker=" + versionIdMarker + ", maxKeys=" + maxKeys + ", isTruncated=" + isTruncated
            + ", versions=" + Arrays.toString(versions) + "]";
    }
}
