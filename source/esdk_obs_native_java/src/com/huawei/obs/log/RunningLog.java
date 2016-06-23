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

package com.huawei.obs.log;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


public class RunningLog
{
    private final Logger LOGGER = Logger.getLogger(RunningLog.class);
    
    private static RunningLog log = new RunningLog();
    
    private RunningLog()
    {
    }
    
    public static RunningLog getRunningLog()
    {
        return log;
    }
    
    /**
     * @param moduleName 模块名称
     * @param essentialMsg 关键信息
     */
    public void info(String moduleName, String essentialMsg)
    {
        StringBuffer logMsg = new StringBuffer();
        logMsg.append(moduleName).append("|");
        logMsg.append(essentialMsg);
        if(LOGGER.isInfoEnabled())
        {
            LOGGER.info(logMsg);
        }
    }
    
    /**
     * @param moduleName 模块名称
     * @param essentialMsg 关键信息
     */
    @SuppressWarnings("deprecation")
    public void warning(String moduleName, String essentialMsg)
    {
        StringBuffer logMsg = new StringBuffer();
        logMsg.append(moduleName).append("|");
        logMsg.append(essentialMsg);
        if(LOGGER.isEnabledFor(Priority.WARN))
        {
            LOGGER.warn(logMsg);
        }
    }
    
    /**
     * @param moduleName 模块名称
     * @param essentialMsg 关键信息
     */
    @SuppressWarnings("deprecation")
    public void error(String moduleName, String essentialMsg)
    {
        StringBuffer logMsg = new StringBuffer();
        logMsg.append(moduleName).append("|");
        logMsg.append(essentialMsg);
        if(LOGGER.isEnabledFor(Priority.ERROR))
        {
            LOGGER.error(logMsg);
        }
    }
    
    /**
     * @param moduleName 模块名称
     * @param essentialMsg 关键信息
     */
    public void debug(String moduleName, String essentialMsg)
    {
        StringBuffer logMsg = new StringBuffer();
        logMsg.append(moduleName).append("|");
        logMsg.append(essentialMsg);
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug(logMsg);
        }
    }
    
}
