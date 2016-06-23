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
//
//import com.huawei.obs.services.multithread.event.GetObjectsEvent;
//import com.huawei.obs.services.multithread.event.PutObjectsEvent;
//import com.huawei.obs.services.multithread.event.ServiceEvent;
//
///**
// * 监听MosThreadedStorageService服务的适配器<br/>
// */
//public class MosStorageServiceEventAdaptor implements MosStorageServiceEventListener
//{
//    // 记录监听到的异常
//    private final Throwable t[] = new Throwable[1];
//    
//    /**
//     * 监听获得对象内容事件
//     * @param getObjectsEvent 获取对象服务过程中的事件
//     */
//    @Override
//    public void event(GetObjectsEvent getObjectsEvent)
//    {
//        storeThrowable(getObjectsEvent);
//    }
//    
//    /**
//     * 监听上传对象的事件
//     * @param putObjectsEvent 下载对象过程中的事件
//     */
//    @Override
//    public void event(PutObjectsEvent putObjectsEvent)
//    {
//        storeThrowable(putObjectsEvent);
//    }
//
//    protected void storeThrowable(ServiceEvent event)
//    {
//        if (t[0] == null && event.getEventCode() == ServiceEvent.EVENT_ERROR)
//        {
//            t[0] = event.getErrorCause();
//        }
//    }
//
//    public boolean wasErrorThrown()
//    {
//        return t[0] != null;
//    }
//
//    public Throwable getErrorThrown()
//    {
//        return t[0];
//    }
//
//    public void throwErrorIfPresent() throws Exception
//    {
//        if (this.wasErrorThrown())
//        {
//            Throwable thrown = this.getErrorThrown();
//            if (thrown instanceof Exception)
//            {
//                throw (Exception) thrown;
//            }
//            else
//            {
//                throw new Exception(thrown);
//            }
//        }
//    }
//}
