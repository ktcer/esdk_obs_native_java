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

import java.util.ArrayList;
import java.util.List;
/**
 * 描述桶的CORS信息的对象
 */
public class S3BucketCors
{
    
    private List<BucketCorsRule> rules;

    /**
     * 返回桶的CORS规则列表
     */
    public List<BucketCorsRule> getRules() {
        if(null == rules)
        {
            return rules = new ArrayList<BucketCorsRule>();
        }
        return rules;
    }

    /**
     * 设置桶的CORS规则
     * @param rules 规则列表
     */
    public void setRules(List<BucketCorsRule> rules) {
        this.rules = rules;
    }

    /**
     * 构建一个描述桶的CORS信息的对象
     * @param rules CORS规则列表
     */
    public S3BucketCors(List<BucketCorsRule> rules) {
        this.rules = rules;
    }
    
    /**
     * 构建一个描述桶的CORS信息的对象
     */
    public S3BucketCors() {
        super();
    }

    @Override
    public String toString()
    {
        return "S3BucketCors [rules=" + rules + "]";
    }
    
}
