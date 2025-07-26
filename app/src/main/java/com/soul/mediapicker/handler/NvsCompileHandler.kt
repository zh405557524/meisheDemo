package com.fuyao.tuiwen.handler

import android.os.Environment
import android.os.StatFs
import com.meicam.sdk.NvsStreamingContext
import com.meicam.sdk.NvsTimeline
import com.soul.lib.utils.LogUtil
import com.soul.mediapicker.DecodeType
import com.soul.mediapicker.utils.Util
import java.io.File
import java.util.Hashtable
import java.util.UUID

/**
 * NVS-SDK的导出逻辑
 */
class NvsCompileHandler {
    val TAG= "NvsCompileHandler"
    interface NvsCompileCallback {
        fun onCompileBegin(timeline: NvsTimeline?, operateRate: Boolean, flag: Int, outputPath: String)
        fun onCompileProgress(timeline: NvsTimeline?, progress: Int)
        fun onCompileCancel()
        fun onCompileFinished(timeline: NvsTimeline?, outputPath: String)
        fun onCompileFailed(timeline: NvsTimeline?, errMsg: String)
        // 上报硬件报错，接入方不需要在此处主动调用停止导出等方法，仅作上报使用
        fun onCompileHardwareError(code: Int, reason: String)
        // 结束信息回调，仅作上报使用
        fun onCompileCompleteInfo(
            isHardwareError: Boolean,
            errorType: Int,
            errorMsg: String?,
            flag: Int
        )
    }

    private var hardwareFail = false
    private var isStop = false
    private var reportedWhenUsingSoftware = false

    fun stop() {
        isStop = true
    }

    fun compile(
        streamingContext: NvsStreamingContext,
        timeline: NvsTimeline,
        outputDir: String,
        timelineHeight: Int,
        timelineDuration: Long,
        decodeType: Int,
        callback: NvsCompileCallback
    ) :String{
        val outputPath = outputDir + File.separator + Util.md5(UUID.randomUUID().toString()) + ".mp4"
        streamingContext.customCompileVideoHeight = timelineHeight
        streamingContext.isDefaultCaptionFade = false
        streamingContext.setCompileCallback3 { _, p1, p2, p3, p4 ->
            callback.onCompileCompleteInfo(
                p1,
                p2,
                p3,
                p4
            )
        }
        streamingContext.setCompileCallback(object : NvsStreamingContext.CompileCallback {
            override fun onCompileProgress(p0: NvsTimeline?, p1: Int) {
                callback.onCompileProgress(p0, p1)
            }

            override fun onCompileFinished(p0: NvsTimeline?) {
                if (hardwareFail) {
                    callback.onCompileFailed(timeline, "合成失败，请重试")
                    return
                }

                if (isStop) {
                    callback.onCompileCancel()
                    return
                }

                callback.onCompileProgress(timeline, 100)
                callback.onCompileFinished(timeline, outputPath)
            }

            override fun onCompileFailed(p0: NvsTimeline?) {
                callback.onCompileFailed(p0, "合成失败")
            }
        })

        streamingContext.setHardwareErrorCallback { i, s ->
            // 当使用硬件编码时，此处报错应处理，停止当前次的compile操作
            // 当使用了STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER标记关闭硬件编码时，不处理此处报错
            if (decodeType == DecodeType.hardware) {
                callback.onCompileHardwareError(i, s)
                LogUtil.i(TAG,"硬件编码失败 s:${s}")
                hardwareFail = true
                streamingContext.stop(NvsStreamingContext.STREAMING_ENGINE_INTERRUPT_STOP)
            } else {
                if (!reportedWhenUsingSoftware) {
                    callback.onCompileHardwareError(i, s)
                    LogUtil.i(TAG,"编码失败 s:${s}")
                    reportedWhenUsingSoftware = true
                }
            }
        }

        val flag = when (decodeType) {
            DecodeType.software -> NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_IGNORE_TIMELINE_VIDEO_SIZE or NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER
            else -> NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_IGNORE_TIMELINE_VIDEO_SIZE
        }
        streamingContext.compileConfigurations = null
        val compileConfigurations = Hashtable<String, Any>()
        compileConfigurations[NvsStreamingContext.COMPILE_BITRATE] = 15 * 1000000
        compileConfigurations[NvsStreamingContext.COMPILE_USE_OPERATING_RATE] = true
        streamingContext.compileConfigurations = compileConfigurations
        hardwareFail = false
        streamingContext.stop()

        callback.onCompileBegin(timeline, true, flag, outputPath)

        // 如果是软编，将所有clip设置成软件解码
        if (decodeType == DecodeType.software) {
            val videoTrack = timeline.getVideoTrackByIndex(0)
            for (index in 0 until  videoTrack.clipCount) {
                val clip = videoTrack.getClipByIndex(index)
                clip.setSoftWareDecoding(true)
            }
        }

        streamingContext.compileTimeline(
            timeline,
            0,
            timelineDuration,
            outputPath,
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_CUSTOM,
            NvsStreamingContext.COMPILE_BITRATE_GRADE_HIGH,
            flag
        )
        return outputPath
    }




    fun compileVideo(
        streamingContext: NvsStreamingContext,
        timeline: NvsTimeline,
        outputDir: String,
        timelineHeight: Int,
        timelineDuration: Long,
        decodeType: Int,
        onlyVideo : Boolean,
        callback: NvsCompileCallback
    ) {
        val outputPath = outputDir + File.separator + Util.md5(UUID.randomUUID().toString()) + ".mp4"
        streamingContext.customCompileVideoHeight = timelineHeight
        streamingContext.isDefaultCaptionFade = false
        streamingContext.setCompileCallback3 { _, p1, p2, p3, p4 ->
            callback.onCompileCompleteInfo(
                p1,
                p2,
                p3,
                p4
            )
        }
        streamingContext.setCompileCallback(object : NvsStreamingContext.CompileCallback {
            override fun onCompileProgress(p0: NvsTimeline?, p1: Int) {
                callback.onCompileProgress(p0, p1)
            }

            override fun onCompileFinished(p0: NvsTimeline?) {
                if (hardwareFail) {
                    callback.onCompileFailed(timeline, "合成失败")
                    return
                }

                if (isStop) {
                    callback.onCompileCancel()
                    return
                }

                callback.onCompileProgress(timeline, 100)
                callback.onCompileFinished(timeline, outputPath)
            }

            override fun onCompileFailed(p0: NvsTimeline?) {
                callback.onCompileFailed(p0, "合成失败")
            }
        })

        streamingContext.setHardwareErrorCallback { i, s ->
            // 当使用硬件编码时，此处报错应处理，停止当前次的compile操作
            // 当使用了STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER标记关闭硬件编码时，不处理此处报错
            if (decodeType == DecodeType.hardware) {
                callback.onCompileHardwareError(i, s)
                hardwareFail = true
                streamingContext.stop(NvsStreamingContext.STREAMING_ENGINE_INTERRUPT_STOP)
            } else {
                if (!reportedWhenUsingSoftware) {
                    callback.onCompileHardwareError(i, s)
                    reportedWhenUsingSoftware = true
                }
            }
        }

        val flag = if(onlyVideo) {
            when (decodeType) {
                DecodeType.software ->  NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_IGNORE_TIMELINE_VIDEO_SIZE or NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER or NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_ONLY_VIDEO
                else -> NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_IGNORE_TIMELINE_VIDEO_SIZE or NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_ONLY_VIDEO
            }
        }else{
            when (decodeType) {
                DecodeType.software ->  NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_IGNORE_TIMELINE_VIDEO_SIZE or NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER
                else -> NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_IGNORE_TIMELINE_VIDEO_SIZE
            }
        }
        streamingContext.compileConfigurations = null
        val compileConfigurations = Hashtable<String, Any>()
        compileConfigurations[NvsStreamingContext.COMPILE_BITRATE] = 15 * 1000000
        compileConfigurations[NvsStreamingContext.COMPILE_USE_OPERATING_RATE] = true
        streamingContext.compileConfigurations = compileConfigurations
        hardwareFail = false
        streamingContext.stop()

        callback.onCompileBegin(timeline, true, flag, outputPath)

        // 如果是软编，将所有clip设置成软件解码
        if (decodeType == DecodeType.software) {
            val videoTrack = timeline.getVideoTrackByIndex(0)
            for (index in 0 until  videoTrack.clipCount) {
                val clip = videoTrack.getClipByIndex(index)
                clip.setSoftWareDecoding(true)
            }
        }

        streamingContext.compileTimeline(
            timeline,
            0,
            timelineDuration,
            outputPath,
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_CUSTOM,
            NvsStreamingContext.COMPILE_BITRATE_GRADE_HIGH,
            flag
        )
    }
}