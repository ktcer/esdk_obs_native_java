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

import java.util.List;

/**描述列举多段上传对象的返回结果
 */
public class PartListing
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    private List<Multipart> multipartList;
    
    /**获取多段上传任务的上传标识
     * 
     * @return 多段上传任务的上传标识
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /** 设置多段上传任务的上传标识
     * 
     * @param uploadId 多段上传任务的上传标识
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取桶的名称
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置桶的名称
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**设置多段上传任务中分段对象的信息
     * 
     * @param multipartList 多段上传任务中分段对象的信息表
     */
    public void setMultipartList(List<Multipart> multipartList)
    {
        this.multipartList = multipartList;
    }
    
    /**列举多段的分段对象信息
     * 
     * @return 返回段信息列表
     */
    public List<Multipart> getMultipartList()
    {
        return multipartList;
    }
    
}
