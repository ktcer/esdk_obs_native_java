package com.huawei.obs.services.multithread.event;
//package com.huawei.obs.services.multithread.event;
//
//import com.huawei.obs.services.multithread.ThreadWatcher;
//
///**
// * service事件基类<br/>
// * 每个事件都有事件编号，代表相应的处理状态。<br/>
// * <p>
// * 事件编号及其含义:
// * <ul>
// * <li>EVENT_STARTED:一个操作已经开始，还没执行</li>
// * <li>EVENT_IN_PROGRESS:一个操作已经被处理，进行中的事件以固定时间间隔执行</li>
// * <li>EVENT_COMPLETED:一个操作已经被执行完毕</li>
// * <li>EVENT_CANCELLED:一个操作已经开始执行，但是在完成之前又被取消</li>
// * <li>EVENT_ERROR:一个操作是被后会抛出一个异常，可以通过{@link #getErrorCause()}获得异常原因</li>
// * <li>EVENT_IGNORED_ERRORS:一个或多个操作失败了，可以通过{@link #getIgnoredErrors()}获得异常原因</li>
// * </ul>
// * </p>
// */
//public class ServiceEvent
//{
//    public static final int EVENT_ERROR = 0;
//
//    /**
//     * 一个操作开始执行，比如开始上传或开始下载一个对象
//     */
//    public static final int EVENT_STARTED = 1;
//
//    /**
//     * 一个操作完成，比如上传完一个对象或下载完一个对象
//     */
//    public static final int EVENT_COMPLETED = 2;
//
//    /**
//     * 一个操作正在执行中，比如正在上传或下载一个对象
//     */
//    public static final int EVENT_IN_PROGRESS = 3;
//
//    /**
//     * 一个操作被取消
//     */
//    public static final int EVENT_CANCELLED = 4;
//
//    /**
//     * 发生一个可被忽略的异常
//     */
//    public static final int EVENT_IGNORED_ERRORS = 5;
//
//    private int eventCode = 0;
//
//    private Object uniqueOperationId = null;
//
//    private Throwable t = null;
//
//    private ThreadWatcher threadWatcher = null;
//
//    private Throwable[] ignoredErrors = null;
//
//    protected ServiceEvent(int eventCode, Object uniqueOperationId)
//    {
//        this.eventCode = eventCode;
//        this.uniqueOperationId = uniqueOperationId;
//    }
//
//    protected void setThreadWatcher(ThreadWatcher threadWatcher)
//    {
//        this.threadWatcher = threadWatcher;
//    }
//
//    protected void setErrorCause(Throwable t)
//    {
//        this.t = t;
//    }
//
//    protected void setIgnoredErrors(Throwable[] ignoredErrors)
//    {
//        this.ignoredErrors = ignoredErrors;
//    }
//
//    public Object getUniqueOperationId()
//    {
//        return uniqueOperationId;
//    }
//
//    public int getEventCode()
//    {
//        return eventCode;
//    }
//
//    public Throwable getErrorCause() throws IllegalStateException
//    {
//        if (eventCode != EVENT_ERROR)
//        {
//            throw new IllegalStateException("Error Cause is only available from EVENT_ERROR events");
//        }
//        return t;
//    }
//
//    public Throwable[] getIgnoredErrors() throws IllegalStateException
//    {
//        if (eventCode != EVENT_IGNORED_ERRORS)
//        {
//            throw new IllegalStateException("Ignored errors are only available from EVENT_IGNORED_ERRORS events");
//        }
//        return ignoredErrors;
//    }
//
////    public ThreadWatcher getThreadWatcher() throws IllegalStateException
////    {
////        if (eventCode != EVENT_STARTED && eventCode != EVENT_IN_PROGRESS)
////        {
////            throw new IllegalStateException("Thread Watcher is only available from EVENT_STARTED "
////                + "or EVENT_IN_PROGRESS events");
////        }
////        return threadWatcher;
////    }
//
//    @Override
//    public String toString()
//    {
//        String eventText = eventCode == EVENT_ERROR ? "EVENT_ERROR" : eventCode == EVENT_STARTED ? "EVENT_STARTED"
//            : eventCode == EVENT_COMPLETED ? "EVENT_COMPLETED" : eventCode == EVENT_IN_PROGRESS ? "EVENT_IN_PROGRESS"
//                : eventCode == EVENT_CANCELLED ? "EVENT_CANCELLED"
//                    : eventCode == EVENT_IGNORED_ERRORS ? "EVENT_IGNORED_ERRORS" : "Unrecognised event status code: "
//                        + eventCode;
//
//        if (eventCode == EVENT_ERROR && getErrorCause() != null)
//        {
//            return eventText + " " + getErrorCause();
//        }
//        else
//        {
//            return eventText;
//        }
//    }
//
//}
