package com.soul.mediapicker.manager

/**
 * Description: TODO
 * Author: zhuMing
 * CreateDate: 2025/3/24 星期一 16:55
 * ProjectName: fuyao_clip_android
 * UpdateUser:
 * UpdateDate: 2025/3/24 星期一 16:55
 * UpdateRemark:
 */
interface IDownloadListener {



    /**
     * 下载进度
     *
     * @param total    总进度
     * @param progress 下载进度
     */
    fun progress(total: Int, progress: Int)

    /**
     * 下载完成
     *
     * @param taskId 任务id
     */
    fun completed()

    /**
     * 下载错误
     *
     * @param e      异常错误
     * @param taskId 任务id
     */
    fun error(taskId: Int, e: Throwable?)
}