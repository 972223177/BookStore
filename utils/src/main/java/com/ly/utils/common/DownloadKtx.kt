package com.ly.utils.common

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * 下载
 * @param retryTimes 中途下载失败重试次数
 * @param progressCallback 进度跳回调
 * @return 是否下载成功
 */
@Suppress("unused")
suspend fun simpleDownload(
    parentPath: String,
    fileName: String,
    url: String,
    retryTimes: Int = 5,
    headers: Map<String, String> = mapOf(),
    progressCallback: ((Double) -> Unit)? = null
): Boolean = suspendCancellableCoroutine {
    val task = DownloadTask.Builder(url, parentPath, fileName)
        .setMinIntervalMillisCallbackProcess(30)
        .setPassIfAlreadyCompleted(false)
        .apply {
            headers.forEach { header ->
                addHeader(header.key,header.value)
            }
        }
        .build()
    it.invokeOnCancellation {
        task.cancel()
    }
    task.enqueue(object : DownloadListener4WithSpeed() {
        private var current = 0
        private var totalLength = 0L
        override fun taskStart(task: DownloadTask) {

        }

        override fun connectStart(
            task: DownloadTask,
            blockIndex: Int,
            requestHeaderFields: MutableMap<String, MutableList<String>>
        ) {

        }

        override fun connectEnd(
            task: DownloadTask,
            blockIndex: Int,
            responseCode: Int,
            responseHeaderFields: MutableMap<String, MutableList<String>>
        ) {

        }

        override fun taskEnd(
            task: DownloadTask,
            cause: EndCause,
            realCause: Exception?,
            taskSpeed: SpeedCalculator
        ) {
            if (cause == EndCause.COMPLETED) {
                progressCallback?.invoke(1.0)
                it.resume(true)
            } else {
                if (current <= retryTimes && realCause != null) {
                    task.enqueue(this)
                    current += 1
                } else {
                    //下载被取消或者下载失败
                    it.resume(false)
                }
            }
        }

        override fun infoReady(
            task: DownloadTask,
            info: BreakpointInfo,
            fromBreakpoint: Boolean,
            model: Listener4SpeedAssistExtend.Listener4SpeedModel
        ) {
            totalLength = info.totalLength
        }

        override fun progressBlock(
            task: DownloadTask,
            blockIndex: Int,
            currentBlockOffset: Long,
            blockSpeed: SpeedCalculator
        ) {

        }

        override fun progress(
            task: DownloadTask,
            currentOffset: Long,
            taskSpeed: SpeedCalculator
        ) {
            progressCallback?.let {
                val progress = (currentOffset.toDouble()) / totalLength
                it(progress)
            }

        }

        override fun blockEnd(
            task: DownloadTask,
            blockIndex: Int,
            info: BlockInfo?,
            blockSpeed: SpeedCalculator
        ) {

        }

    })
}