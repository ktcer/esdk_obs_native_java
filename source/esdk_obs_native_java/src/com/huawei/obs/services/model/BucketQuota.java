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

/**描述桶配额
 */
public class BucketQuota
{
    private long bucketQuota;

    /**获取配额信息,单位为字节
     * 
     * @return 返回配额信息
     */
    public long getBucketQuota()
    {
        return bucketQuota;
    }

    /**设置配额
     * 
     * @param quota 配额信息,单位为字节
     */
    public void setBucketQuota(long quota)
    {
        this.bucketQuota = quota;
    }

}
