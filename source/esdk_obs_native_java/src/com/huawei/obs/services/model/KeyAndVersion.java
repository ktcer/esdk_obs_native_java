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
 * 版本和对象
 * <p>
 * 用于在删除对象时，指定删除哪一版本的对象 @see {@link DeleteObjectsRequest}
 */
public class KeyAndVersion
{
    protected String key, version;
    
    /**
     * 构造方法
     * @param key 对象名称
     * @param version 版本
     */
    public KeyAndVersion(String key, String version)
    {
        this.key = key;
        this.version = version;
    }
    
    /**
     * 构造方法
     * @param key 对象名称
     */
    public KeyAndVersion(String key)
    {
        this(key, null);
    }
    
    /**
     * 返回对象名称
     * @return 对象名称
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * 返回对象的版本号
     * @return 版本号
     */
    public String getVersion()
    {
        return version;
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
     * 设置对象的版本号
     * @param version 对象的版本号
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    
}
