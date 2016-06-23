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


/**描述获取对象的请求
 */
public class GetObjectRequest
{
    private String bucketName;
    
    private String objectKey;
    
    private Long rangeStart;
    
    private Long rangeEnd;
    
    private String versionId;
    
    /**指定桶名和对象名的构造函数
     * 
     *@param bucketName 桶的名称
     *@param objectKey 对象名称
     */
    public GetObjectRequest(String bucketName, String objectKey)
    {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }
    
    /**返回对象起始位置
     * 
     * @return 对象起始位置
     */
    public Long getRangeStart()
    {
        return rangeStart;
    }

    /**设置对象起始位置
     * 
     * @param rangeStart 对象起始位置
     */
    public void setRangeStart(Long rangeStart)
    {
        this.rangeStart = rangeStart;
    }

    /**返回对象结束位置
     * 
     * @return 对象结束位置
     */
    public Long getRangeEnd()
    {
        return rangeEnd;
    }

    /**设置对象结束
     * 
     * @param rangeEnd 对象内容结束位置
     */
    public void setRangeEnd(Long rangeEnd)
    {
        this.rangeEnd = rangeEnd;
    }
    
    /**返回对象所在的桶
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置对象所在的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**返回对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置要获取的对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }

    /**
     * 返回版本所属的对象
     * @return 版本所属的对象
     */
    public String getVersionId()
    {
        return versionId;
    }
    
    /**
     * 设置版本所属的对象
     * @param versionId 版本所属的对象
     */
    public void setVersionId(String versionId)
    {
        this.versionId = versionId;
    }
}
