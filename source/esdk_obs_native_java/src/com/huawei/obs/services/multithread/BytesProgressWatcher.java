package com.huawei.obs.services.multithread;
//package com.huawei.obs.services.multithread;
//
//import java.util.Map;
//import java.util.TreeMap;
//
//public class BytesProgressWatcher
//{
//    public static final int SECONDS_OF_HISTORY = 5;
//
//    private boolean isStarted = false;
//
//    private long bytesToTransfer = 0;
//
//    private long startTimeAllTransfersMS = -1;
//
//    private long totalBytesInAllTransfers = 0;
//
//    private long startTimeCurrentTransferMS = -1;
//
//    private long totalBytesInCurrentTransfer = 0;
//
//    private long endTimeCurrentTransferMS = -1;
//
//    private final Map<Long, Long> historyOfBytesBySecond = new TreeMap<Long, Long>();
//
//    private long earliestHistorySecond = Long.MAX_VALUE;
//
//    private Object synchronizeObject = new Object();
//
//    /**
//     * 创建一个观察器
//     * @param bytesToTransfer 传输的总长度
//     */
//    public BytesProgressWatcher(long bytesToTransfer)
//    {
//        this.bytesToTransfer = bytesToTransfer;
//    }
//
//    /**
//     * @return 需要传输的字节
//     */
//    public long getBytesToTransfer()
//    {
//        return bytesToTransfer;
//    }
//
//    public void setBytesToTransfer(long bytesToTransfer)
//    {
//        if (isStarted())
//        {
//            throw new IllegalStateException("Watcher already started.");
//        }
//        this.bytesToTransfer = bytesToTransfer;
//    }
//
//    /**
//     * 重置字节数量和计时;当传输开始或传输重新开始时，这个方法会自动执行
//     */
//    public void resetWatcher()
//    {
//        synchronized (synchronizeObject)
//        {
//            startTimeCurrentTransferMS = System.currentTimeMillis();
//            if (startTimeAllTransfersMS == -1)
//            {
//                startTimeAllTransfersMS = startTimeCurrentTransferMS;
//            }
//            endTimeCurrentTransferMS = -1;
//            totalBytesInCurrentTransfer = 0;
//            isStarted = true;
//        }
//    }
//
//    public void updateBytesTransferred(long byteCount)
//    {
//        if (!isStarted)
//        {
//            resetWatcher();
//        }
//
//        synchronized (synchronizeObject)
//        {
//            totalBytesInCurrentTransfer += byteCount;
//            totalBytesInAllTransfers += byteCount;
//
//            if (totalBytesInCurrentTransfer >= bytesToTransfer)
//            {
//                endTimeCurrentTransferMS = System.currentTimeMillis();
//            }
//
//            Long currentSecond = new Long(System.currentTimeMillis() / 1000);
//            Long bytesInSecond = historyOfBytesBySecond.get(currentSecond);
//            if (bytesInSecond != null)
//            {
//                historyOfBytesBySecond.put(currentSecond, new Long(byteCount + bytesInSecond.longValue()));
//            }
//            else
//            {
//                historyOfBytesBySecond.put(currentSecond, new Long(byteCount));
//            }
//
//            if (currentSecond.longValue() < earliestHistorySecond)
//            {
//                earliestHistorySecond = currentSecond.longValue();
//            }
//
//            long removeHistoryBeforeSecond = currentSecond.longValue() - SECONDS_OF_HISTORY;
//            for (long sec = earliestHistorySecond; sec < removeHistoryBeforeSecond; sec++)
//            {
//                Long pSec = new Long(sec);
//                Long bytes = historyOfBytesBySecond.remove(pSec);
//                removedFromHistory(pSec, bytes);
//            }
//            earliestHistorySecond = removeHistoryBeforeSecond;
//        }
//    }
//
//    protected void removedFromHistory(Long pSec, Long pBytes)
//    {
//    }
//
//    protected void clearHistory()
//    {
//        synchronized (synchronizeObject)
//        {
//            Long currentSecond = new Long(System.currentTimeMillis() / 1000);
//            for (long sec = earliestHistorySecond; sec <= currentSecond; sec++)
//            {
//                Long pSec = new Long(sec);
//                Long bytes = historyOfBytesBySecond.remove(pSec);
//                removedFromHistory(pSec, bytes);
//            }
//        }
//    }
//
//    public long getBytesTransferred()
//    {
//        return totalBytesInCurrentTransfer;
//    }
//
//    public long getBytesRemaining()
//    {
//        return bytesToTransfer - totalBytesInCurrentTransfer;
//    }
//
//    public long getRemainingTime()
//    {
//        BytesProgressWatcher[] progressWatchers = new BytesProgressWatcher[1];
//        progressWatchers[0] = this;
//
//        long bytesRemaining = bytesToTransfer - totalBytesInCurrentTransfer;
//        double remainingSecs = bytesRemaining / calculateOverallBytesPerSecond(progressWatchers);
//        return Math.round(remainingSecs);
//    }
//
//    public double getRecentByteRatePerSecond()
//    {
//        if (!isStarted)
//        {
//            return 0;
//        }
//
//        long currentSecond = System.currentTimeMillis() / 1000;
//        long startSecond = 1 + (currentSecond - SECONDS_OF_HISTORY);
//        long endSecond = (endTimeCurrentTransferMS != -1 ? endTimeCurrentTransferMS / 1000 : currentSecond);
//
//        if (currentSecond - SECONDS_OF_HISTORY > endSecond)
//        {
//            historyOfBytesBySecond.clear();
//            return 0;
//        }
//
//        long sumOfBytes = 0;
//        long numberOfSecondsInHistory = 0;
//        for (long sec = startSecond; sec <= endSecond; sec++)
//        {
//            numberOfSecondsInHistory++;
//            Long bytesInSecond = historyOfBytesBySecond.get(new Long(sec));
//            if (bytesInSecond != null)
//            {
//                sumOfBytes += bytesInSecond.longValue();
//            }
//        }
//        return (numberOfSecondsInHistory == 0 ? 0 : (double) sumOfBytes / numberOfSecondsInHistory);
//    }
//
//    protected long getElapsedTimeMS()
//    {
//        if (!isStarted)
//        {
//            return 0;
//        }
//        if (endTimeCurrentTransferMS != -1)
//        {
//            return endTimeCurrentTransferMS - startTimeCurrentTransferMS;
//        }
//        else
//        {
//            return System.currentTimeMillis() - startTimeCurrentTransferMS;
//        }
//    }
//
//    protected long getTotalBytesInAllTransfers()
//    {
//        return totalBytesInAllTransfers;
//    }
//
//    protected boolean isStarted()
//    {
//        return isStarted;
//    }
//
//    protected long getHistoricStartTimeMS()
//    {
//        return startTimeAllTransfersMS;
//    }
//
//    public static long sumBytesToTransfer(BytesProgressWatcher[] progressWatchers)
//    {
//        long sumOfBytes = 0;
//        for (int i = 0; i < progressWatchers.length; i++)
//        {
//            sumOfBytes += progressWatchers[i].getBytesToTransfer();
//        }
//        return sumOfBytes;
//    }
//
//    public static long sumBytesTransferred(BytesProgressWatcher[] progressWatchers)
//    {
//        long sumOfBytes = 0;
//        for (int i = 0; i < progressWatchers.length; i++)
//        {
//            sumOfBytes += progressWatchers[i].getBytesTransferred();
//        }
//        return sumOfBytes;
//    }
//
//    public static long calculateRemainingTime(BytesProgressWatcher[] progressWatchers)
//    {
//        long bytesRemaining = sumBytesToTransfer(progressWatchers) - sumBytesTransferred(progressWatchers);
//        double bytesPerSecond = calculateOverallBytesPerSecond(progressWatchers);
//
//        if (Math.abs(bytesPerSecond) < 0.001d)
//        {
//            return 0;
//        }
//
//        double remainingSecs = bytesRemaining / bytesPerSecond;
//        return Math.round(remainingSecs);
//    }
//
//    public static double calculateOverallBytesPerSecond(BytesProgressWatcher[] progressWatchers)
//    {
//        long initialStartTime = Long.MAX_VALUE; // The oldest start time of any
//                                                // monitor.
//
//        long bytesTotal = 0;
//        for (int i = 0; i < progressWatchers.length; i++)
//        {
//            if (!progressWatchers[i].isStarted())
//            {
//                continue;
//            }
//
//            bytesTotal += progressWatchers[i].getTotalBytesInAllTransfers();
//
//            if (progressWatchers[i].getHistoricStartTimeMS() < initialStartTime)
//            {
//                initialStartTime = progressWatchers[i].getHistoricStartTimeMS();
//            }
//        }
//
//        long elapsedTimeSecs = (System.currentTimeMillis() - initialStartTime) / 1000;
//
//        double bytesPerSecondOverall = (elapsedTimeSecs == 0 ? bytesTotal : (double) bytesTotal / elapsedTimeSecs);
//
//        return bytesPerSecondOverall;
//    }
//
//    public static long calculateRecentByteRatePerSecond(BytesProgressWatcher[] progressWatchers)
//    {
//        double sumOfRates = 0;
//        for (int i = 0; i < progressWatchers.length; i++)
//        {
//            if (progressWatchers[i].isStarted())
//            {
//                sumOfRates += progressWatchers[i].getRecentByteRatePerSecond();
//            }
//        }
//        return Math.round(sumOfRates);
//    }
//
//}
