package com.soul.mediapicker.manager

import android.os.SystemClock
import com.soul.lib.Global
import com.soul.lib.module.net.NetManager
import com.soul.lib.utils.LogUtil
import com.soul.mediapicker.manager.download.DownloadManager
import com.soul.mediapicker.manager.download.DownloadProgressListener
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Description: TODO
 * Author: zhuMing
 * CreateDate: 2025/3/24 星期一 16:32
 * ProjectName: fuyao_clip_android
 * UpdateUser:
 * UpdateDate: 2025/3/24 星期一 16:32
 * UpdateRemark:
 */
class DownLoadManager2() {
    val TAG = "DownLoadManager2"

    companion object {
        val instance = DownLoadManager2()
    }


    /**
     * 添加下载任务
     * @param url 下载地址
     * @param path 保存路径
     * @param iDownloadListener 下载监听
     */
    fun addDownloadTask(
        url: List<String>, path: List<String>, iDownloadListener: IDownloadListener
    ) {
        val downloadTask = DownloadTask(6)
        downloadTask.addDownloadTask(url, path, iDownloadListener)
    }

    fun md5(url: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(url.toByteArray()))
            .toString(16).padStart(32, '0')
    }
    /**
     * 添加下载任务
     * @param url 下载地址
     * @return 返回保存文件路径
     */
    fun addDownloadTask(
        url: List<String>,
        downloadListener: ((List<String>) -> Unit)?
    ): List<String> {
        val mNoticeBasePath = Global.getExternalCacheDir() + "resource"
        val paths = ArrayList<String>()
        url.forEach {
            val fileName = md5(it)
            val path = "$mNoticeBasePath/${fileName}.png"
            if (File(path).exists()) {//如果文件存在，不用下载
//                File(path).delete()
            }
            paths.add(path)
        }
        val downloadTask = DownloadTask(5)
        downloadTask.addDownloadTask(url, paths, object : IDownloadListener {
            override fun progress(total: Int, progress: Int) {
//                downloadListener?.invoke(paths)
            }

            override fun completed() {
                downloadListener?.invoke(paths)
            }

            override fun error(taskId: Int, e: Throwable?) {
                throw e!!
            }
        })

        return paths
    }

    var taskId = 10;
    suspend fun addDownloadTaskSync(
        urls: java.util.ArrayList<String>, paths: java.util.ArrayList<String>,
        downloadProgressListener: ((String) -> Unit)? = null
    ) {
        return suspendCoroutine { continuation ->
            synchronized(this) {
                DownloadTask(taskId++).addDownloadTask(urls, paths, object : IDownloadListener {
                    override fun progress(total: Int, progress: Int) {
                        downloadProgressListener?.invoke(
                            ((progress / total.toFloat()) * 100).toInt().toString()
                        )
                    }

                    override fun completed() {
                        LogUtil.i(TAG, "下载完成")
                        continuation.resume(Unit)
                    }

                    override fun error(taskId: Int, e: Throwable?) {
                        continuation.resumeWithException(e!!)
                        throw e!!
                    }
                })
            }
        }
    }

    /**
     * 下载文件
     * @param url 下载地址
     * @param path 保存路径
     */
    fun addDownloadFileTask(
        url: String, path: String, downloadSucceedListener: (() -> Unit)?
    ) {
        val urls = ArrayList<String>()
        urls.add(url)
        val paths = ArrayList<String>()
        paths.add(path)

        DownloadTask(4).addDownloadTask(urls, paths, object : IDownloadListener {
            override fun progress(total: Int, progress: Int) {

            }

            override fun completed() {
                downloadSucceedListener?.invoke()
            }

            override fun error(taskId: Int, e: Throwable?) {
                throw e!!
            }
        })
    }


    suspend fun addDownloadTaskSync(
        url: List<String>, path: List<String>,
        downloadProgressListener: ((String) -> Unit)? = null
    ) {
        val urls = ArrayList<String>()
        urls.addAll(url)
        val paths = ArrayList<String>()
        paths.addAll(path)
        return addDownloadTaskSync(urls, paths, downloadProgressListener)
    }


    class DownloadTask(val id: Int = 0) {
        val TAG = "DownloadTask"
        val isDebug = LogUtil.sIsDebug
        val downloadTaskId = ArrayList<String>()
        val downloadedTaskId = ArrayList<Int>()

        val taskMap = HashMap<Int, String>()

        var isCompleted = false


        val downloadListener = object :
            DownloadProgressListener {

            override fun pending(taskId: Int) {
//                if (isDebug) {
//                    LogUtil.i(TAG, "pending:$taskId  url:${taskMap[taskId]}")
//                }
            }

            override fun progress(taskId: Int, total: Int, progress: Int) {
            }

            override fun completed(taskId: Int) {
                synchronized(this) {
                    if (isDebug) {
                        LogUtil.i(
                            TAG,
                            "${id} addDownloadTask completed:$taskId  url:${taskMap[taskId]}"
                        )
                    }
                    downloadedTaskId.add(taskId)
                    val total = downloadTaskId.size
                    var progress = downloadedTaskId.size
                    if (progress > total) progress = total
                    downloadProgressListener?.progress(total, progress)

                    SystemClock.sleep(80)
                    DownloadManager.getInstance().removeDownloadListener(taskId)
                    if (downloadTaskId.size <= downloadedTaskId.size && !isCompleted) {
                        isCompleted = true
                        downloadProgressListener?.completed()
                    }
                }
            }

            override fun error(taskId: Int, e: Throwable?) {
                LogUtil.i(
                    TAG,
                    "addDownloadTask error:$taskId  url:${taskMap[taskId]} e:${e?.message}"
                )
                downloadProgressListener?.error(taskId, e)
            }
        }

        init {
            DownloadManager.getInstance().setDownloadManager(downloadListener)
        }


        var downloadProgressListener: IDownloadListener? = null


        fun addDownloadTask(
            url: List<String>, path: List<String>, iDownloadListener: IDownloadListener
        ) {
            if (!NetManager.getInstance(Global.getContext())
                    .isAvailable(Global.getContext(), true)
            ) {
                throw IllegalStateException("网络不可用")
            }
            synchronized(DownloadTask::class) {
                isCompleted = false
                LogUtil.i(TAG, " ${id} 开始下载 url:$url path:$path")
                this.downloadProgressListener = iDownloadListener
                downloadTaskId.addAll(url)
                for (i in url.indices) {
                    LogUtil.i(TAG, "i:${i}")
                    val taskId =
                        DownloadManager.getInstance()
                            .addDownload(Global.getContext(), url[i], path[i])
                    taskMap[taskId] = url[i]
                    if (isDebug) {
                        LogUtil.i(
                            TAG,
                            "${id}  addDownloadTask taskId:$taskId  url:${url[i]} path:${path[i]}"
                        )
                    }
                }

            }
        }
    }
}