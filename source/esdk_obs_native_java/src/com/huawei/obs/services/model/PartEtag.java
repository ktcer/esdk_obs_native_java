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
/**
 * 段的MD5值（Etag）和编号
 */
public class PartEtag
{
    /**
     * 整个对象的MD5作为Etag
     */
    private String eTag;
    
    /**
     * 段的编号
     */
    private Integer partNumber;

    /**
     * 返回段的ETage值
     * @return the eTag 段的ETage值
     */
    public String geteTag()
    {
        return eTag;
    }

    /**
     * 设置段的ETage值
     * @param eTag 段的ETage值
     */
    public void seteTag(String eTag)
    {
        this.eTag = eTag;
    }

    /**
     * 返回短号
     * @return the partNumber 段号
     */
    public Integer getPartNumber()
    {
        return partNumber;
    }

    /**
     * 设置段号
     * @param partNumber 单号
     */
    public void setPartNumber(Integer partNumber)
    {
        this.partNumber = partNumber;
    }
    
}
