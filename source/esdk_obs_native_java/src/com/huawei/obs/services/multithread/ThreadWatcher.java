package com.huawei.obs.services.multithread;
//package com.huawei.obs.services.multithread;
//
//public class ThreadWatcher
//{
//    private long completedThreads = 0;
//
//    private long threadCount = 0;
//
//    private CancelEventTrigger cancelEventListener = null;
//
//    private BytesProgressWatcher[] progressWatchers = null;
//
//    public ThreadWatcher(BytesProgressWatcher[] progressWatchers)
//    {
//        this.progressWatchers = progressWatchers;
//        this.threadCount = this.progressWatchers.length;
//    }
//
//    public ThreadWatcher(long threadCount)
//    {
//        this.threadCount = threadCount;
//    }
//
//    public void updateThreadsCompletedCount(long completedThreads)
//    {
//        updateThreadsCompletedCount(completedThreads, null);
//    }
//
//    public void updateThreadsCompletedCount(long completedThreads, CancelEventTrigger cancelEventListener)
//    {
//        this.completedThreads = completedThreads;
//        this.cancelEventListener = cancelEventListener;
//    }
//
//    public long getCompletedThreads()
//    {
//        return completedThreads;
//    }
//
//    public long getThreadCount()
//    {
//        return threadCount;
//    }
//
//    public boolean isBytesTransferredInfoAvailable()
//    {
//        return (progressWatchers != null);
//    }
//
//    public long getBytesTotal() throws IllegalStateException
//    {
//        if (!isBytesTransferredInfoAvailable())
//        {
//            throw new IllegalStateException("Bytes Transferred Info is not available in this object");
//        }
//        return BytesProgressWatcher.sumBytesToTransfer(progressWatchers);
//    }
//
//    public long getBytesTransferred()
//    {
//        if (!isBytesTransferredInfoAvailable())
//        {
//            throw new IllegalStateException("Bytes Transferred Info is not available in this object");
//        }
//        return BytesProgressWatcher.sumBytesTransferred(progressWatchers);
//    }
//
//    public long getBytesPerSecond()
//    {
//        return BytesProgressWatcher.calculateRecentByteRatePerSecond(progressWatchers);
//    }
//
//    public boolean isTimeRemainingAvailable()
//    {
//        return (progressWatchers != null);
//    }
//
//    public long getTimeRemaining()
//    {
//        if (!isTimeRemainingAvailable())
//        {
//            throw new IllegalStateException("Time remaining estimate is not available in this object");
//        }
//        return BytesProgressWatcher.calculateRemainingTime(progressWatchers);
//    }
//
//    public boolean isCancelTaskSupported()
//    {
//        return cancelEventListener != null;
//    }
//
//    public void cancelTask()
//    {
//        if (isCancelTaskSupported())
//        {
//            cancelEventListener.cancelTask(this);
//        }
//    }
//
//    public CancelEventTrigger getCancelEventListener()
//    {
//        return cancelEventListener;
//    }
//}
