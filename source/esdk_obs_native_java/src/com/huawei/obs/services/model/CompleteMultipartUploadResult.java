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

/**描述多段合并的返回结果
 */
public class CompleteMultipartUploadResult
{
    private String bucketName;
    
    private String objectKey;
    
    private String etag;
    
    /**获取要合并的多段上传任务所属的桶
     * 
     * @return 返回桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要合并的多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取要生成的对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置要生成的对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**获取对象的eTag
     * 
     * @return 对象的etag
     */
    public String getEtag()
    {
        return etag;
    }
    
    /**设置对象的eTag
     * 
     * @param objEtag 对象的eTag
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }
}
