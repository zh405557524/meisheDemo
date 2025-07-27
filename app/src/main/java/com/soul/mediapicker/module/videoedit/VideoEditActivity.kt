package com.soul.mediapicker.module.videoedit

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.fuyao.tuiwen.handler.NvsCompileHandler
import com.meicam.sdk.NvsStreamingContext
import com.meicam.sdk.NvsTimeline
import com.meicam.sdk.NvsVideoResolution
import com.soul.lib.utils.LogUtil
import com.soul.lib.utils.ToastUtils
import com.soul.mediapicker.DecodeType
import com.soul.mediapicker.base.BaseActivity
import com.soul.mediapicker.bean.SrtConfig
import com.soul.mediapicker.bean.srtDefaultByHeight
import com.soul.mediapicker.databinding.ActivityVideEditBinding
import com.soul.mediapicker.manager.NvsSteamManager
import com.soul.mediapicker.utils.LoadingDialogUtil
import com.soul.mediapicker.utils.Util.getCustomHeight
import java.io.File

class VideoEditActivity : BaseActivity<ActivityVideEditBinding>() {
    var TAG = "VideEditActivity"

    lateinit var videoEditViewModel: VideoEditViewModel

    /**
     * 播放器功能
     */
    lateinit var playerFunction: PlayerFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        // 添加屏幕常亮标志
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
    }

    override fun getRootViewBind(): ActivityVideEditBinding {
        return ActivityVideEditBinding.inflate(layoutInflater)
    }

    override fun initView() {
        playerFunction = PlayerFunction(
            viewBinding.imageViewPlay, viewBinding.sbStrength, lifecycleOwner = this
        )
    }

    override fun initData() {
        // 初始化ViewModel
        videoEditViewModel = VideoEditViewModel()

        // 加载视频数据
        loadVideoData()
    }

    override fun initEvent() {
        viewBinding.btnMainTop.setOnClickListener {
            exportVideo()
            LoadingDialogUtil.showLoading(this@VideoEditActivity, "正在导出中...")
        }
    }

    fun buildingDir(activity: Activity): String {
        return activity.getExternalFilesDir("clip")!!.absolutePath + File.separator + "videos"
    }

    var tempPath: String? = null
    fun exportVideo() {
        var mTimeline = NvsSteamManager.instance.timeline ?: return

        val videoRes: NvsVideoResolution = NvsSteamManager.instance.timeline!!.videoRes
        val resultHeight = getCustomHeight(videoRes.imageWidth, videoRes.imageHeight)
        tempPath = NvsCompileHandler().compile(
            NvsStreamingContext.getInstance(), mTimeline, buildingDir(this),
            resultHeight, mTimeline.duration, DecodeType.hardware, object :
                NvsCompileHandler.NvsCompileCallback {
                override fun onCompileBegin(
                    timeline: NvsTimeline?,
                    operateRate: Boolean,
                    flag: Int,
                    outputPath: String
                ) {
                }

                override fun onCompileProgress(timeline: NvsTimeline?, progress: Int) {
                    LoadingDialogUtil.setText("导出中 $progress%")
                }

                override fun onCompileCancel() {
                    LoadingDialogUtil.hideLoading()
                }

                override fun onCompileFinished(timeline: NvsTimeline?, outputPath: String) {
                    LoadingDialogUtil.hideLoading()
                    ToastUtils.showLongToast("导出成功: $outputPath")
                    val duration = NvsSteamManager.instance.getDuration()
                    videoEditViewModel.saveVideoToGallery(
                        this@VideoEditActivity,
                        outputPath,
                        duration
                    )
                    playerFunction.show()
                }

                override fun onCompileFailed(timeline: NvsTimeline?, errMsg: String) {
                    ToastUtils.showLongToast("导出失败: $errMsg")
                    LoadingDialogUtil.hideLoading()
                }

                override fun onCompileHardwareError(code: Int, reason: String) {
                    LoadingDialogUtil.hideLoading()
                }

                override fun onCompileCompleteInfo(
                    isHardwareError: Boolean,
                    errorType: Int,
                    errorMsg: String?,
                    flag: Int
                ) {

                }

            })
    }

    /**
     * 加载视频数据
     */
    private fun loadVideoData() {
        LoadingDialogUtil.showLoading(this@VideoEditActivity, "正在加载视频数据...")
        videoEditViewModel.loadVideoData(this, object : VideoEditViewModel.OnVideoDataLoadListener {
            override fun onSuccess(videoDataModel: VideoDataModel) {
                LogUtil.i(
                    TAG,
                    "视频数据加载成功: 音频路径=${videoDataModel.audioPath}, 视频数量=${videoDataModel.videoList.size}, 字幕数量=${videoDataModel.subtitleList.size}"
                )

                // 在这里处理加载成功后的逻辑
                // 例如：创建时间线、设置视频数据等
                handleVideoDataLoaded(videoDataModel)
            }

            override fun onError(errorMessage: String) {
                LogUtil.e(TAG, "视频数据加载失败: $errorMessage")
                Toast.makeText(this@VideoEditActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onProgress(message: String) {
                LogUtil.i(TAG, "加载进度: $message")
                // 可以在这里更新加载对话框的消息
                LoadingDialogUtil.setText(message)
            }
        })
    }

    /**
     * 处理视频数据加载完成后的逻辑
     */
    private fun handleVideoDataLoaded(videoDataModel: VideoDataModel) {
        // 这里可以添加处理视频数据的逻辑
        // 例如：创建时间线、设置视频源等
        LogUtil.i(TAG, "开始处理视频数据...")

        // 示例：如果视频列表不为空，可以创建时间线
        if (videoDataModel.videoList.isNotEmpty()) {
            // 这里可以调用createTimeLine方法
            createTimeLine(resolutionWidth, resolutionHeight)

            // 使用本地音频路径
            NvsSteamManager.instance.addAudio(
                videoDataModel.audioLocalPath, 0, videoDataModel.audioDuration
            )

            var video = videoDataModel.videoList
            for (index in video.indices) {
                val item = video[index]
                // 使用本地视频路径
                NvsSteamManager.instance.appendVideoClip(
                    item.localPath, 0, 0, false // 假设每个视频片段5秒，非静音
                )
            }

            // 处理字幕数据
            handleSubtitleData(videoDataModel.subtitleList)

            LogUtil.i(TAG, "视频数据处理完成，时间线已创建。")
            NvsSteamManager.instance.prepare()
            playerFunction.show()
        }
    }

    /**
     * 处理字幕数据
     */
    private fun handleSubtitleData(subtitleList: List<CartoonTimelineItem>) {
        LogUtil.i(TAG, "开始处理字幕数据，共 ${subtitleList.size} 条字幕")
        var srt = srtDefaultByHeight(25.0f)
        NvsSteamManager.instance.addAllCaption(subtitleList, srt, null)
        // 这里可以添加字幕处理逻辑
        // 例如：将字幕添加到时间线、显示字幕等
        subtitleList.forEachIndexed { index, subtitle ->
            LogUtil.i(
                TAG,
                "字幕 ${index + 1}: ${subtitle.start}ms - ${subtitle.end}ms: ${subtitle.srt.text}"
            )
        }
    }


    private var resolutionWidth = 1440

    private var resolutionHeight = 1080

    fun createTimeLine(resolutionWidth: Int?, resolutionHeight: Int?) {
        if (NvsSteamManager.instance.timeline != null) return

        // 设置视频分辨率
        if (resolutionWidth != null && resolutionHeight != null) {
            this.resolutionWidth = resolutionWidth
            this.resolutionHeight = resolutionHeight
        }

        // 创建时间线
        NvsSteamManager.instance.create(this, this.resolutionWidth, this.resolutionHeight)
        NvsSteamManager.instance.connect(viewBinding.ivVideo)

        LogUtil.i(
            TAG,
            "createTimeLine: resolutionWidth:$resolutionWidth, resolutionHeight:$resolutionHeight"
        )

        // 获取容器高度和屏幕宽度
        val containerHeight = viewBinding.flVideoContainer.measuredHeight
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        // 计算视频宽度
        var videoWidth =
            (this.resolutionWidth * (containerHeight / this.resolutionHeight.toFloat())).toInt()
        var videoHeight = containerHeight

        // 如果视频宽度超过屏幕宽度，重新计算高度
        if (videoWidth > screenWidth) {
            videoWidth = screenWidth
            videoHeight =
                (this.resolutionHeight * (screenWidth / this.resolutionWidth.toFloat())).toInt()
        }

        // 设置视频容器的布局参数

        viewBinding.flVideo.layoutParams.width = videoWidth;
        viewBinding.flVideo.layoutParams.height = videoHeight;
        viewBinding.flVideo.requestLayout()
    }

}