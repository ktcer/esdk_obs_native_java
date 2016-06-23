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

/**描述存量信息
 */
public class BucketStorageInfo
{
    private long size;
    
    private long objectNum;

    /**获取存量信息的大小，单位为字节
     * 
     * @return 存量信息的大小
     */
    public long getSize()
    {
        return size;
    }
    
    /** 设置存量信息的大小 ，单位为字节
     * 
     * @param storageSize 存量信息的大小.
     */
    public void setSize(long storageSize)
    {
        this.size = storageSize;
    }

    /**获取存储的对象个数
     * 
     * @return 对象个数
     */
    public long getObjectNumber()
    {
        return objectNum;
    }

    /** 设置存储的对象个数
     * 
     * @param objectNumber 对象个数.
     */
    public void setObjectNumber(long objectNumber)
    {
        this.objectNum = objectNumber;
    }
    
}
