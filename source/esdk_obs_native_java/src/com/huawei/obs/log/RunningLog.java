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
