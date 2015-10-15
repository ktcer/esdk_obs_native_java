package com.huawei.obs.services.model;
/**
 * 所有请求重定向节点<br/>
 * 描述所有请求的重定向行为，如果这个节点出现，所有其他的兄弟节点都不能出现。
 */
public class RedirectAllRequest
{
    private String protocol;
    
    private String hostName;
    
    /**
     * 返回重定向使用的协议
     * @return 协议
     */
    public String getProtocol()
    {
        return protocol;
    }
    
    /**
     * 设置重定向使用的协议
     * @param protocol 协议
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
    
    /**
     * 返回主机地址
     * @return 重定向到的主机地址
     */
    public String getHostName()
    {
        return hostName;
    }
    
    /**
     * 设置主机地址
     * @param hostName 重定向到的主机地址
     */
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    
}
