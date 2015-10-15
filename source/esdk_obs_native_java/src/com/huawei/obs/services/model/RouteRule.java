package com.huawei.obs.services.model;
/**
 * 路由规则<br/>
 * 包含重定向条件和重定向信息
 */
public class RouteRule
{
    private RouteRuleCondition condition;
    private Redirect redirect;
    
    /**
     * 返回重定向的条件
     * @return 重定向规则匹配的条件
     */
    public RouteRuleCondition getCondition()
    {
        return condition;
    }
    
    /**
     * 设置重定向的条件
     * @param condition 重定向规则匹配的条件
     */
    public void setCondition(RouteRuleCondition condition)
    {
        this.condition = condition;
    }
    
    /**
     * 返回重定向的信息
     * @return 重定向信息
     */
    public Redirect getRedirect()
    {
        return redirect;
    }
    
    /**
     * 设置重定向的信息
     * @param redirect 重定向信息
     */
    public void setRedirect(Redirect redirect)
    {
        this.redirect = redirect;
    }
    
}
