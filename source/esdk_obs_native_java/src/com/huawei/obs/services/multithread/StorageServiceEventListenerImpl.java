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

package com.huawei.obs.services.multithread;
//package com.huawei.obs.services.multithread;
//
//import org.jets3t.service.multi.StorageServiceEventListener;
//import org.jets3t.service.multi.event.CopyObjectsEvent;
//import org.jets3t.service.multi.event.CreateBucketsEvent;
//import org.jets3t.service.multi.event.CreateObjectsEvent;
//import org.jets3t.service.multi.event.DeleteObjectsEvent;
//import org.jets3t.service.multi.event.DownloadObjectsEvent;
//import org.jets3t.service.multi.event.GetObjectHeadsEvent;
//import org.jets3t.service.multi.event.GetObjectsEvent;
//import org.jets3t.service.multi.event.ListObjectsEvent;
//import org.jets3t.service.multi.event.LookupACLEvent;
//import org.jets3t.service.multi.event.UpdateACLEvent;
//
//import com.huawei.obs.services.Convert;
//import com.huawei.obs.services.multithread.event.PutObjectsEvent;
///**
// * MosThreadedStorageService服务的监听器<br/>
// * 监听MosThreadedStorageService服务中的事件，如:<br/>
// * 创建对象、获取对象内容、复制对象、删除对象等。
// */
//class StorageServiceEventListenerImpl implements StorageServiceEventListener
//{
//    private MosStorageServiceEventListener serviceListener = null;
//    
//    public StorageServiceEventListenerImpl(MosStorageServiceEventListener serviceListener)
//    {
//        this.serviceListener = serviceListener;
//    }
//    
//    @Override
//    public void event(ListObjectsEvent event)
//    {
//    }
//
//    @Override
//    public void event(CreateObjectsEvent event)
//    {
//        PutObjectsEvent putObjectsEvent = Convert.changeFromS3CreateObjectsEvent(event);
//        serviceListener.event(putObjectsEvent);
//    }
//
//    @Override
//    public void event(CopyObjectsEvent event)
//    {
//    }
//
//    @Override
//    public void event(CreateBucketsEvent event)
//    {
//    }
//
//    @Override
//    public void event(DeleteObjectsEvent event)
//    {
//    }
//
//    @Override
//    public void event(GetObjectsEvent event)
//    {
//        com.huawei.obs.services.multithread.event.GetObjectsEvent getObjectsEvent =
//        Convert.changeFromS3GetObjectsEvent(event);
//        serviceListener.event(getObjectsEvent);
//    }
//
//    @Override
//    public void event(GetObjectHeadsEvent event)
//    {
//    }
//
//    @Override
//    public void event(LookupACLEvent event)
//    {
//    }
//
//    @Override
//    public void event(UpdateACLEvent event)
//    {
//    }
//
//    @Override
//    public void event(DownloadObjectsEvent event)
//    {
//    }
//}
