package com.soul.mediapicker.utils

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

}