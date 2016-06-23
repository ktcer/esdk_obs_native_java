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

/**描述取消多段操作的请求
 */
public class AbortMultipartUploadRequest
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    /**获取要取消的多段上传任务的Id
     * 
     * @return 返回要取消的多段上传任务的Id
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /**设置要取消的多段上传任务的Id
     * 
     * @param uploadId 多段上传任务Id
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取要取消的多段上传任务所属的桶
     * 
     * @return 要取消的多段上传任务所属的桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要取消的多段上传任务所属的桶
     * 
     * @param bucketName 多段上传任务取消的对象所属桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取要取消的多段上传任务上传的对象
     * 
     * @return 返回对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置要取消的多段上传任务上传的对象
     * 
     @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
}
