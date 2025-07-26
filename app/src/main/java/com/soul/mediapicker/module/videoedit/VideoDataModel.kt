package com.soul.mediapicker.module.videoedit

import android.os.Parcelable

/**
 * Description: 视频数据模型
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:07
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:07
 * UpdateRemark:
 */
data class VideoDataModel(
    val audioPath: String,
    val audioLocalPath: String, // 音频本地路径
    val videoList: List<VideoItem>,
    val subtitleList: List<CartoonTimelineItem>, // 字幕列表
    val audioDuration: Long,//单位毫秒
)

/**
 * Description: 视频项数据模型
 * Author: zhuMing
 * CreateDate: 2025/7/26 11:07
 * ProjectName: mediapicker
 * UpdateUser:
 * UpdateDate: 2025/7/26 11:07
 * UpdateRemark:
 */
data class VideoItem(
    val height: Int,
    val url: String,
    val localPath: String, // 视频本地路径
    val width: Int
)



data class CartoonTimelineItem(
    val id: String,
    var start: Double, // milliseconds
    var end: Double, // milliseconds
    var srt: Srt
)  {
    var isBody: Boolean = true
    fun copy(): CartoonTimelineItem {
        return CartoonTimelineItem(
            id,
            start,
            end,
            srt = srt.copy()
        )
    }
}

data class Srt(
    var start: Double?, // milliseconds
    var end: Double?, // milliseconds
    var text: String?,
    var richText: String?,
    var textWithBreak: String? = null, // 带换行的内容
    var speaker: String? = "-1",
    var recommandRuleName: String? = "",
    var wrapSrtContent: String? = "",   //自动换行字幕内容
)  {
    fun append(srt: Srt): Srt {
        return Srt(
            start, srt.end, "$text${srt.text}",
            "${richText ?: text}${srt.richText ?: srt.text}",
        )
    }

    fun split(text: String, index: Int): List<Srt> {
        val textLength = text.length
        if (textLength <= 0 || index >= textLength) {
            return listOf(this)
        }
        val s = start ?: 0.0
        val e = end ?: 0.0
        val mid = s + ((e - s) * index / text.length)
        return listOf(
            Srt(
                s, mid, text.substring(0, index), text.substring(0, index),
            ),
            Srt(
                mid, e, text.substring(index), text.substring(index),
            )
        )
    }

    fun split(index: Int): List<Srt> {
        val textLength = text?.length ?: 0
        if (textLength <= 0 || index >= textLength) {
            return listOf(this)
        }
        val s = start ?: 0.0
        val e = end ?: 0.0
        val mid = s + ((e - s) * index / text!!.length)
        return listOf(
            Srt(
                s, mid, text?.substring(0, index), text?.substring(0, index),
            ),
            Srt(
                mid, e, text?.substring(index), text?.substring(index),
            )
        )
    }

    fun newSrtList(textList: List<String>): List<Srt> {
        if (textList.isEmpty()) {
            return listOf(
                Srt(
                    start = start,
                    end = end,
                    text = "",
                    richText = ""
                )
            )
        }
        if (textList.size == 1) {
            return listOf(
                Srt(
                    start = start,
                    end = end,
                    text = textList.first(),
                    richText = textList.first()
                )
            )
        }
        val s = start ?: 0.0
        val e = end ?: 0.0
        var totalLength = 0
        for (text in textList) {
            totalLength += text.length
        }
        if (totalLength == 0) {
            return listOf(
                Srt(
                    start = start,
                    end = end,
                    text = "",
                    richText = ""
                )
            )
        }
        val d = (e - s) / totalLength
        val ret = mutableListOf<Srt>()
        var startTimestamp = s
        for (text in textList) {
            val endTimestamp = startTimestamp + text.length * d
            ret.add(
                Srt(
                    start = startTimestamp,
                    end = endTimestamp,
                    text = text,
                    richText = text,
                )
            )
            startTimestamp = endTimestamp
        }
        return ret
    }

    fun copy(): Srt {
        return Srt(
            start = start,
            end = end,
            text = text,
            richText = richText,
            textWithBreak = textWithBreak,
            speaker = speaker,
            recommandRuleName = recommandRuleName,
            wrapSrtContent = wrapSrtContent,
        )
    }

    fun copy(textWithBreak: String?): Srt {
        return Srt(
            start = start,
            end = end,
            text = text,
            richText = richText,
            textWithBreak
        )
    }
}