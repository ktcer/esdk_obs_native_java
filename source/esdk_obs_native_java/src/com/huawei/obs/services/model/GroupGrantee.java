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
 * ACL中用户的容器，包含用的标识ID
 */
public class GroupGrantee implements GranteeInterface
{
    private String id = null;

    /**
     * 不带参数创建ACL中的用户对象的默认构造函数
     */
    public GroupGrantee()
    {
    }

    /**
     * 指定策略中用户URI属性的构造函数
     * 
     * @param groupUri 策略中用户的URI属性
     */
    public GroupGrantee(String groupUri)
    {
        this.id = groupUri;
    }

    /**
     * 设置策略中用户的URI属性
     * 
     * @param uri 策略中用户的URI属性
     */
    public void setIdentifier(String uri)
    {
        this.id = uri;
    }

    /**
     * 获取策略中用户的URI属性
     * 
     * @return 策略中用户的URI属性
     */
    public String getIdentifier()
    {
        return id;
    }

    /**
     * 对象比较方法
     * 
     * @param obj 要比较的对象
     * @return true 比较的两个对象相同， false 比较的两个对象不同
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof GroupGrantee)
        {
            return id.equals(((GroupGrantee) obj).id);
        }
        return false;
    }

    /**
     * 对象的HASH方法
     * 
     * @return 返回对象的HASH值
     */
    public int hashCode()
    {
        // for Coverity checking, 复用父类方法
        return super.hashCode();
    }

    /**
     * 返回对象描述信息
     * 
     * @return 返回对象描述字符串
     */
    public String toString()
    {
        return "GroupGrantee [" + id + "]";
    }
}
