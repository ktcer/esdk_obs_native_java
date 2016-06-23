package org.jets3t.service.model;

import java.util.List;

public class S3BucketCors
{
    /**
     * 一条Rule的标识，由不超过255个字符的字符串组成
     * 可选
     */
    private String id;
    
    /**
     * 【可选】客户端可以缓存的CORS响应时间，以秒为单位。
     */
    private int maxAgeSecond;
    
    /**
     * 【必选】CORS规则允许的Method,有效值：GET、PUT、HEAD、POST 、DELETE
     */
    private List<String>allowedMethod;
    
    /**
     * 【必选】CORS规则允许的Origin（表示域名的字符串），可以带一个匹配符”*”。每一个AllowedOrigin可以带最多一个“*”通配符
     */
    private List<String>allowedOrigin;
    
    /**
     * 【可选】配置CORS请求中允许携带的“Access-Control-Request-Headers”头域
     */
    private List<String>allowedHeader;
    
    /**
     * 【可选】CORS响应中带的附加头域，给客户端提供额外的信息，不可出现空格
     */
    private List<String>exposeHeader;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getMaxAgeSecond()
    {
        return maxAgeSecond;
    }

    public void setMaxAgeSecond(int maxAgeSecond)
    {
        this.maxAgeSecond = maxAgeSecond;
    }

    public List<String> getAllowedMethod()
    {
        return allowedMethod;
    }

    public void setAllowedMethod(List<String> allowedMethod)
    {
        this.allowedMethod = allowedMethod;
    }

    public List<String> getAllowedOrigin()
    {
        return allowedOrigin;
    }

    public void setAllowedOrigin(List<String> allowedOrigin)
    {
        this.allowedOrigin = allowedOrigin;
    }

    public List<String> getAllowedHeader()
    {
        return allowedHeader;
    }

    public void setAllowedHeader(List<String> allowedHeader)
    {
        this.allowedHeader = allowedHeader;
    }

    public List<String> getExposeHeader()
    {
        return exposeHeader;
    }

    public void setExposeHeader(List<String> exposeHeader)
    {
        this.exposeHeader = exposeHeader;
    }
    
    
    public String toXML()
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<CORSConfiguration><CORSRule>";
        if (this.id != null){
            xml += "<ID>" + this.id + "</ID>";
        }
        xml += listToXML("AllowedMethod", this.allowedMethod);
        xml += listToXML("AllowedOrigin", this.allowedOrigin);
        xml += listToXML("AllowedHeader", this.allowedHeader);
        xml += "<MaxAgeSeconds>" + this.maxAgeSecond + "</MaxAgeSeconds>";
        xml += listToXML("ExposeHeader", this.exposeHeader);
        xml += "</CORSRule></CORSConfiguration>";
        return xml;
    }
    
    private String listToXML(String root,List<String>list)
    {
        String xml = "";
        
        if (list == null || root == null){
            return xml;
        }
        String rootStart = "<" + root + ">";
        String rootEnd  = "</" + root + ">";
        for (int i = 0; i < list.size(); i++)
        {
            xml += rootStart + list.get(i) + rootEnd;
        }
        return xml;   
    }
     
}
