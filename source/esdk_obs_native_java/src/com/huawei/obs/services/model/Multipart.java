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

/**描述多段中某一个段的信息
 */
public class Multipart
{
    private Integer partNumber;
    
    private Date lastModified;
    
    private String etag;
    
    private Long size;
    
    /**获取分段在多段上传任务中的编号
     * 
     * @return 返回分段在多段上传任务中的编号
     */
    public Integer getPartNumber()
    {
        return partNumber;
    }
    
    /**设置分段在多段上传任务中的编号
     * 
     * @param partNumber 多段上传任务中某一段的编号
     */
    public void setPartNumber(Integer partNumber)
    {
        this.partNumber = partNumber;
    }
    
    /**获取段创建时间
     * 
     * @return 段的创建时间
     */
    public Date getLastModified()
    {
        return lastModified;
    }
    
    /**设置段创建时间
     * 
     * @param lastModified 段的创建时间
     */
    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }
    
    /**获取分段的etag
     * 
     * @return 返回对象的etag
     */
    public String getEtag()
    {
        return etag;
    }
    
    /**设置分段的etag
     * 
     * @param objEtag 段的etag
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }
    
    /**获取分段的数据大小，单位字节
     * 
     * @return 段的数据内容大小
     */
    public Long getSize()
    {
        return size;
    }
    
    /**设置分段的数据的大小，单位字节
     * 
     * @param size 段的数据内容大小
     */
    public void setSize(Long size)
    {
        this.size = size;
    }
}
