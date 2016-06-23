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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述桶信息
 * <p>
 * 桶时对象的包含容器，桶名必须全局唯一且遵循如下规则:<br/>
 * <ul>
 * <li>只能包含小写字母、数字、 "-"、 "."。</li>
 * <li>只能以数字或字母开头。</li>
 * <li>长度要求不少于3个字符，并且不能超过63个字符。</li>
 * <li>不能是IP地址。</li>
 * <li>不能以"-"结尾。</li>
 * <li>不可以包括有两个相邻的"."。</li>
 * <li>"."和"-"不能相邻，如"my-.bucket"和"my.-bucket "都是非法的。</li>
 * </ul>
 * </p>
 */
public class S3Bucket
{
    private String bucketName = null;
    
    private Owner owner = null;
    
    private Date creationDate = null;
    
    private String location = null;
    
    private Map<String, Object> metadata = new HashMap<String, Object>();
    
    /**
     * 访问控制策略
     */
    private AccessControlList acl = null;
    
    /**获取桶的名称
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置桶的名称
     * 只能包含小写字母、数字、 "-"、 "."
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取桶的属主
     * 
     * @return 桶的属主
     */
    public Owner getOwner()
    {
        return owner;
    }
    
    /**设置桶的属主
     * 
     * @param bucketOwner 属主
     */
    public void setOwner(Owner bucketOwner)
    {
        this.owner = bucketOwner;
    }
    
    /**获取桶创建时间
     * 
     * @return 桶创建时间
     */
    public Date getCreationDate()
    {
        return creationDate;
    }
    
    /**设置桶创建时间
     * 
     * @param bucketCreationDate 桶创建时间
     */
    public void setCreationDate(Date bucketCreationDate)
    {
        this.creationDate = bucketCreationDate;
    }

    /**
     * 返回桶的元数据
     * @return 桶的元数据
     */
    public Map<String, Object> getMetadata()
    {
        return metadata;
    }

    /**
     * 设置桶的元数据
     * @param metadata 不可以为null
     */
    public void setMetadata(Map<String, Object> metadata)
    {
        this.metadata.putAll(metadata);
    }
    
    /**
     * 返回桶的区域位置
     * @return 桶的区域位置
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * 设置桶的区域位置
     * @param location 桶的区域位置
     */
    public void setLocation(String location)
    {
        this.location = location;
    }

    /**
     * 返回访问控制策略
     * @return 访问控制策略
     */
    public AccessControlList getAcl()
    {
        return acl;
    }

    /**
     * 设置访问控制策略
     * @param acl 访问控制策略
     */
    public void setAcl(AccessControlList acl)
    {
        this.acl = acl;
    }
}
