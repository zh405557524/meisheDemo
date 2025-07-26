package com.soul.mediapicker.module.videoedit

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.soul.basekotlinui.viewmodel.BaseViewModel
import com.soul.lib.Global
import com.soul.lib.utils.LogUtil
import com.soul.mediapicker.manager.DownLoadManager2
import com.soul.mediapicker.utils.GsonUtil
import com.soul.mediapicker.utils.LoadingDialogUtil
import com.soul.mediapicker.utils.VideoCompileUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors

/**
 * Description: 视频编辑ViewModel
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:07
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:07
 * UpdateRemark:
 */
class VideoEditViewModel : BaseViewModel() {
    
    var audioPath = "https://tuiwen-oss.fuyaoup.com/app-tmp/1/6821aa3423934e488ff0d7c6ce3c7722.mp3"
    
    // 数据加载回调接口
    interface OnVideoDataLoadListener {
        fun onSuccess(videoDataModel: VideoDataModel)
        fun onError(errorMessage: String)
        fun onProgress(message: String) // 进度更新回调
    }

    /**
     * 加载视频数据
     * @param context 上下文
     * @param listener 加载结果回调
     */
    fun loadVideoData(context: Context, listener: OnVideoDataLoadListener) {
        // 显示加载对话框
        LoadingDialogUtil.showLoading(context, "正在加载视频数据...")
        
        // 在协程中执行异步操作
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. 读取assets中的video.json文件
                withContext(Dispatchers.Main) {
                    listener.onProgress("正在读取视频数据...")
                }
                val videoList = loadVideoListFromAssets(context)
                
                // 2. 读取assets中的srt.json文件
                withContext(Dispatchers.Main) {
                    listener.onProgress("正在读取字幕数据...")
                }
                val subtitleList = loadSubtitleListFromAssets(context)
                
                // 3. 下载音频文件
                withContext(Dispatchers.Main) {
                    listener.onProgress("正在下载音频文件...")
                }
                val audioLocalPath = downloadAudioFile(context, listener)
                
                // 4. 下载视频文件
                withContext(Dispatchers.Main) {
                    listener.onProgress("正在下载视频文件...")
                }
                val downloadedVideoList = downloadVideoFiles(context, videoList, listener)
                
                // 5. 创建数据模型
                val videoDataModel = VideoDataModel(
                    audioPath, 
                    audioLocalPath, 
                    downloadedVideoList,
                    subtitleList,
                    829 * 1000L
                ) // 假设音频时长为829秒
                
                // 5. 切换到主线程返回结果
                withContext(Dispatchers.Main) {
                    LoadingDialogUtil.hideLoading()
                    listener.onSuccess(videoDataModel)
                }
                
            } catch (e: Exception) {
                // 6. 处理异常
                withContext(Dispatchers.Main) {
                    LoadingDialogUtil.hideLoading()
                    listener.onError("加载视频数据失败: ${e.message}")
                }
            }
        }
    }
    
    /**
     * 从assets文件夹读取视频列表
     */
    private fun loadVideoListFromAssets(context: Context): List<VideoItem> {
        return try {
            val assetManager: AssetManager = context.assets
            val inputStream = assetManager.open("video.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            
            // 使用Gson解析JSON数据，这里需要创建一个临时的数据类来解析JSON
            val type = object : TypeToken<List<TempVideoItem>>() {}.type
            val tempList: List<TempVideoItem> = GsonUtil.fromJson(jsonString, type) ?: emptyList()
            
            // 转换为VideoItem，暂时设置空的本地路径
            tempList.map { temp ->
                VideoItem(
                    height = temp.height,
                    url = temp.url,
                    localPath = "", // 将在下载后设置
                    width = temp.width
                )
            }
            
        } catch (e: Exception) {
            throw Exception("读取视频数据文件失败: ${e.message}")
        }
    }
    
    /**
     * 从assets文件夹读取字幕列表
     */
    private fun loadSubtitleListFromAssets(context: Context): List<CartoonTimelineItem> {
        return try {
            val assetManager: AssetManager = context.assets
            val inputStream = assetManager.open("srt.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            
            // 使用Gson解析JSON数据
            val type = object : TypeToken<List<TempSubtitleItem>>() {}.type
            val tempList: List<TempSubtitleItem> = GsonUtil.fromJson(jsonString, type) ?: emptyList()
            
            // 转换为CartoonTimelineItem
            tempList.mapIndexed { index, temp ->
                CartoonTimelineItem(
                    id = "subtitle_$index",
                    start = temp.start_time * 1000, // 转换为毫秒
                    end = temp.end_time * 1000,     // 转换为毫秒
                    srt = Srt(
                        start = temp.start_time * 1000, // 转换为毫秒
                        end = temp.end_time * 1000,     // 转换为毫秒
                        text = temp.sentence,
                        richText = temp.sentence,
                        textWithBreak = temp.sentence,
                        speaker = "-1",
                        recommandRuleName = "",
                        wrapSrtContent = temp.sentence
                    )
                )
            }
            
        } catch (e: Exception) {
            throw Exception("读取字幕数据文件失败: ${e.message}")
        }
    }
    
    /**
     * 下载音频文件
     */
    private suspend fun downloadAudioFile(context: Context, listener: OnVideoDataLoadListener): String {
        return withContext(Dispatchers.IO) {
            val audioFileName = DownLoadManager2.instance.md5(audioPath)
            val audioLocalPath = "${Global.getExternalCacheDir()}audio/${audioFileName}.mp3"
            
            // 确保目录存在
            val audioDir = File(audioLocalPath).parentFile
            if (!audioDir!!.exists()) {
                audioDir.mkdirs()
            }
            
            // 如果文件已存在，直接返回路径
            if (File(audioLocalPath).exists()) {
                LogUtil.i("VideoEditViewModel", "音频文件已存在: $audioLocalPath")
                return@withContext audioLocalPath
            }

                        // 下载音频文件
            val urls = arrayListOf(audioPath)
            val paths = arrayListOf(audioLocalPath)
            
            DownLoadManager2.instance.addDownloadTaskSync(
                urls, paths
            ) { progress ->
                LogUtil.i("VideoEditViewModel", "音频下载进度: $progress%")
                // 使用协程启动器在主线程执行回调
                CoroutineScope(Dispatchers.Main).launch {
                    listener.onProgress("音频下载进度: $progress%")
                }
            }
            
            audioLocalPath
        }
    }
    
    /**
     * 下载视频文件列表
     */
    private suspend fun downloadVideoFiles(context: Context, videoList: List<VideoItem>, listener: OnVideoDataLoadListener): List<VideoItem> {
        return withContext(Dispatchers.IO) {
            val videoDir = "${Global.getExternalCacheDir()}videos/"
            val videoDirFile = File(videoDir)
            if (!videoDirFile.exists()) {
                videoDirFile.mkdirs()
            }
            
            val urls = arrayListOf<String>()
            val paths = arrayListOf<String>()
            val videoItems = arrayListOf<VideoItem>()
            
            // 准备下载列表
            videoList.forEachIndexed { index, videoItem ->
                val fileName = DownLoadManager2.instance.md5(videoItem.url)
                val localPath = "${videoDir}${fileName}.mp4"
                
                // 如果文件已存在，跳过下载
                if (File(localPath).exists()) {
                    LogUtil.i("VideoEditViewModel", "视频文件已存在: $localPath")
                    videoItems.add(videoItem.copy(localPath = localPath))
                } else {
                    urls.add(videoItem.url)
                    paths.add(localPath)
                    videoItems.add(videoItem.copy(localPath = localPath))
                }
            }
            
                        // 如果有需要下载的文件
            if (urls.isNotEmpty()) {
                LogUtil.i("VideoEditViewModel", "开始下载 ${urls.size} 个视频文件")
                
                DownLoadManager2.instance.addDownloadTaskSync(
                    urls, paths
                ) { progress ->
                    LogUtil.i("VideoEditViewModel", "视频下载进度: $progress%")
                    // 使用协程启动器在主线程执行回调
                    CoroutineScope(Dispatchers.Main).launch {
                        listener.onProgress("视频下载进度: $progress%")
                    }
                }
            }
            
            videoItems
        }

    }

    //保存视频
    fun saveVideoToGallery(context: Context, videoFilePath: String, duration: Long) {//毫秒
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {

                // 在这里执行子线程任务
                val videoFile = File(videoFilePath)
                if (!videoFile.exists()) {
                } else {
//                    VideoUtils.getVideoDuration(videoFilePath)?.let {
//                        if (it < duration) {
////                        ToastUtil.showToast("导出失败:视频时长不符合要求")
//                            LogUtil.i(TAG, "导出失败:视频时长不符合要求")
//                            return@execute
//                        }
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 创建 ContentValues 来存储视频元数据
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Video.Media.DISPLAY_NAME, videoFile.name) // 文件名
                            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4") // MIME类型
                            put(
                                MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM
                            ) // 相册路径
                        }
                        // 插入视频到 MediaStore
                        val uri: Uri? =
                            context.contentResolver.insert(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues
                            )
                        uri?.let {
                            // 将视频文件写入 MediaStore
                            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                                FileInputStream(videoFile).use { inputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            // 通知相册更新
                            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, it))
                            Log.i("SaveVideo", "Video saved to gallery successfully")
                        } ?: run {
                            Log.e("SaveVideo", "Failed to insert video into MediaStore")
                        }
                    } else {
                        // Android 9 及以下版本 (API 28-)，使用 File 方式存储
                        val videosDir = File(VideoCompileUtil.getCompileVideoPath(context))
                        if (!videosDir.exists()) videosDir.mkdirs() // 确保目录存在

                        val targetFile = File(videosDir, videoFile.name)
                        try {
                            FileInputStream(videoFile).use { inputStream ->
                                FileOutputStream(targetFile).use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            // 发送广播通知相册更新
                            context.sendBroadcast(
                                Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(targetFile)
                                )
                            )
                            Log.i(
                                "SaveVideo",
                                "Video saved to gallery successfully in Movies directory (API 28-)"
                            )
                        } catch (e: Exception) {
                            Log.e("SaveVideo", "Failed to save video: ${e.message}")
                        }
                    }
                }
                LoadingDialogUtil.hideLoading()
            } catch (e: Exception) {
                e.printStackTrace()
                LoadingDialogUtil.hideLoading()
            }
        }
        executor.shutdown() // 关闭Executor

    }
    /**
     * 临时数据类，用于解析视频JSON
     */
    private data class TempVideoItem(
        val height: Int,
        val url: String,
        val width: Int
    )
    
    /**
     * 临时数据类，用于解析字幕JSON
     */
    private data class TempSubtitleItem(
        val start_time: Double,
        val end_time: Double,
        val sentence: String
    )
}