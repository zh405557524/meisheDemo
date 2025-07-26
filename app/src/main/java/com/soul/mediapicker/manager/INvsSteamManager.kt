package com.soul.mediapicker.manager

import android.content.Context
import com.soul.mediapicker.view.FyLiveWindow

/**
 * Description: TODO
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:14
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:14
 * UpdateRemark:
 */
interface INvsSteamManager {


    /**
     * 初始化时间线与分辨率
     * @param context 应用上下文
     * @param resolutionWidth 视频宽度
     * @param resolutionHeight 视频高度
     */
    fun create(context: Context, resolutionWidth: Int, resolutionHeight: Int)

    /**
     * 连接到 UI 窗口控件
     * @param fyLiveWindow 播放窗口
     */
    fun connect(fyLiveWindow: FyLiveWindow)

    /** 准备预览第一帧，添加完片段后调用 */
    fun prepare()

    /**
     * 预览指定时间戳帧
     * @param timestamp 毫秒时间戳
     * @param isCallBack 是否回调
     */
    fun preview(timestamp: Long = 0,isCallBack:Boolean =true)

    /**
     * 从指定时间点开始播放
     * @param timestamp 毫秒时间戳
     */
    fun play(timestamp: Long = 0)



    /** 暂停播放 */
    fun pause()

    /** 停止播放 */
    fun stop()


    /** 自动播放或暂停 */
    fun autoPlay()

    /** 是否正在播放 */
    fun isPlaying(): Boolean

    /** 当前是否处于准备完成状态 */
    fun isPrepare(): Boolean


}