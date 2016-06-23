package org.jets3t.service.model;

import java.util.List;

public class S3OptionInfoRequest
{
    /**
     * 【必选】预请求指定的跨域请求Origin（通常为域名）
     */
    private String origin;
    
    /**
     * 【必选】实际请求可以带的HTTP方法
     */
    private List<String>requestMethod;
    
    /**
     * 【可选】实际请求可以带的HTTP头域
     */
    private List<String>requestHeaders;

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public List<String> getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(List<String> requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public List<String> getRequestHeaders()
    {
        return requestHeaders;
    }

    public void setRequestHeaders(List<String> requestHeaders)
    {
        this.requestHeaders = requestHeaders;
    }
    
    
}
