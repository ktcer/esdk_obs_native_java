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


/**描述列举段的请求
 */
public class ListPartsRequest
{
    /**
     * 桶名
     */
    private String bucketName;
    
    /**
     * 对象名
     */
    private String key;
    
    /**
     * 上传Id
     */
    private String uploadId;
    
    /**
     * 规定在列举已上传段响应中的最大Part数目。
     * 默认值1000。
     */
    private Integer maxParts;
    
    /**
     * 指定List的起始位置，只有Part Number数目大于该参数的Part会被列出。
     */
    private Integer partNumberMarker;
    
    /**
     * @return 列出的段所属的桶的桶名
     */
    public String getBucketName()
    {
        return bucketName;
    }

    /**
     * @param bucketName 列出的段所属的桶的桶名
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    /**
     * @return 列出的段所属的对象的对象名
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key 列出的段所属的对象的对象名
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * @return 上传任务的id
     */
    public String getUploadId()
    {
        return uploadId;
    }

    /**
     * @param uploadId 上传任务的id
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }

    /**
     * @return 规定在列举已上传段响应中的最大Part数目
     */
    public Integer getMaxParts()
    {
        return maxParts;
    }

    /**
     * @param maxParts 规定在列举已上传段响应中的最大Part数目
     */
    public void setMaxParts(Integer maxParts)
    {
        this.maxParts = maxParts;
    }

    /**
     * @return 指定List的起始位置，只有Part Number数目大于该参数的Part会被列出。
     */
    public Integer getPartNumberMarker()
    {
        return partNumberMarker;
    }

    /**
     * @param partNumberMarker 指定List的起始位置，只有Part Number数目大于该参数的Part会被列出。
     */
    public void setPartNumberMarker(Integer partNumberMarker)
    {
        this.partNumberMarker = partNumberMarker;
    }

    @Override
    public String toString()
    {
        return "ListPartsRequest [bucketName=" + bucketName + ", key=" + key + ", uploadId=" + uploadId + ", maxParts="
            + maxParts + ", partNumberMarker=" + partNumberMarker + "]";
    }
}
