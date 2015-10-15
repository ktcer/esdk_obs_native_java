package com.huawei.obs.services.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 桶的WebsiteConfiguration
 */
public class WebsiteConfiguration
{
    private String suffix;

    private String key;

    private RedirectAllRequest redirectAllRequestsTo;

    private List<RouteRule> routeRules = new ArrayList<RouteRule>();

    /**
     * 
     * @return suffix suffix被追加在对文件夹的请求的末尾 （例如： Suffix 配置的是“index.html”，请求<br/>
     *         的是“samplebucket/images/”， 返回的数据将 是“ samplebucket”桶内名为“images/index.html”<br/>
     *         的对象的内容）。 Suffix 元素不能为空或者包含“/”字符。<br/>
     */
    public String getSuffix()
    {
        return suffix;
    }

    /**
     * 
     * @param suffix suffix被追加在对文件夹的请求的末尾 （例如： Suffix 配置的是“index.html”，请求<br/>
     *         的是“samplebucket/images/”， 返回的数据将 是“samplebucket”桶内名为“images/index.html”<br/>
     *         的对象的内容）。 Suffix 元素不能为空或者包含“ /”字符。<br/>
     */
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    /**
     * 返回对象名称
     * @return key 对象名称
     */
    public String getKey()
    {
        return key;
    }

    /**
     * 设置对象名称
     * @param key 对象名称
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * 返回路由规则集合
     * @return routeRules 路由规则集合
     */
    public List<RouteRule> getRouteRules()
    {
        return routeRules;
    }

    /**
     * 设置路由规则集合
     * @param routeRules 路由规则集合
     */
    public void setRouteRules(List<RouteRule> routeRules)
    {
        this.routeRules = routeRules;
    }

    /**
     * 获得重定向的站点名
     * @return 重定向的站点名
     */
    public RedirectAllRequest getRedirectAllRequestsTo()
    {
        return redirectAllRequestsTo;
    }

    /**
     * 设置重定向的站点名
     * @param redirectAllRequestsTo 重定向的站点名
     */
    public void setRedirectAllRequestsTo(RedirectAllRequest redirectAllRequestsTo)
    {
        this.redirectAllRequestsTo = redirectAllRequestsTo;
    }

    @Override
    public String toString()
    {
        return "WebsiteConfigration [suffix=" + suffix + ", key=" + key + ", redirectAllRequestsTo="
            + redirectAllRequestsTo + ", routeRules=" + routeRules + "]";
    }
}
