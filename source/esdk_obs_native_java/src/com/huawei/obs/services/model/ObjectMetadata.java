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

/**描述对象元数据
 */
public class ObjectMetadata
{
    private Map<String, Object> metadata = new HashMap<String, Object>();
    
    private Date lastModified;
    
    private Long contentLength;
    
    private String contentType;
    
    private String contentEncoding;

    private String etag;

    /**获取对象元数据MAP
     * 
     * @return 元数据描述MAP
     */
    public Map<String, Object> getMetadata()
    {
        return metadata;
    }

    /** 获取对象的eTag
     * 
     * @return  对象的eTag字符串
     */
    public String getEtag()
    {
        return etag;
    }

    /** 设置对象的eTag
     * 
     * @param objEtag  对象eTag字符串
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }
    
    /**设置对象元数据
     * 
     * @param metadata    对象元数据
     */
    public void setMetadata(Map<String, Object> metadata)
    {
        this.metadata = metadata;
    }

    /**获取对象的修改时间
     * 
     * @return 对象修改时间
     */
    public Date getLastModified()
    {
        return lastModified;
    }

    /**设置对象的修改时间
     * 
     * @param lastModified    对象的修改时间
     */
    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    /** 获取对象内容编码
     * 
     * @return 内容编码
     */
    public String getContentEncoding()
    {
        return contentEncoding;
    }

    /** 设置对象内容编码
     * 
     * @param contentEncoding 对象内容编码
     */
    public void setContentEncoding(String contentEncoding)
    {
        this.contentEncoding = contentEncoding;
    }

    /** 获取对象内容长度
     * 
     * @return 内容长度
     */
    public Long getContentLength()
    {
        return contentLength;
    }
    
    /** 设置对象内容长度
     * 
     * @param contentLength 内容长度
     */
    public void setContentLength(Long contentLength)
    {
        this.contentLength = contentLength;
    }
    
    /** 获取对象内容类型
     * 
     * @return 内容类型text/xml
     */
    public String getContentType()
    {
        return contentType;
    }
    
    /** 设置对象内容类型
     * 
     * @param contentType 内容类型text/xml
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }
}
