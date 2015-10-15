package com.huawei.obs.services.multithread.event;
//package com.huawei.obs.services.multithread.event;
//
//import com.huawei.obs.services.model.S3Object;
//import com.huawei.obs.services.multithread.ThreadWatcher;
//
///**
// * 获取对象事件
// */
//public class GetObjectsEvent extends ServiceEvent
//{
//    private S3Object[] objects = null;
//
//    private void setObjects(S3Object[] objects)
//    {
//        this.objects = objects;
//    }
//
//    private GetObjectsEvent(int eventCode, Object uniqueOperationId)
//    {
//        super(eventCode, uniqueOperationId);
//    }
//
//    public static GetObjectsEvent newErrorEvent(Throwable t, Object uniqueOperationId)
//    {
//        GetObjectsEvent event = new GetObjectsEvent(EVENT_ERROR, uniqueOperationId);
//        event.setErrorCause(t);
//        return event;
//    }
//    
//    public static GetObjectsEvent newStartedEvent(ThreadWatcher threadWatcher, Object uniqueOperationId)
//    {
//        GetObjectsEvent event = new GetObjectsEvent(EVENT_STARTED, uniqueOperationId);
//        event.setThreadWatcher(threadWatcher);
//        return event;
//    }
//
//    /**
//     * @param threadWatcher 
//     * @param uniqueOperationId
//     * @return 返回事件
//     */
//    public static GetObjectsEvent newInProgressEvent(ThreadWatcher threadWatcher,
//        S3Object[] objects, Object uniqueOperationId)
//    {
//        GetObjectsEvent event = new GetObjectsEvent(EVENT_IN_PROGRESS, uniqueOperationId);
//        event.setThreadWatcher(threadWatcher);
//        event.setObjects(objects);
//        return event;
//    }
//    public static GetObjectsEvent newCompletedEvent(Object uniqueOperationId)
//    {
//        GetObjectsEvent event = new GetObjectsEvent(EVENT_COMPLETED, uniqueOperationId);
//        return event;
//    }
//
//    public static GetObjectsEvent newCancelledEvent(S3Object[] incompletedObjects, Object uniqueOperationId)
//    {
//        GetObjectsEvent event = new GetObjectsEvent(EVENT_CANCELLED, uniqueOperationId);
//        event.setObjects(incompletedObjects);
//        return event;
//    }
//
//    public static GetObjectsEvent newIgnoredErrorsEvent(ThreadWatcher threadWatcher,
//        Throwable[] ignoredErrors, Object uniqueOperationId)
//    {
//        GetObjectsEvent event = new GetObjectsEvent(EVENT_IGNORED_ERRORS, uniqueOperationId);
//        event.setIgnoredErrors(ignoredErrors);
//        return event;
//    }
//    
//    public S3Object[] getCompletedObjects() throws IllegalStateException
//    {
//        if (getEventCode() != EVENT_IN_PROGRESS)
//        {
//            throw new IllegalStateException("Completed Objects are only available from EVENT_IN_PROGRESS events");
//        }
//        return objects;
//    }
//
//    public S3Object[] getCancelledObjects() throws IllegalStateException
//    {
//        if (getEventCode() != EVENT_CANCELLED)
//        {
//            throw new IllegalStateException("Completed Objects are only available from EVENT_IN_PROGRESS events");
//        }
//        return objects;
//    }
//}
