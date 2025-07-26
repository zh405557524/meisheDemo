package com.soul.mediapicker.base

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.soul.lib.utils.Bar2Utils
import com.soul.lib.utils.LogUtil

/**
 * Description: kotlinActivity的基类
 * Author: zhuMing
 * CreateDate: 2024-05-17 16:56
 * ProjectName: traveler
 * UpdateUser:
 * UpdateDate: 2024-05-17 16:56
 * UpdateRemark:
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    val context = this

    protected lateinit var viewBinding: T
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        setCustomDensity()
        super.onCreate(savedInstanceState)
        //沉浸状态至布局中
        transparentStatusBar()
        //设置状态栏的色彩模式为Light
        setStatusBarLightMode()
        viewBinding = getRootViewBind()
        setContentView(viewBinding.root)
        initView()
        initData()
        initEvent()
        initObserver()
    }

    open fun setStatusBarLightMode() {
        Bar2Utils.setStatusBarLightMode(this, true)
    }

    open fun transparentStatusBar() {
        Bar2Utils.transparentStatusBar(this)
    }

    abstract fun getRootViewBind(): T

    abstract fun initView()

    abstract fun initData()

    abstract fun initEvent()

    open fun initObserver() {

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setCustomDensity()
    }


    /**
     * 适配，修改设备密度
     */
    private var sNoncompatDensity = 0f

    private var sNoncompatScaledDensity = 0f

    protected open fun setCustomDensity() {
        val application = application
        val appDisplayMetrics = application.resources.displayMetrics
        if (sNoncompatDensity == 0f) {
            sNoncompatDensity = appDisplayMetrics.density
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity
            // 防止系统切换后不起作用
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity =
                            application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
        val designDensity = getDesignDensity()
        LogUtil.i(TAG, "像素密度designDensity:$designDensity")
        val targetDensity: Float = appDisplayMetrics.widthPixels / getDesignDensity()
        LogUtil.i("TAG", "targetDensity:$targetDensity")
        // 防止字体变小
        val targetScaleDensity: Float =
            targetDensity * (sNoncompatScaledDensity / sNoncompatDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaleDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        val activityDisplayMetrics = this.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaleDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    protected open fun getDesignDensity(): Float {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            375F
        } else {
            720f
        }
    }

    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11及以上：使用 WindowInsetsController API
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.apply {
                // 隐藏状态栏和导航栏
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // 设置手势滑动时短暂显示系统栏
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Android 11以下：使用旧的沉浸式全屏设置
            @Suppress("DEPRECATION")
            // 设置Activity为全屏模式
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
// 隐藏状态栏和导航栏
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        }
    }
}