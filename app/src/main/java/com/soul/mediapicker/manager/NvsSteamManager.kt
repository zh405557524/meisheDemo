package com.soul.mediapicker.manager

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import com.meicam.sdk.NvsAudioClip
import com.meicam.sdk.NvsAudioResolution
import com.meicam.sdk.NvsAudioTrack
import com.meicam.sdk.NvsCaption
import com.meicam.sdk.NvsColor
import com.meicam.sdk.NvsRational
import com.meicam.sdk.NvsStreamingContext
import com.meicam.sdk.NvsTimeline
import com.meicam.sdk.NvsTimelineCaption
import com.meicam.sdk.NvsVideoClip
import com.meicam.sdk.NvsVideoResolution
import com.meicam.sdk.NvsVideoTrack
import com.soul.lib.utils.LogUtil
import com.soul.mediapicker.bean.ScaleAndRotateBean
import com.soul.mediapicker.bean.SrtConfig
import com.soul.mediapicker.module.videoedit.CartoonTimelineItem
import com.soul.mediapicker.module.videoedit.Srt
import com.soul.mediapicker.utils.GsonUtil
import com.soul.mediapicker.utils.Util
import com.soul.mediapicker.view.FyLiveWindow
import kotlin.math.max

/**
 * Description: TODO
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:14
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:14
 * UpdateRemark:
 */
class NvsSteamManager : INvsSteamManager {

    val TAG = "NvsSteamManager"


    private val COMPILEVIDEORES_2160 = 2160


    /**
     * 时间刻度；单位：毫秒；1秒=1000毫秒=1000000微秒
     */
    @Volatile
    var timestamp: Long = 0

    /**
     * 分辨率-宽度
     */
    var resolutionWidth: Int = 0

    /**
     * 分辨率-高度
     */
    var resolutionHeight: Int = 0

    var mStreamingContext = NvsStreamingContext.getInstance()

    var timeline: NvsTimeline? = null

    /**
     * 视频轨道
     */
    var videoTrack: NvsVideoTrack? = null


    /**
     * 背景音乐音频轨道
     */
    var bgmNvsAudioTrack: NvsAudioTrack? = null

    /**
     * 播放监听
     */
    var onPlayListener: OnPlayListener? = null


    private var mPixTimeLineRatio = 1f

    /**
     * 字幕监听
     */

    /**
     * 播放状态监听
     */
    private var onPlayListeners = ArrayList<OnPlayListener>()

    /**
     * 状态监听
     */
    private var onNvsSteamStateListeners: ArrayList<OnNvsSteamStateListener> = ArrayList()


    private val videoFps = 30

    /**
     * 播放监听
     */
    interface OnPlayListener {

        /**
         * 播放
         */
        fun onPlay()

        /**
         * 播放进度 - 单位：毫秒
         */
        fun onProgress(progress: Long)

        /**
         * 暂停
         */
        fun onPause()

        /**
         * 停止
         */
        fun onStop()

    }


    /**
     * 美摄状态监听
     */
    interface OnNvsSteamStateListener {
        fun onPrepare()//视频，字幕已添加
    }

    companion object {
        val instance by lazy {
            NvsSteamManager()
        }
    }

    /**
     * 创建
     */
    override fun create(context: Context, resolutionWidth: Int, resolutionHeight: Int) {
        this.resolutionWidth = resolutionWidth
        this.resolutionHeight = resolutionHeight
        timeline = createTimeline(resolutionWidth, resolutionHeight)
        LogUtil.i(TAG, "create")
        NvsStreamingContext.getInstance().sdkVersion
        LogUtil.i(
            TAG,
            "sdkVersion:${GsonUtil.toJson(NvsStreamingContext.getInstance().sdkVersion)}"
        )
    }

    /**
     * 连接UI布局
     */
    override fun connect(fyLiveWindow: FyLiveWindow) {
        //设置监听
        if (timeline == null) return
        mStreamingContext.connectTimelineWithLiveWindow(timeline, fyLiveWindow)
        val timelineHeight: Float = timeline!!.getVideoRes().imageHeight.toFloat()
        val liveWindowHeight: Float = fyLiveWindow.getHeight().toFloat()
        mPixTimeLineRatio = timelineHeight / liveWindowHeight
        LogUtil.i(TAG, "connect mPixTimeLineRatio:$mPixTimeLineRatio")
    }


    /**
     * 添加音频
     * @param audioPath 音频路径
     * @param startTime 开始时间 单位：毫秒
     * @param trimIn 裁剪开始时间 单位：毫秒
     * @param trimOut 裁剪结束时间 单位：毫秒
     */
    fun addAudio(audioPath: String, startTime: Long, trimIn: Long, trimOut: Long): NvsAudioClip? {
        if (timeline == null) return null
        if (audioTrack == null) {
            audioTrack = timeline!!.appendAudioTrack()
        }
        return audioTrack!!.addClip(audioPath, startTime * 1000, trimIn * 1000, trimOut * 1000)
    }

    /**
     * 添加音频
     * @param audioPath 音频路径
     * @param startTime 开始时间 单位：毫秒
     * @param duration 时长 单位：毫秒
     */
    fun addAudio(audioPath: String, startTime: Long, duration: Long): NvsAudioClip? {
        if (timeline == null) return null
        if (audioTrack == null) {
            audioTrack = timeline!!.appendAudioTrack()
        }
        return audioTrack!!.addClip(audioPath, startTime * 1000, 0, duration * 1000)
    }


    /**
     * 当前视频长度 单位：微秒
     */
    var videoDuration: Long = 0

    private var videoClips = ArrayList<VideoClip>()

    private data class VideoClip(
        var index: Int,
        var path: String = "",
        var startTime: Long,//微秒
        var endTime: Long,//微秒
        var trimIn: Long = 0,//微秒
        var trimOut: Long = 0,//微秒
        var duration: Long = 0,//微秒
        var isMirror: Boolean = false,//是否镜像
        var isMute: Boolean = false,//是否静音
    )

    /**
     * 添加视频片段
     * @param path 视频路径
     * @param trimIn 裁剪开始时间 单位 毫秒
     * @param trimOut 裁剪结束时间 单位 毫秒
     */
     fun appendVideoClip(
        path: String, trimIn: Long, trimOut: Long, isMute: Boolean
    ): NvsVideoClip? {
        isPrepare = false
        LogUtil.i(TAG, "appendClip")
        if (timeline == null || videoTrack == null) return null
        val nvsVideoClip: NvsVideoClip
        if (trimIn == 0L && trimOut == 0L) {
            nvsVideoClip = videoTrack!!.appendClip(path)
        } else {
            LogUtil.i(TAG, "添加视频片段:trimIn:$trimIn,trimOut:$trimOut isMute:$isMute")
            nvsVideoClip = videoTrack!!.appendClip(path, trimIn * 1000L, trimOut * 1000L)
        }
        if (nvsVideoClip == null) return null
        if (isMute) {
            nvsVideoClip.setVolumeGain(0f, 0f)
        } else {
            nvsVideoClip.setVolumeGain(1f, 1f)
        }
        val duration = videoTrack!!.duration - videoDuration
        videoDuration = videoTrack!!.duration
        val startTime = videoDuration - duration
        val endTime = videoDuration
        videoClips.add(
            VideoClip(
                nvsVideoClip.index, path, startTime, endTime, trimIn, trimOut,
                duration, isMute = isMute
            )
        )
        return nvsVideoClip
    }

    /**
     * 是否进行准备-所有片段添加完成后，才进行准备
     */
    private var isPrepare = false

    /**
     * 准备-预览第一帧
     */
    override fun prepare() {
        if (timeline == null) return
        timestamp = 0
        LogUtil.i(TAG, "prepare")
        seekTimeline(0, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER)
        LogUtil.i(TAG, "准备预览")
        isPrepare = true
        for (onNvsSteamStateListener in onNvsSteamStateListeners) {
            onNvsSteamStateListener.onPrepare()
        }
//        findCurrentCaption(timestamp)//字幕
//        onCationListener?.onTitleChange(currentTitle)//标题
    }


    /**
     * 预览
     * @param timestamp 时间戳 单位：毫秒
     * @param isCallBack 是否回调
     */
    override fun preview(timestamp: Long, isCallBack: Boolean) {
        this.timestamp = timestamp
        if (timeline == null) return
        LogUtil.i(TAG, "preview:$timestamp")
        seekTimeline(
            timestamp,
            NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER
        )
    }

    /**
     * 播放
     * @param timestamp 时间戳 单位：毫秒
     */
    override fun play(timestamp: Long) {
        LogUtil.i(TAG, "play:$timestamp")
        this.timestamp = timestamp
        if (timeline == null) return

        mStreamingContext.playbackTimeline(
            timeline, max(0, timestamp * 1000), -1,
            NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, false, 0
        )
        isPlaying = true
    }


    fun play() {
        if (isPrepare) {
            play(timestamp)
        }
    }


    @Volatile
    private var isPlaying = false

    override fun isPlaying(): Boolean {
        return isPlaying
    }


    override fun pause() {
        if (isPlaying) {
            LogUtil.i(TAG, "pause")
            isPlaying = false
            mStreamingContext.pausePlayback()
        }
    }

    override fun stop() {
        mStreamingContext.stop()
        isPlaying = false
    }

    override fun autoPlay() {
        if (isPlaying()) {
            pause()
        } else {
            play()
        }
    }


    /**
     * 是否准备-如果未进行准备，则可以重新添加资源
     */
    override fun isPrepare(): Boolean {
        return isPrepare
    }


    /**
     * 已添加的字幕的索引
     */
    var captionIndex = 0
    var currentSrtConfig: SrtConfig? = null
    var currentScaleAndRotateBean: ScaleAndRotateBean? = null
    var captions:List<CartoonTimelineItem> ?=null//字幕数据
    var captionLastIndex = 0//字幕索引

    /**
     * 当前字幕集合
     */
    var captionMap = mutableMapOf<Srt, NvsTimelineCaption>()
    /**
     * 添加所有的字幕
     */
     fun addAllCaption(
        captions: List<CartoonTimelineItem>,
        config: SrtConfig,
        scaleAndRotateBean: ScaleAndRotateBean?
    ) {
        currentSrtConfig = config
        this.captions = captions
        currentScaleAndRotateBean = scaleAndRotateBean
        captionLastIndex = captionIndex
        addStepCaption(captions, config, scaleAndRotateBean)
    }


    /**
     *步进添加字幕
     */
    private fun addStepCaption(
        captions: List<CartoonTimelineItem>?,
        config: SrtConfig?,
        scaleAndRotateBean: ScaleAndRotateBean?
    ) {
        if (captions == null || timeline == null || config == null) return
        //从最后一个字幕开始添加
        if (captionLastIndex > 0 && captionLastIndex <= captions.size) {
            var start = captionLastIndex - 4
            if (start < 0) start = 0
            for (index in start until captionLastIndex) {
                val caption = captions[index]
                if (captionMap.containsKey(caption.srt)) {
                    continue
                }
                appendCaption(caption.srt, config, scaleAndRotateBean)
            }
            captionLastIndex = start
        }

        //步进添加字幕
        var captionEnd = captionIndex + 4
        if (captionIndex >= captions.size) {
            return
        }
        if (captionEnd > captions.size) {
            captionEnd = captions.size
        }
        var start = captionIndex - 4
        if (start < 0) start = 0
        for (index in start until captionEnd) {
            val caption = captions[index]
            if (captionMap.containsKey(caption.srt)) {
                continue
            }
            appendCaption(caption.srt, config, scaleAndRotateBean)
        }
        captionIndex = captionEnd
    }

    var coverTime = 0L//封面时间,单位：毫秒
    /**
     * 添加字幕
     */
    private fun appendCaption(
        srt: Srt, srtConfig: SrtConfig, scaleAndRotateBean: ScaleAndRotateBean?,
        isTitle: Boolean = false
    ): NvsTimelineCaption? {
        if (timeline == null) return null

        if (srt.start == null || srt.end == null || srt.text == null) {
            return null
        }

        val text = srt.text ?: ""

        var srtStart = (srt.start ?: 0.0) * 1000
        if (srtStart == 0.0) {
            srtStart = coverTime * 1000.0//所有字幕都在封面后
        }
        val srtEnd = (srt.end ?: 0.0) * 1000
        val srtDuration = srtEnd.toLong() - srtStart.toLong()
        val caption =
            timeline!!.addModularCaption(text, srtStart.toLong(), srtDuration) ?: return null
        if (!isTitle) {
            captionMap[srt] = caption
        }

        //设置字幕动画
//        if (srt.captionAnimationConfig != null) {
//            Util.applyCaptionAnimation(
//                caption,
//                srtDuration / 1000,
//                srt.captionAnimationConfig!!
//            )
//        }
        var realWidth = resolutionWidth.toFloat() - srtConfig.position.x
        if (srtConfig.position.isMidX) {
            realWidth = resolutionWidth.toFloat()
        }
        setCaptionConfig(srtConfig, caption, scaleAndRotateBean)
        val wrapText = Util.getAutoWrapWord(caption, realWidth, text, "", 0)
        caption.text = wrapText
        return caption
    }

    private fun setCaptionConfig(
        srtConfig: SrtConfig?,
        caption: NvsTimelineCaption,
        scaleAndRotateBean: ScaleAndRotateBean?
    ) {
        srtConfig?.let {
            val fontUrl = srtConfig.fontInfo?.url
//            val fontPath = if (fontUrl.isNullOrEmpty()) {
//                PersistStore.getFontLocalPath(PersistStore.getDefaultFontInfo()?.url ?: "")
//                    ?: Consts.cartoonDefaultFontPath
//            } else {
//                PersistStore.getFontLocalPath(fontUrl)
//            }
//            caption.setFontByFilePath(fontPath)
            caption.outlineWidth = 14F
            caption.bold = false
            caption.weight = 66
            caption.anchorPoint = PointF(0.5f, 0.5f)
            caption.textAlignment = NvsCaption.TEXT_ALIGNMENT_CENTER
            if (srtConfig.borderColor != null) {
                caption.drawOutline = true
                caption.outlineColor = colorStringToNvsColor(srtConfig.borderColor)
                caption.outlineWidth = (srtConfig.borderWidth ?: 4).toFloat() * 2
            } else {
                caption.drawOutline = false
            }

            srtConfig.backgroundColor?.let {
                caption.backgroundColor = colorStringToNvsColor(it)
                caption.backgroundRadius = 0F
            }
            if (!srtConfig.shadowColor.isNullOrEmpty() && srtConfig.shadowOffset != null) {
                caption.shadowColor = colorStringToNvsColor(srtConfig.shadowColor)
                caption.shadowOffset = srtConfig.shadowOffset
            }
            caption.textColor = colorStringToNvsColor(srtConfig.color)
            caption.fontSize = srtConfig.fontSize.toFloat()
            caption.opacity = srtConfig.alpha

        }


        scaleAndRotateBean?.let {
            if (null != it.assetAnchor && null != it.scaleFactor && null != it.angle) {
                it.scaleFactor?.forEach { scale ->
                    caption.scaleCaption(scale, it.assetAnchor)
                }
                caption.rotateCaption(it.angle!!)
            }
            if (null != it.translateCaptionPf) {
            }
            caption.translateCaption(it.translateCaptionPf)

        }
    }


    private fun colorStringToNvsColor(colorString: String?): NvsColor? {
        if (colorString == null || colorString.isEmpty()) return null
        val color = NvsColor(1F, 1F, 1F, 1F)
        val hexColor = Color.parseColor(colorString)
        color.a = (hexColor and -0x1000000 ushr 24).toFloat() / 0xFF
        color.r = (hexColor and 0x00ff0000 shr 16).toFloat() / 0xFF
        color.g = (hexColor and 0x0000ff00 shr 8).toFloat() / 0xFF
        color.b = (hexColor and 0x000000ff).toFloat() / 0xFF
        return color
    }

    /**
     * 播放进度
     * @param progress 进度  千分比 1~1000
     */
     fun seek(progress: Long) {
        if (timeline == null) return
        timestamp = (progress / 1000f * timeline!!.duration / 1000).toLong()
        play(timestamp)
    }


    /**
     * 获取时长 单位：毫秒
     */
     fun getDuration(): Long {
        if (timeline == null) return 0
        return timeline!!.duration / 1000
    }


     fun addOnNvsSteamStateListener(onNvsSteamStateListener: OnNvsSteamStateListener) {
        if (!onNvsSteamStateListeners.contains(onNvsSteamStateListener)) {
            onNvsSteamStateListeners.add(onNvsSteamStateListener)
        }
    }

     fun addOnPlayListener(onPlayListener: OnPlayListener) {
        if (!onPlayListeners.contains(onPlayListener)) {
            onPlayListeners.add(onPlayListener)
        }
    }

     fun removeOnPlayListener(onPlayListener: OnPlayListener) {
        onPlayListeners.remove(onPlayListener)
    }

    var audioTrack: NvsAudioTrack? = null

    private fun createTimeline(
        resolutionWidth: Int, resolutionHeight: Int
    ): NvsTimeline {
        if (timeline == null) {
            val videoResolution = NvsVideoResolution()
            videoResolution.imageWidth = resolutionWidth / 4 * 4
            videoResolution.imageHeight = resolutionHeight / 2 * 2
            videoResolution.imagePAR = NvsRational(1, 1)
            val videoFps = NvsRational(videoFps, 1)
            val audioResolution = NvsAudioResolution()
            audioResolution.sampleRate = 44100
            audioResolution.channelCount = 2
            timeline = NvsStreamingContext.getInstance().createTimeline(
                videoResolution,
                videoFps,
                audioResolution,
                NvsStreamingContext.CREATE_TIMELINE_FLAG_DONT_ADD_DEFAULT_VIDEO_TRANSITION
            )
            videoTrack = timeline?.appendVideoTrack()
            audioTrack = timeline?.appendAudioTrack()
            bgmNvsAudioTrack = timeline?.appendAudioTrack()
        }
        return timeline!!
    }

    /**
     * 预览
     * Seek
     */
    private fun seekTimeline(timestamp: Long, seekShowMode: Int) {
        LogUtil.i(TAG, "seekTimeline:$timestamp")
        val settingValues = ParameterSettingValues.instance()
        val compileHeight = settingValues.compileVideoRes
        if (compileHeight == COMPILEVIDEORES_2160) {
            val nvsRational = NvsRational(1, 2)
            mStreamingContext.seekTimeline(
                timeline, timestamp * 1000,
                nvsRational,
                seekShowMode or NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_BUDDY_ORIGIN_VIDEO_FRAME
            )
        } else {
            mStreamingContext.seekTimeline(
                timeline, timestamp * 1000,
                NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE,
                seekShowMode or NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_BUDDY_ORIGIN_VIDEO_FRAME
            )
        }
    }


}