package com.liulishuo.okdownload.core.file

import android.content.Context
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.breakpoint.DownloadStore

/**
 * 解决下载时可能出现卡死的情况
 */
@Suppress("unused")
class FixProcessFileStrategy : ProcessFileStrategy() {
    override fun createProcessStream(
        task: DownloadTask,
        info: BreakpointInfo,
        store: DownloadStore
    ): MultiPointOutputStream {
        return FixMultiOutputStream(task, info, store, null)
    }

    companion object {

        /**
         * 最好在application的onCreate调用
         */
        fun wrapper(context: Context) {
            try {
                OkDownload.setSingletonInstance(
                    OkDownload.Builder(context).processFileStrategy(FixProcessFileStrategy())
                        .build()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}