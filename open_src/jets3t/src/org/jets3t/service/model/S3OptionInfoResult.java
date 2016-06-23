package org.jets3t.service.model;

import java.util.List;

public class S3OptionInfoResult
{
    /**
     * 如果请求的Origin满足服务端的CORS配置，则在响应中包含这个Origin
     */
    private String allowOrigin;
    
    /**
     * 如果请求的headers满足服务端的CORS配置，则在响应中包含这个headers。
     */
    private List<String>allowHeaders;
    
    /**
     * 服务端CORS配置中的MaxAgeSeconds
     */
    private int maxAge;
    
    /**
     * 如果请求的Access-Control-Request-Method满足服务端的CORS配置，则在响应中包含这条rule中的Methods
     */
    private List<String>allowMethods;
    
    /**
     * 服务端CORS配置中的ExposeHeader。
     */
    private List<String>exposeHeaders;

    public String getAllowOrigin()
    {
        return allowOrigin;
    }

    public void setAllowOrigin(String allowOrigin)
    {
        this.allowOrigin = allowOrigin;
    }

    public List<String> getAllowHeaders()
    {
        return allowHeaders;
    }

    public void setAllowHeaders(List<String> allowHeaders)
    {
        this.allowHeaders = allowHeaders;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    public List<String> getAllowMethods()
    {
        return allowMethods;
    }

    public void setAllowMethods(List<String> allowMethods)
    {
        this.allowMethods = allowMethods;
    }

    public List<String> getExposeHeaders()
    {
        return exposeHeaders;
    }

    public void setExposeHeaders(List<String> exposeHeaders)
    {
        this.exposeHeaders = exposeHeaders;
    }
    
}
