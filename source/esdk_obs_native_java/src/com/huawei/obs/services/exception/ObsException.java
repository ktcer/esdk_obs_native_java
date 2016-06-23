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

package com.huawei.obs.services.exception;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**显示服务器不能正常处理业务时返回的信息。包括对应请求的一些描述信息，不能正常处理的原因和错误码。
 */
public class ObsException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    private String xmlMessage = null;
    
    private String errorCode = null;
    
    private String errorMessage = null;
    
    private String errorRequestId = null;
    
    private String errorHostId = null;
    
    private Map<String, String> responseHeaders = null;
    
    private int responseCode = -1;
    
    private String responseStatus = null;
    
    private String responseDate = null;
    
    private String requestVerb = null;
    
    private String requestPath = null;
    
    private String requestHost = null;
    
    /**默认构造函数
     * 
     * @param message Exception的基本描述信息
     */
    public ObsException(String message)
    {
        this(message, null, null);
    }
    
    /**指定描述信息创建异常对象
     * 
     * @param message Exception的基本描述信息
     * @param xmlMessage Exception的XML格式描述信息
     */
    public ObsException(String message, String xmlMessage)
    {
        this(message, xmlMessage, null);
    }
    
    private ObsException(String message, String xmlMessage, Throwable cause)
    {
        super(message, cause);
        if (xmlMessage != null)
        {
            parseXmlMessage(xmlMessage);
        }
    }
    
    /**返回异常的描述信息
     * 
     * @return  异常对象的描述字符串
     */
    @Override
    public String toString()
    {
        String myString = super.toString();
        
        if (requestVerb != null)
        {
            myString +=
                " " + requestVerb + " '" + requestPath + "'"
                    + (requestHost != null ? " on Host '" + requestHost + "'" : "")
                    + (responseDate != null ? " @ '" + responseDate + "'" : "");
        }
        if (responseCode != -1)
        {
            myString += " -- ResponseCode: " + responseCode + ", ResponseStatus: " + responseStatus;
        }
        if (isParsedFromXmlMessage())
        {
            myString += ", XML Error Message: " + xmlMessage;
        }
        else
        {
            if (errorRequestId != null)
            {
                myString += ", RequestId: " + errorRequestId + ", HostId: " + errorHostId;
            }
        }
        return myString;
    }
    
    private boolean isParsedFromXmlMessage()
    {
        return xmlMessage != null;
    }
    
    private String findXmlElementText(String xmlMsg, String elementName)
    {
        Pattern pattern = Pattern.compile(".*<" + elementName + ">(.*)</" + elementName + ">.*");
        Matcher matcher = pattern.matcher(xmlMsg);
        if (matcher.matches() && matcher.groupCount() == 1)
        {
            return matcher.group(1);
        }
        else
        {
            return null;
        }
    }
    
    private void parseXmlMessage(String xmlMsg)
    {
        xmlMsg = xmlMsg.replaceAll("\n", "");
        this.xmlMessage = xmlMsg;
        
        this.errorCode = findXmlElementText(xmlMsg, "Code");
        this.errorMessage = findXmlElementText(xmlMsg, "Message");
        this.errorRequestId = findXmlElementText(xmlMsg, "RequestId");
        this.errorHostId = findXmlElementText(xmlMsg, "HostId");
    }
    
    /**获取异常的描述信息
     * 
     * @return 异常的描述信息
     */
    public String getXmlMessage()
    {
        return xmlMessage;
    }
    
    /**设置异常的描述信息
     * 
     * @param xmlMessage 异常的描述信息
     */
    public void setXmlMessage(String xmlMessage)
    {
        this.xmlMessage = xmlMessage;
    }
    
    /**获取错误码
     * 
     * @return  异常的错误码
     */
    public String getErrorCode()
    {
        return errorCode;
    }
    
    /**设置错误码
     * 
     * @param errorCode 异常的错误码
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    
    /**获取错误描述
     * 
     * @return  异常的错误描述
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    /**设置错误描述
     * 
     * @param errorMessage 异常的错误描述
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    /**获取错误对应的请求ID
     * 
     * @return  异常的错误描述
     */
    public String getErrorRequestId()
    {
        return errorRequestId;
    }
    
    /**设置错误对应的请求ID
     * 
     * @param  errorRequestId 请求ID
     */
    public void setErrorRequestId(String errorRequestId)
    {
        this.errorRequestId = errorRequestId;
    }
    
    /**获取错误对应的主机ID
     * 
     * @return  主机ID
     */
    public String getErrorHostId()
    {
        return errorHostId;
    }
    
    /**设置错误对应的主机ID
     * 
     * @param  errorHostId 主机ID
     */
    public void setErrorHostId(String errorHostId)
    {
        this.errorHostId = errorHostId;
    }
    
    /**获取响应头
     * 
     * @return 响应头
     */
    public Map<String, String> getResponseHeaders()
    {
        return responseHeaders;
    }
    
    /**设置响应头
     * 
     * @param responseHeaders 响应头
     */
    public void setResponseHeaders(Map<String, String> responseHeaders)
    {
        this.responseHeaders = responseHeaders;
    }
    
    /**获取响应码
     * 
     * @return 响应码
     */
    public int getResponseCode()
    {
        return responseCode;
    }
    
    /**设置响应码
     * 
     * @param responseCode 响应码
     */
    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }
    
    /**获取响应描述
     * 
     * @return 响应描述
     */
    public String getResponseStatus()
    {
        return responseStatus;
    }
    
    /**设置响应描述
     * 
     * @param responseStatus 响应描述
     */
    public void setResponseStatus(String responseStatus)
    {
        this.responseStatus = responseStatus;
    }
    
    /**获取响应时间
     * 
     * @return 响应时间
     */
    public String getResponseDate()
    {
        return responseDate;
    }
    
    /**设置响应时间
     * 
     * @param responseDate 响应时间
     */
    public void setResponseDate(String responseDate)
    {
        this.responseDate = responseDate;
    }
    
    /**获取请求版本信息
     * 
     * @return 请求版本信息
     */
    public String getRequestVerb()
    {
        return requestVerb;
    }
    
    /**设置请求版本信息
     * 
     * @param requestVerb 请求版本信息
     */
    public void setRequestVerb(String requestVerb)
    {
        this.requestVerb = requestVerb;
    }
    
    /**获取请求路径
     * 
     * @return 请求路径
     */
    public String getRequestPath()
    {
        return requestPath;
    }
    
    /**设置请求路径
     * 
     * @param requestPath 路径
     */
    public void setRequestPath(String requestPath)
    {
        this.requestPath = requestPath;
    }
    
    /**获取请求主机
     * 
     * @return 请求主机
     */
    public String getRequestHost()
    {
        return requestHost;
    }
    
    /**设置请求主机
     * 
     * @param requestHost 请求主机
     */
    public void setRequestHost(String requestHost)
    {
        this.requestHost = requestHost;
    }
    
}
