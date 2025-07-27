package com.soul.mediapicker.bean

import android.graphics.PointF
import android.os.Parcelable
import com.soul.mediapicker.SrtPositionType

data class SrtConfig(
    var position: SrtPosition,
    var color: String = "#FFFFFF",
    var fontSize: Int = 61,
    var fontInfo: FontInfo? = null,
    var borderColor: String? = "#000000",
    var backgroundColor: String? = null,
    var shadowColor: String? = null,
    var shadowOffset: PointF? = null,
    var styleIndex: Int? = null,
    var borderWidth: Int? = 4,
    var alpha: Float = 1f,//取值 0~1f,默认不设置透明度
)

data class FontInfo(
    val name: String?,
    val url: String?,
    val isDefault: Boolean = false
)


fun srtDefaultByHeight(height: Float, fontInfo: FontInfo? = null): SrtConfig {
    return SrtConfig(
        position = defaultByHeight(height),
        color = "#ffffff",
        fontSize = 61,
    )
}

fun defaultByHeight(height: Float): SrtPosition {
    return SrtPosition(
        433f,
        height * 5 / 6,
        isMidX = true,
        isMidY = false,
        SrtPositionType.custom
    )
}

data class SrtPosition(
    var x: Float,
    var y: Float,
    var isMidX: Boolean,
    var isMidY: Boolean,
    var type: Int,
)


data class ScaleAndRotateBean(
    var assetAnchor: PointF?,
    var scaleFactor: List<Float>?,
    var angle: Float?,
    var translateCaptionPf: PointF?,
    var isVertical: Boolean = false//是否是竖屏
) {
    fun copy(): ScaleAndRotateBean {
        val tempScaleFactor = mutableListOf<Float>()
        scaleFactor?.forEach {
            tempScaleFactor.add(it)
        }

        return ScaleAndRotateBean(
            assetAnchor = PointF(assetAnchor?.x ?: 0f, assetAnchor?.y ?: 0f),
            scaleFactor = tempScaleFactor,
            angle = angle,
            translateCaptionPf = PointF(translateCaptionPf?.x ?: 0f, translateCaptionPf?.y ?: 0f),
            isVertical = isVertical
        )
    }
}
