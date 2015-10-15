package com.huawei.obs.services.model;
/**
 * 描述重定向规则匹配的条件
 */
public class RouteRuleCondition
{
    private String keyPrefixEquals;
    private String httpErrorCodeReturnedEquals;
    
    /**
     * 获得对象名的前缀
     * @return 描述当重定向生效时对象名的前缀
     */
    public String getKeyPrefixEquals()
    {
        return keyPrefixEquals;
    }
    
    /**
     * 设置对象名前缀。当发生错误时，如果错误码等于这个值，那么Redirect生效。
     * @param keyPrefixEquals 描述当重定向生效时对象名的前缀
     */
    public void setKeyPrefixEquals(String keyPrefixEquals)
    {
        this.keyPrefixEquals = keyPrefixEquals;
    }
    
    /**
     * 获得HTTP错误码
     * @return Redirect生效时的HTTP错误码
     */
    public String getHttpErrorCodeReturnedEquals()
    {
        return httpErrorCodeReturnedEquals;
    }
    
    /**
     * 设置HTTP错误码
     * @param httpErrorCodeReturnedEquals Redirect生效时的HTTP错误码
     */
    public void setHttpErrorCodeReturnedEquals(String httpErrorCodeReturnedEquals)
    {
        this.httpErrorCodeReturnedEquals = httpErrorCodeReturnedEquals;
    }
    @Override
    public String toString()
    {
        return "RouteRuleCondition [keyPrefixEquals=" + keyPrefixEquals + ", httpErrorCodeReturnedEquals="
            + httpErrorCodeReturnedEquals + "]";
    }
    
    
}
