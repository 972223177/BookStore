package com.liulishuo.okdownload.core.file

import android.os.SystemClock
import android.util.Log
import android.util.SparseArray
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.breakpoint.DownloadStore
import java.io.IOException

class FixMultiOutputStream (
    task: DownloadTask,
    private val info: BreakpointInfo,
    private val store: DownloadStore,
    syncRunnable: Runnable?
) : MultiPointOutputStream(
    task, info, store, syncRunnable
) {

    // close 方法要保证 outputStreamMap 和 noSyncLengthMap 的元素个数相等，只修改这里没有作用
    @Synchronized
    @Throws(IOException::class)
    public override fun close(blockIndex: Int) {
        val outputStream = outputStreamMap[blockIndex]
        if (outputStream != null) {
            outputStream.close()
            // outputStreamMap.remove(blockIndex);
            synchronized(noSyncLengthMap) {

                // make sure the length of noSyncLengthMap is equal to outputStreamMap
                outputStreamMap.remove(blockIndex)
                noSyncLengthMap.remove(blockIndex)
            }
//            d(TAG, "OutputStream close task[" + task.id + "] block[" + blockIndex + "]")
        }
    }

    // flushProcess 增加 catch 块
    @Throws(IOException::class)
    public override fun flushProcess() {
        val success: Boolean
        val size: Int
        synchronized(noSyncLengthMap) {
            // make sure the length of noSyncLengthMap is equal to outputStreamMap
            size = noSyncLengthMap.size()
        }
        val increaseLengthMap = SparseArray<Long>(size)
        success = try {
            for (i in 0 until size) {
                val blockIndex = outputStreamMap.keyAt(i)
                // because we get no sync length value before flush and sync,
                // so the length only possible less than or equal to the real persist
                // length.
                val noSyncLength = noSyncLengthMap[blockIndex].get()
                if (noSyncLength > 0) {
                    increaseLengthMap.put(blockIndex, noSyncLength)
                    val outputStream = outputStreamMap[blockIndex]
                    outputStream.flushAndSync()
                }
            }
            true
        } catch (ex: IOException) {
            Log.w(TAG, "OutputStream flush and sync data to filesystem failed $ex")
            false
        } catch (ex: Exception) { // 增加这部分，catch住其他类型exception
            Log.w(TAG, "OutputStream flush and sync data to filesystem failed $ex")
            false
        }
        if (success) {
            val increaseLengthSize = increaseLengthMap.size()
            var allIncreaseLength: Long = 0
            for (i in 0 until increaseLengthSize) {
                val blockIndex = increaseLengthMap.keyAt(i)
                val noSyncLength = increaseLengthMap.valueAt(i)
                store.onSyncToFilesystemSuccess(info, blockIndex, noSyncLength)
                allIncreaseLength += noSyncLength
                noSyncLengthMap[blockIndex].addAndGet(-noSyncLength)
//                Log.d(
//                    TAG, "OutputStream sync success (" + task.id + ") "
//                            + "block(" + blockIndex + ") " + " syncLength(" + noSyncLength + ")"
//                            + " currentOffset(" + info.getBlock(blockIndex).currentOffset
//                            + ")"
//                )
            }
            allNoSyncLength.addAndGet(-allIncreaseLength)
            lastSyncTimestamp.set(SystemClock.uptimeMillis())
        }
    }

    companion object {
        private const val TAG = "FixeMultiOutputStream"
    }
}