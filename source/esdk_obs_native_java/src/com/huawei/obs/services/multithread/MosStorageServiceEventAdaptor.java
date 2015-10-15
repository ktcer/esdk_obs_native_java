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
