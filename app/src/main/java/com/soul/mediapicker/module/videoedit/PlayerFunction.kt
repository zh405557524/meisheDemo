package com.soul.mediapicker.module.videoedit

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.soul.mediapicker.manager.NvsSteamManager
import com.soul.mediapicker.utils.TimeUtil

/**
 * Description: 播放器UI
 * Author: zhuMing
 * CreateDate: 2025/1/3 星期五 14:17
 * ProjectName: fuyao_clip_android
 * UpdateUser:
 * UpdateDate: 2025/1/3 星期五 14:17
 * UpdateRemark:
 */
class PlayerFunction(
    var playView: View? = null,//播放按钮
    var seekBar: SeekBar? = null,//进度条
    var currentTimeTextView: TextView? = null,//当前时间
    var durationTextView: TextView? = null,//总时长
    val lifecycleOwner: LifecycleOwner
) : LifecycleEventObserver {

    val TAG = "PlayerFunction"

    init {
        initView()
        initData()
        initEvent()
        lifecycleOwner.lifecycle.addObserver(this)
    }


    private lateinit var onPlayListener: NvsSteamManager.OnPlayListener


    private lateinit var onNvsSteamStateListener: NvsSteamManager.OnNvsSteamStateListener

    private fun initView() {

    }


    private fun initData() {


    }


    private fun initEvent() {
        seekBar?.max = 1000
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                NvsSteamManager.instance.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                NvsSteamManager.instance.seek(seekBar?.progress?.toLong() ?: 0)


            }
        })
        onPlayListener = object : NvsSteamManager.OnPlayListener {

            override fun onPlay() {
                playView?.alpha = 0F
            }

            override fun onProgress(progress: Long) {
                if (seekBar?.visibility == View.VISIBLE) {
                    val duration = NvsSteamManager.instance.getDuration()
                    seekBar?.progress = ((progress.toFloat() / duration.toFloat()) * 1000).toInt()
                    currentTimeTextView?.text = TimeUtil.parseMinuteSecond(progress / 1000)
                }
            }

            override fun onPause() {
                playView?.alpha = 1.0f
            }

            override fun onStop() {
                playView?.alpha = 1.0f
            }
        }
        NvsSteamManager.instance.addOnPlayListener(onPlayListener)

        onNvsSteamStateListener = object :
            NvsSteamManager.OnNvsSteamStateListener {
            override fun onPrepare() {
                val duration = NvsSteamManager.instance.getDuration()
                if (duration > 0) {
                    durationTextView?.text = TimeUtil.parseMinuteSecond(duration / 1000)
                }
            }
        }
        NvsSteamManager.instance.addOnNvsSteamStateListener(onNvsSteamStateListener)

        playView?.setOnClickListener {
            NvsSteamManager.instance.autoPlay()
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

        when (event) {
            Lifecycle.Event.ON_DESTROY -> {}
            Lifecycle.Event.ON_CREATE -> {}
            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {}
            Lifecycle.Event.ON_PAUSE -> {
                NvsSteamManager.instance.pause()
            }

            Lifecycle.Event.ON_STOP -> {}
            Lifecycle.Event.ON_ANY -> {}
        }
    }

    fun show() {
        playView?.visibility = View.VISIBLE
        seekBar?.visibility = View.VISIBLE
        currentTimeTextView?.visibility = View.VISIBLE
        durationTextView?.visibility = View.VISIBLE

    }

    fun hide() {
        playView?.visibility = View.GONE
        seekBar?.visibility = View.GONE
        currentTimeTextView?.visibility = View.GONE
        durationTextView?.visibility = View.GONE
    }

    fun destroy() {
        NvsSteamManager.instance.removeOnPlayListener(onPlayListener)
    }

    /**
     * 是否控制播放
     */
    var isPlayControl = false
        set(value) {
            playView?.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

}