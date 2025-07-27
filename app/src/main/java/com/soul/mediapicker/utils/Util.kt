package com.soul.mediapicker.utils

import android.graphics.PointF
import com.meicam.sdk.NvsCaption
import com.meicam.sdk.NvsTimelineCaption
import com.soul.mediapicker.manager.ParameterSettingValues
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.util.UUID
import kotlin.math.roundToInt

object Util {

    var Http_DisplayId = ""
    var Http_uid = ""

    fun getCustomHeight(width: Int, height: Int): Int {
        val settingValues = ParameterSettingValues.instance()
        val compileHeight = settingValues.compileVideoRes
        var resultHeight = 0
        if (width > height) {
            val result = (compileHeight * 1.0f / height * width).toInt()
            resultHeight = if (result > 3840) {
                (3840 * 1.0f / width * height).toInt()
            } else {
                compileHeight
            }
        } else if (width == height) {
            resultHeight = compileHeight
        } else {
            val result = (compileHeight * 1.0f / width * height).toInt()
            resultHeight = if (result > 3840) {
                3840
            } else {
                result
            }
        }
        if (resultHeight % 2 != 0) {
            resultHeight -= 1
        }
        return resultHeight
    }
    fun md5(url: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(url.toByteArray()))
            .toString(16).padStart(32, '0')
    }

    //获取字幕自动换行文本
    fun getAutoWrapWord(
        caption: NvsTimelineCaption,
        maxWidth: Float,
        source: String,
        curText: String,
        position: Int
    ): String {
        if (source.isEmpty()) {
            return ""
        }
        caption.text = source
        val firstVertices = caption.getCaptionBoundingVertices(NvsCaption.BOUNDING_TYPE_TEXT)
        val firstLeftTop: PointF = firstVertices[0]
        val firstRightBottom: PointF = firstVertices[2]
        val width = firstRightBottom.x - firstLeftTop.x
        if (width <= maxWidth) {
            return source
        } else {
            val ratio = width / maxWidth
            var maxNum = (source.length / ratio).toInt()
            if (maxNum == 0) {
                maxNum = 1
            }
            val lineNum = source.length / maxNum
            val end = source.length % maxNum
            var ret = ""
            for (index in 0 until lineNum) {
                ret += source.substring(index * maxNum, (index + 1) * maxNum)
                if (index < lineNum - 1) {
                    ret += "\n"
                }
            }
            if (end > 0) {
                ret += "\n${source.substring(source.length - end, source.length)}"
            }
            return clearOtherWrap(ret)
        }
    }


    //清除多余换行符
    private fun clearOtherWrap(data: String): String {
        if (data.contains("\n\n")) {
            val nData = data.replace("\n\n", "\n")
            return clearOtherWrap(nData)
        } else {
            return data
        }
    }


}