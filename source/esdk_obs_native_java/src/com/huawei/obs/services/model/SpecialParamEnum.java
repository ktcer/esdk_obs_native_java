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
 * 特殊操作参数。
 */
public enum SpecialParamEnum
{
    
    /**
     * 获取桶区域位置信息。
     */
    LOCATION("location"),
    /**
     * 获取桶存量信息。
     */
    STORAGEINFO("storageinfo"),
    /**
     * 获取/设置桶配额。
     */
    QUOTA("quota"),
    /**
     * 获取/设置桶（对象）设置权限控制策略。
     */
    ACL("acl"),
    /**
     * 获取/设置桶日志管理配置。
     */
    LOGGING("logging"),
    /**
     * 桶的策略。
     */
    POLICY("policy"),
    /**
     * 桶的生命周期配置。
     */
    LIFECYCLE("lifecycle"),
    /**
     * 桶的 Website 配置。
     */
    WEBSITE("website"),
    /**
     * 桶的多版本状态。
     */
    VERSIONING("versioning"),
    /**
     * 桶的 CORS 配置。
     */
    CORS("cors"),
    /**
     * 列举/创建多段上传任务。
     */
    UPLOADS("uploads"),
    /**
     * 列举桶内对象（含多版本）。
     */
    VERSIONS("versions"),
    /**
     * 批量删除对象。
     */
    DELETE("delete");
    
    /**
     * stringCode对应数据库中和外部的Code
     */
    private String stringCode;
    
    private SpecialParamEnum(String stringCode)
    {
        if (stringCode == null)
        {
            throw new IllegalArgumentException("stringCode is null");
        }
        this.stringCode = stringCode;
    }
    
    public String getStringCode()
    {
        return this.stringCode.toLowerCase();
    }
    
    public static SpecialParamEnum getValueFromStringCode(String stringCode)
    {
        if (stringCode == null)
        {
            throw new IllegalArgumentException("string code is null");
        }
        
        for (SpecialParamEnum installMode : SpecialParamEnum.values())
        {
            if (installMode.getStringCode().equals(stringCode.toLowerCase()))
            {
                return installMode;
            }
        }
        
        throw new IllegalArgumentException("string code is illegal");
    }
}
