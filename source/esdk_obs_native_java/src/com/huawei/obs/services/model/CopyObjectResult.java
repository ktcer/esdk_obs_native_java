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

/**描述复制对象的返回结果
 */
public class CopyObjectResult
{
    private String etag;
    
    private Date lastModified;
    
    /**获取对象的eTag
     * 
     * @return 返回对象的eTag
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
    
    /**获取对象的修改时间
     * 
     * @return 返回日期
     */
    public Date getLastModified()
    {
        return lastModified;
    }
    
    /**设置对象的修改时间
     * 
     * @param lastModified 对象的修改时间
     */
    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }
    
}
