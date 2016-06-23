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

import java.io.File;


/**描述多段上传的请求
 */
public class UploadPartRequest
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    private int partNumber;
    
    private Long partSize;
    
    private long offset = 0;
    
    /**
     * 多段上传任务中某一分段偏移的大小，单位字节
     * @return 多段上传任务中某一分段偏移的大小，单位字节
     */
    public long getOffset() {
		return offset;
	}

    /**
     * 设置多段上传任务中某一分段偏移的大小，单位字节
     * @param offset 某一分段偏移的大小，单位字节
     */
	public void setOffset(long offset) {
		this.offset = offset;
	}

	private File file;
    
//    private InputStream inputStream;
    
    /**
     * 上传段的构造方法
     * @param bucketName 所属的桶名
     * @param objectKey 所属对象名称
     * @param fileName 上传的文件
     */
    public UploadPartRequest(String bucketName,String objectKey,String fileName)
    {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.file = new File(fileName);
    }
    
    /**
     * 上传段的构造方法
     * @param bucketName 所属的桶名
     * @param objectKey 所属对象名称
     * @param file 上传的文件
     */
    public UploadPartRequest(String bucketName,String objectKey,File file)
    {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.file = file;
    }
    
    /**
     * 获取多段上传任务上传分段的编号
     * @return 返回多段上传任务上传分段的编号
     */
    public int getPartNumber()
    {
        return partNumber;
    }
    
    /**
     * 设置上传任务上传分段的编号
     * @param partNumber 分段的编号
     */
    public void setPartNumber(int partNumber)
    {
        this.partNumber = partNumber;
    }
    
    /**获取多段上传任务的标识
     * 
     * @return 返回多段上传任务的标识
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /**设置上传分段所属的上传任务，uploadId为这个标识
     * 
     * @param uploadId 上传任务标识
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取多段上传任务所属的桶
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
//    /**多段上传任务上传分段的数据流
//     * 
//     * @param input 数据流
//     */
//    public void setInputStream(InputStream input)
//    {
//        this.inputStream = input;
//    }
//    
//    /**多段上传任务上传分段的数据流
//     * 
//     * @return 返回数据流
//     */
//    public InputStream getInputStream()
//    {
//        return inputStream;
//    }
    
    /**设置多段上传任务中某一分段的大小，单位字节
     * 
     * @param partSize 分段字节大小
     */
    public void setPartSize(Long partSize)
    {
        this.partSize = partSize;
    }
    
    /**获取多段上传任务中某一分段的大小，单位字节
     * 
     * @return 分段的大小
     */
    public Long getPartSize()
    {
        return partSize;
    }

    /**
     * 返回上传的文件
     * @return 上传的文件
     */
    public File getFile()
    {
        return file;
    }

    /**
     * 设置上传的文件
     * @param file 上传的文件
     */
    public void setFile(File file)
    {
        this.file = file;
    }

}
