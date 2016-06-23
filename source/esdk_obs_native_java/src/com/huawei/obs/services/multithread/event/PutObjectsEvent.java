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

package com.huawei.obs.services.multithread.event;
//package com.huawei.obs.services.multithread.event;
//
//import com.huawei.obs.services.model.S3Object;
//import com.huawei.obs.services.multithread.ThreadWatcher;
///**
// * 上传对象的事件
// */
//public class PutObjectsEvent extends ServiceEvent
//{
//    private S3Object[] objects = null;
//
//    private PutObjectsEvent(int eventCode, Object uniqueOperationId)
//    {
//        super(eventCode, uniqueOperationId);
//    }
//
//    public static PutObjectsEvent newErrorEvent(Throwable t, Object uniqueOperationId)
//    {
//        PutObjectsEvent event = new PutObjectsEvent(EVENT_ERROR, uniqueOperationId);
//        event.setErrorCause(t);
//        return event;
//    }
//
//    public static PutObjectsEvent newStartedEvent(ThreadWatcher threadWatcher, Object uniqueOperationId)
//    {
//        PutObjectsEvent event = new PutObjectsEvent(EVENT_STARTED, uniqueOperationId);
//        event.setThreadWatcher(threadWatcher);
//        return event;
//    }
//
//    public static PutObjectsEvent newInProgressEvent(ThreadWatcher threadWatcher,
//        S3Object[] completedObjects, Object uniqueOperationId)
//    {
//        PutObjectsEvent event = new PutObjectsEvent(EVENT_IN_PROGRESS, uniqueOperationId);
//        event.setThreadWatcher(threadWatcher);
//        event.setObjects(completedObjects);
//        return event;
//    }
//    public static PutObjectsEvent newCompletedEvent(Object uniqueOperationId)
//    {
//        PutObjectsEvent event = new PutObjectsEvent(EVENT_COMPLETED, uniqueOperationId);
//        return event;
//    }
//
//    public static PutObjectsEvent newCancelledEvent(S3Object[] s3Objects, Object uniqueOperationId)
//    {
//        PutObjectsEvent event = new PutObjectsEvent(EVENT_CANCELLED, uniqueOperationId);
//        event.setObjects(s3Objects);
//        return event;
//    }
//
//    public static PutObjectsEvent newIgnoredErrorsEvent(ThreadWatcher threadWatcher,
//        Throwable[] ignoredErrors, Object uniqueOperationId)
//    {
//        PutObjectsEvent event = new PutObjectsEvent(EVENT_IGNORED_ERRORS, uniqueOperationId);
//        event.setIgnoredErrors(ignoredErrors);
//        return event;
//    }
//    
//    private void setObjects(S3Object[] objects)
//    {
//        this.objects = objects;
//    }
//
//    public S3Object[] getCreatedObjects() throws IllegalStateException
//    {
//        if (getEventCode() != EVENT_IN_PROGRESS)
//        {
//            throw new IllegalStateException("Created Objects are only available from EVENT_IN_PROGRESS events");
//        }
//        return objects;
//    }
//
//    public S3Object[] getCancelledObjects() throws IllegalStateException
//    {
//        if (getEventCode() != EVENT_CANCELLED)
//        {
//            throw new IllegalStateException("Cancelled Objects are  only available from EVENT_CANCELLED events");
//        }
//        return objects;
//    }
//}
