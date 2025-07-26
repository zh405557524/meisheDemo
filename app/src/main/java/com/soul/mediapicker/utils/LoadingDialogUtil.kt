package com.soul.mediapicker.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.soul.mediapicker.R

/**
 * Description: 加载对话框工具类
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:07
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:07
 * UpdateRemark:
 */
object LoadingDialogUtil {

    private var loadingDialog: Dialog? = null

    /**
     * 显示加载对话框
     */
    fun showLoading(context: Context, message: String = "加载中...") {
        if (loadingDialog?.isShowing == true) {
            return
        }


        loadingDialog = Dialog(context, R.style.LoadingDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        val messageTextView = view.findViewById<TextView>(R.id.tv_loading_message)
        messageTextView.text = message

        loadingDialog?.apply {
            setContentView(view)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    fun setText(text: String) {
        loadingDialog?.findViewById<TextView>(R.id.tv_loading_message)?.post {
            loadingDialog?.findViewById<TextView>(R.id.tv_loading_message)?.text = text
        }
    }

    /**
     * 隐藏加载对话框
     */
    fun hideLoading() {
        loadingDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        loadingDialog = null
    }
} 