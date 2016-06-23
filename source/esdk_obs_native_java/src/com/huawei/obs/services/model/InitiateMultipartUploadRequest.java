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

/**描述多段上传任务初始化请求
 */
public class InitiateMultipartUploadRequest
{
    private String bucketName;
    
    private String objectKey;
    
    private String webSiteRedirectLocation;
    
    private AccessControlList acl;
    
    /**获取要初始化的多段上传对象的访问控制列表
     * 
     * @return 返回访问控制列表
     */
    public AccessControlList getAcl()
    {
        return acl;
    }
    
    /**设置要初始化的多段上传对象的访问控制列表
     * 
     * @param acl 访问控制列表
     */
    public void setAcl(AccessControlList acl)
    {
        this.acl = acl;
    }
    
    /**获取要初始化的多段上传任务所属的桶
     * 
     * @return 返回桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要初始化的多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取多段上传任务要形成的对象名称
     * 
     * @return 返回多段上传任务要形成的对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置多段上传任务要形成的对象名称
     * 
     * @param objectKey 多段上传任务要形成的对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**
     * 设置Website重定向链接
     * 当桶设置了Website配置，可以将获取这个对象的请求重定向到桶内另一个对象或一个外部的URL。
     * @return  Website重定向链接
     */
    public String getWebSiteRedirectLocation()
    {
        return webSiteRedirectLocation;
    }
    
    /**
     * 当桶设置了Website重定向链接，可以将获取这个对象的请求重定向到桶内另一个对象或一个外部的URL。
     * @param webSiteRedirectLocation  Website重定向链接
     */
    public void setWebSiteRedirectLocation(String webSiteRedirectLocation)
    {
        this.webSiteRedirectLocation = webSiteRedirectLocation;
    }
}
