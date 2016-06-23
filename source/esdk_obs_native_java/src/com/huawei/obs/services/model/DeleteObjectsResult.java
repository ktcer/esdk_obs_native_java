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

import java.util.List;

/**批量删除对象的返回结果
 */
public class DeleteObjectsResult
{
    private List<DeleteObjectResult> deletedObjectResults;

    private List<ErrorResult> errorResults;

    /**获取批量删除中删除成功的对象信息列表
     * 
     * @return 删除成功的对象信息列表 @see {@link DeleteObjectResult}
     */
    public List<DeleteObjectResult> getDeletedObjectResults()
    {
        return deletedObjectResults;
    }

    /**设置批量删除对象对应的信息列表
     * 
     * @param deletedObjectResults 删除成功的对象信息列表 @see {@link #deletedObjectResults}
     */
    public void setDeletedObjectResults(List<DeleteObjectResult> deletedObjectResults)
    {
        this.deletedObjectResults = deletedObjectResults;
    }

    /**获取批量删除中删除失败的对象信息列表
     * 
     * @return 删除失败的对象信息列表
     */
    public List<ErrorResult> getErrorResults()
    {
        return errorResults;
    }

    /**设置批量删除中删除失败的对象信息列表
     * 
     * @param errorResults 删除失败的对象信息列表
     */
    public void setErrorResults(List<ErrorResult> errorResults)
    {
        this.errorResults = errorResults;
    }
    
    /**内部类，描述删除对象操作的返回结果
     */
    public class DeleteObjectResult
    {
        private String objectKey;
        private String version;
        
        /**根据对象的信息构造一个对象删除的返回结果
         * 
         * @param objectKey 对象名称
         * @param version 对象版本
         */
        public DeleteObjectResult(String objectKey, String version)
        {
            this.objectKey = objectKey;
            this.version = version;
        }
        
        /**获取对象名称
         * 
         * @return 对象名称
         */
        public String getObjectKey()
        {
            return objectKey;
        }
        
        /**获取对象版本信息
         * 
         * @return 对象版本信息
         */
        public String getVersion()
        {
            return version;
        }
    }
    
    /**内部类，描述删除对象操作失败的返回结果
     */
    public class ErrorResult
    {
        private String objectKey;
        private String version;
        private String errorCode;
        private String message;
        
        /**创建一个对象删除的错误描述信息
         *
         * @param objectKey 对象名称
         * @param version 对象版本
         * @param errorCode 错误码
         * @param message 错误描述
         */
        public ErrorResult(String objectKey, String version, String errorCode, String message)
        {
            this.objectKey = objectKey;
            this.version = version;
            this.errorCode = errorCode;
            this.message = message;
        }
        
        /**获取删除对象的名称
         * 
         * @return 返回删除对象的名称
         */
        public String getObjectKey()
        {
            return objectKey;
        }
        
        /**获取删除对象的版本
         * 
         * @return 返回删除对象的版本
         */
        public String getVersion()
        {
            return version;
        }
        
        /**获取删除对象返回值的错误码
         * 
         * @return 返回删除对象返回值的错误码
         */
        public String getErrorCode()
        {
            return errorCode;
        }
        
        /**获取删除对象返回值的错误描述
         * 
         * @return 返回删除对象返回值的错误描述
         */
        public String getMessage()
        {
            return message;
        }
    }
}
