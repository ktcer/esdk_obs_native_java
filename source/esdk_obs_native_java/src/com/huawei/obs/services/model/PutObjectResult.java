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

/**描述put方式上传对象返回结果
 */
public class PutObjectResult
{
    private String etag;

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
     * @param objEtag 对象eTag
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }

}
