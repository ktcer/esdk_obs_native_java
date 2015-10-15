package com.huawei.obs.services.model;
/**
 * 版本状态
 */
public class BucketVersioningConfiguration
{
    /**
     * 暂停多版本状态
     */
    public static final String SUSPENDED = "Suspended";
    
    /**
     * 启用多版本状态
     */
    public static final String ENABLED = "Enabled";

    private String status;
    
    /**
     * 创建一个版本状态
     * <p>
     * 提示：如果一个桶的多版本状态一旦被启用，则版本状态将无法关闭，或更改为{@link #SUSPENDED suspended}，
     * @param status
     * @see #ENABLED
     * @see #SUSPENDED
     */
    public BucketVersioningConfiguration(String status)
    {
        setStatus(status);
    }

    /**
     * 获取多版本状态
     * @return status 多版本状态
     */
    public String getStatus()
    {
        return status;
    }
    
    /**
     * 设置多版本状态
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

}
