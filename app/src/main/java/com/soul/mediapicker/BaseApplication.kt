package com.soul.mediapicker

import com.meicam.sdk.NvsStreamingContext
import com.soul.lib.Global

/**
 * Description: TODO
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:22
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:22
 * UpdateRemark:
 */
class BaseApplication : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化全局配置
        initGlobalConfig()
    }

    private fun initGlobalConfig() {
        // 这里可以进行全局配置的初始化，比如日志、网络等
        // LogUtil.init(this) // 假设有一个日志工具类
        // GsonUtil.init() // 假设有一个Gson工具类
        // NvsStreamingContext.init(this) // 初始化NvsStreamingContext
        // 其他全局配置...
        Global.init(this, BuildConfig.DEBUG)
        val mLicPath = "assets:/meishe.lic"
        initSdk(mLicPath)
    }


    fun initSdk(licPath: String) {
        NvsStreamingContext.init(
            this, licPath,
            NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT
                    or NvsStreamingContext.STREAMING_CONTEXT_FLAG_ENABLE_HDR_DISPLAY_WHEN_SUPPORTED
                    or NvsStreamingContext.STREAMING_CONTEXT_FLAG_INTERRUPT_STOP_FOR_INTERNAL_STOP
                    or NvsStreamingContext.STREAMING_CONTEXT_FLAG_NEED_GIF_MOTION
                    or NvsStreamingContext.STREAMING_CONTEXT_FLAG_ENABLE_MAX_CACHE_CAPTION_LIMIT
        )
        NvsStreamingContext.getInstance().isDefaultCaptionFade = false
    }

}