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

/**描述批量删除对象请求
 */
public class DeleteObjectsRequest
{
    private String bucketName;
    
    private boolean quiet;
    
    private KeyAndVersion[] keyAndVersions;
    
    /**获取批量删除对象所属的桶
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置批量删除对象所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取对象的删除模式quiet或verbose
     * 
     * @return true:只返回对象删除失败信息， false:返回对象删除成功的信息
     */
    public boolean isQuiet()
    {
        return quiet;
    }
    
    /**设置对象的删除模式quiet或verbose
     * 
     * @param quiet true:只返回对象删除失败信息， false:返回对象删除成功的信息
     */
    public void setQuiet(boolean quiet)
    {
        this.quiet = quiet;
    }
    
    /**
     * 返回要删除的指定的版本的对象
     * @return 删除的指定版本对象
     */
    public KeyAndVersion[] getKeyAndVersions()
    {
        return keyAndVersions;
    }
    
    /**
     * 指定要删除的某一版本的对象
     * @param keyAndVersions 版本对象
     */
    public void setKeyAndVersions(KeyAndVersion[] keyAndVersions)
    {
        this.keyAndVersions = keyAndVersions;
    }
}
