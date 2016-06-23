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

/**描述复制对象的请求
 */
public class CopyObjectRequest
{
    private String sourceBucketName;
    
    private String sourceObjectKey;
    
    private String destinationBucketName;
    
    private String destinationObjectKey;
    
    private ObjectMetadata newObjectMetadata;
    
    private boolean replaceMetadata;
    
    // 2016-03-14 swx231817 
    private Date ifModifiedSince;
    
    private Date ifUnmodifiedSince;
    
    private String ifMatchTag;
    
    private String ifNoneMatchTag;
    
    private String versionId;
    
    /**获取修改时间
     * 
     * @return  修改时间
     */
    public Date getIfModifiedSince()
    {
        return ifModifiedSince;
    }
    
    /**设置修改时间
     * 
     * @param ifModifiedSince  修改时间，只有当源对象在此参数指定的时间之后修改过才进行复制对象操作，否则返回412（前置条件不满足）
     */
    public void setIfModifiedSince(Date ifModifiedSince)
    {
        this.ifModifiedSince = ifModifiedSince;
    }
    
    /**获取未修改时间
     * 
     * @return  修改时间
     */
    public Date getIfUnmodifiedSince()
    {
        return ifUnmodifiedSince;
    }
    
    /**设置未修改时间
     * 
     * @param ifUnmodifiedSince  修改时间，只有当源对象在此参数指定的时间之后没有修改过才进行复制对象操作，否则返回412（前置条件不满足）
     */
    public void setIfUnmodifiedSince(Date ifUnmodifiedSince)
    {
        this.ifUnmodifiedSince = ifUnmodifiedSince;
    }
    
    /**获取要赋值对象的Etag
     * 
     * @return  对象的Etag
     */
    public String getIfMatchTag()
    {
        return ifMatchTag;
    }
    
    /**获取要复制对象的Etag
     * 
     * @param ifMatchTag 对象的Etag，只有当源对象的Etag与此参数指定的值相等时才进行复制对象操作，否则返回412（前置条件不满足）
     */
    public void setIfMatchTag(String ifMatchTag)
    {
        this.ifMatchTag = ifMatchTag;
    }
    
    /**获取要复制对象的Etag
     * 
     * @return  对象的Etag
     */
    public String getIfNoneMatchTag()
    {
        return ifNoneMatchTag;
    }
    
    /**设置要复制对象的Etag
     * 
     * @param ifNoneMatchTag 对象的Etag，只有当源对象的Etag与此参数指定的值不相等时才进行复制对象操作，否则返回412（前置条件不满足）
     */
    public void setIfNoneMatchTag(String ifNoneMatchTag)
    {
        this.ifNoneMatchTag = ifNoneMatchTag;
    }
    
    /**获取要复制对象的版本ID
     * 
     * @return  对象版本ID
     */
    public String getVersionId()
    {
        return versionId;
    }
    
    /**设置要复制对象的版本ID
     * 
     * @param versionId  对象版本ID
     */
    public void setVersionId(String versionId)
    {
        this.versionId = versionId;
    }
    
    /**创建对象拷贝的请求的构造参数
     * 
     * @param sourceBucketName 要复制的源桶
     * @param sourceObjectKey 要复制的源对象
     * @param destinationBucketName 要复制的目标桶
     * @param destinationObjectKey 要复制的目标对象
     */
    public CopyObjectRequest(String sourceBucketName, String sourceObjectKey, String destinationBucketName,
        String destinationObjectKey)
    {
        this.sourceBucketName = sourceBucketName;
        this.sourceObjectKey = sourceObjectKey;
        this.destinationBucketName = destinationBucketName;
        this.destinationObjectKey = destinationObjectKey;
    }
    
    /**获取要复制的源桶的名称
     * 
     * @return  桶的名称
     */
    public String getSourceBucketName()
    {
        return sourceBucketName;
    }
    
    /**设置要复制的源桶的名称
     * 
     * @param sourceBucketName 源桶的名称
     */
    public void setSourceBucketName(String sourceBucketName)
    {
        this.sourceBucketName = sourceBucketName;
    }
    
    /**获取要复制的源对象名称
     * 
     * @return  对象名称
     */
    public String getSourceObjectKey()
    {
        return sourceObjectKey;
    }
    
    /**设置要复制的源对象名称
     * 
     * @param sourceObjectKey 源对象名称
     */
    public void setSourceObjectKey(String sourceObjectKey)
    {
        this.sourceObjectKey = sourceObjectKey;
    }
    
    /**获取要复制的目标桶的名称
     * 
     * @return  桶的名称
     */
    public String getDestinationBucketName()
    {
        return destinationBucketName;
    }
    
    /**设置要复制的目标桶
     * 
     * @param  destinationBucketName 桶的名称
     */
    public void setDestinationBucketName(String destinationBucketName)
    {
        this.destinationBucketName = destinationBucketName;
    }
    
    /**获取要复制的目标对象名称
     * 
     * @return  对象名称
     */
    public String getDestinationObjectKey()
    {
        return destinationObjectKey;
    }
    
    /**设置要复制的目标对象的名称
     * 
     * @param  destinationObjectKey 对象的名称
     */
    public void setDestinationObjectKey(String destinationObjectKey)
    {
        this.destinationObjectKey = destinationObjectKey;
    }
    
    /**获取要复制的对象的元数据
     * 
     * @return  ObjectMetadata 新对象元数据
     */
    public ObjectMetadata getNewObjectMetadata()
    {
        return newObjectMetadata;
    }
    
    /**设置要复制的对象的元数据
     * 
     * @param  newObjectMetadata 新对象元数据
     */
    public void setNewObjectMetadata(ObjectMetadata newObjectMetadata)
    {
        this.newObjectMetadata = newObjectMetadata;
    }
    
    /**是否创建新对象
     * 
     * @return true 创建新的元数据， false 不创建新的元数据 
     */
    public boolean isReplaceMetadata()
    {
        return replaceMetadata;
    }
    
    /**设置是否创建新对象
     * 
     * @param replaceMetadata 是否创建新对象标志
     */
    public void setReplaceMetadata(boolean replaceMetadata)
    {
        this.replaceMetadata = replaceMetadata;
    }
}
