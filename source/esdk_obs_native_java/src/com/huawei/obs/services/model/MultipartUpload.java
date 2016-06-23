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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**描述多段上传的任务
 */
public class MultipartUpload
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    private Date initiatedDate;
    
    private String storageClass;
    
    private Owner owner;
    
    private Owner initiator;
    
    private List<Multipart> multipartsUploaded = new ArrayList<Multipart>();

    /**获取多段上传任务的发起者
     * 
     * @return 多段上传任务的发起者
     */
    public Owner getInitiator()
    {
        return initiator;
    }
    
    /**设置多段上传任务的发起者
     * 
     * @param initiator 多段上传任务的发起者
     */
    public void setInitiator(Owner initiator)
    {
        this.initiator = initiator;
    }
    
    /**获取多段上传任务的属主
     * 
     * @return 多段上传任务的属主
     */
    public Owner getOwner()
    {
        return owner;
    }
    
    /**设置多段上传任务的属主
     * 
     * @param owner 多段上传任务的属主
     */
    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }

    /**获取多段上传任务的编号
     * 
     * @return 多段上传任务的编号
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /**设置多段上传任务的编号
     * 
     * @param uploadId 多段上传任务的编号
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取多段上传任务所属的桶
     * 
     * @return 多段上传任务所属的桶
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置多段上传任务所属的桶
     * 
     * @param bucketName 多段上传任务所属的桶
    
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取多段上传任务的对象名称
     * 
     * @return 多段上传任务的对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置多段上传任务的对象名称
     * 
     * @param objectKey 多段上传任务的对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**获取多段对象的存储类别
     * 
     * @return 多段对象的存储类别
     */
    public String getStorageClass()
    {
        return storageClass;
    }
    
    /**设置多段对象的存储类别
     * 
     * @param storageClass 多段对象的存储类别
     */
    public void setStorageClass(String storageClass)
    {
        this.storageClass = storageClass;
    }
    
    /**获取多段上传任务的创建时间
     * 
     * @return 多段上传任务的创建时间
     */
    public Date getInitiatedDate()
    {
        return initiatedDate;
    }
    
    /**设置多段上传任务的创建时间
     * 
     * @param initiatedDate 多段上传任务的创建时间
     */
    public void setInitiatedDate(Date initiatedDate)
    {
        this.initiatedDate = initiatedDate;
    }

    /**
     * 获得多段上传的任务
     */
    public List<Multipart> getMultipartsUploaded()
    {
        return multipartsUploaded;
    }
    
    /**
     * 设置多段上传任务
     */
    public void setMultipartsUploaded(List<Multipart> multipartsUploaded)
    {
        this.multipartsUploaded = multipartsUploaded;
    }
    
}
