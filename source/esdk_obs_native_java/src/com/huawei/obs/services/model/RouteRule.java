/**
* Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
* eSDK is licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
