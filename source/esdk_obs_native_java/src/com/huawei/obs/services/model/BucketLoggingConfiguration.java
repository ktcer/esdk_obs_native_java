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
import java.util.Arrays;
import java.util.List;

/**
 * 描述桶的日志管理配置
 */

public class BucketLoggingConfiguration
{
    private String targetBucketName = null;

    private String logfilePrefix = null;

    private final List<GrantAndPermission> targetGrantsList = new ArrayList<GrantAndPermission>();

    public BucketLoggingConfiguration()
    {
    }

    /**
     * <p>返回目标桶</p>
     * 在生成日志时源桶的所有者可以指定一个目标桶，将生成的所有日志放到该桶中。
     * @return 目标桶 <br/>
     */
    public String getTargetBucketName()
    {
        return targetBucketName;
    }

    /**
     * <p>设置目标桶</p>
     * <p>在生成日志时，源桶的所有者可以指定一个目标桶，将生成的所有日志放到该桶中。</p>
     * @param targetBucketName 目标桶 <br/>
     */
    public void setTargetBucketName(String targetBucketName)
    {
        this.targetBucketName = targetBucketName;
    }

    /**
     * <p>
     * 返回对象名前缀（即日志文件名前缀）
     * </p>
     * @return 对象名前缀<br/>
     */
    public String getLogfilePrefix()
    {
        return logfilePrefix;
    }

    /**
     * <p>
     * 设置对象名前缀（即日志文件名前缀）
     * </p>
     * @param logfilePrefix 对象名前缀<br/>
     */
    public void setLogfilePrefix(String logfilePrefix)
    {
        this.logfilePrefix = logfilePrefix;
    }

    /**
     * 返回授权信息
     * @return 授权信息 {@link GrantAndPermission}
     */
    public GrantAndPermission[] getTargetGrants()
    {
        return targetGrantsList.toArray(new GrantAndPermission[targetGrantsList
                .size()]);
    }

    /**
     * <p>设置授权信息</p>
     * <p>对于一个桶的日志访问权限来说， owner在创桶时将自动获得对源桶的FULL_CONTROL权限。
     * 不同的权限决定了对不同日志的访问限制。</p>
     * @param targetGrants 授权信息 {@link GrantAndPermission}<br/>
     */
    public void setTargetGrants(GrantAndPermission[] targetGrants)
    {
        targetGrantsList.clear();
        targetGrantsList.addAll(Arrays.asList(targetGrants));
    }

    /**
     * 对于一个桶的日志访问权限来说， owner在创桶时将自动获得对源桶的FULL_CONTROL权限。
     * 不同的权限决定了对不同日志的访问限制。
     * @param targetGrant
     */
    public void addTargetGrant(GrantAndPermission targetGrant)
    {
        targetGrantsList.add(targetGrant);
    }

    @Override
    public String toString()
    {
        String result = super.toString();
        result += ", targetGrants=[" + targetGrantsList + "]";
        return result;
    }
    
    /**
     * 是否启用日志配置管理的功能
     * @return 是否开启了日志配置管理的功能，true表示开启，false表示关闭
     */
    public boolean isLoggingEnabled() {
        return targetBucketName != null
            && logfilePrefix != null;
    }
}
