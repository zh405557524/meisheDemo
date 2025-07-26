package com.fuyao.clip.module.handler

import android.graphics.PointF
import android.util.Range

object DynamicWaterMarkHandler {

    private const val leftTop = 0
    private const val rightTop = 1
    private const val rightBottom = 2
    private const val leftBottom = 3
    private const val leftPoint = 4
    private const val topPoint = 5
    private const val rightPoint = 6
    private const val bottomPoint = 7

    //获取左边起始点偏移量
    fun getNvsOriginPoint(resolutionWidth : Int ,
                        resolutionHeight: Int,
                        captionWidth : Float ,
                        captionHeight : Float): PointF {
        val x = - (resolutionWidth - captionWidth) / 2
        val y = 0f
        return PointF(x, y)
    }

    //获取左边点偏移量
    fun getNvsLeftPoint(resolutionWidth : Int ,
                        resolutionHeight: Int,
                        captionWidth : Float ,
                        captionHeight : Float): PointF {
        val x = - (resolutionWidth - captionWidth) / 2
        val y = (resolutionHeight.toFloat() - captionHeight) / 2
        return PointF(x, y)
    }

    //获取顶部点偏移量
    fun getNvsTopPoint(resolutionWidth : Int ,
                        resolutionHeight: Int,
                        captionWidth : Float ,
                        captionHeight : Float): PointF {
        val x = (resolutionWidth - captionWidth) / 2
        val y = (resolutionHeight.toFloat() - captionHeight) / 2
        return PointF(x, y)
    }

    //获取右边点偏移量
    fun getNvsRightPoint(resolutionWidth : Int ,
                       resolutionHeight: Int,
                       captionWidth : Float ,
                       captionHeight : Float): PointF {
        val x = (resolutionWidth.toFloat() - captionWidth) / 2
        val y = -(resolutionHeight.toFloat() - captionHeight) / 2
        return PointF(x, y)
    }

    //获取底部点偏移量
    fun getNvsBottomPoint(resolutionWidth : Int ,
                       resolutionHeight: Int,
                       captionWidth : Float ,
                       captionHeight : Float): PointF {
        val x = -(resolutionWidth.toFloat() - captionWidth) / 2
        val y = -(resolutionHeight.toFloat() - captionHeight) / 2
        return PointF(x, y)
    }


    //获取动态水印的实时位置
    fun getRealLocation(
        waterMarkWidth: Double,
        waterMarkHeight : Double,
        rootViewWidth : Double,
        rootViewHeight : Double,
        animDuration : Double,
        timestamp: Double
    ):Pair<Double ,Double>{
        val areaPosition = getPositionArea(animDuration , timestamp)
       return if(areaPosition > leftBottom){
            getPointPosition(areaPosition ,waterMarkWidth, waterMarkHeight, rootViewWidth, rootViewHeight)
        }
        else{
            getAreaRealPosition(areaPosition,waterMarkWidth, waterMarkHeight, rootViewWidth, rootViewHeight, animDuration, timestamp)
        }
    }

    private fun getAreaRealPosition(area : Int,
                                    waterMarkWidth: Double,
                                    waterMarkHeight : Double,
                                    rootViewWidth : Double,
                                    rootViewHeight : Double,
                                    animDuration : Double,
                                    timestamp: Double):Pair<Double ,Double>{
        var realX = 0.0
        var realY = 0.0
        val xTotal = (rootViewWidth - waterMarkWidth) / 2
        val yTotal = (rootViewHeight - waterMarkHeight) / 2
        val end = timestamp % animDuration
        //进度
        if(area == leftTop){
            val baseX = 0.0
            val baseY = (rootViewHeight - waterMarkHeight) / 2
            val progress = end / (animDuration / 4)
            realX = baseX + progress * xTotal
            realY = baseY - progress * yTotal
        }
        else if(area == rightTop){
            val baseX = (rootViewWidth - waterMarkWidth) / 2
            val baseY = 0.0
            val progress = (end  - animDuration / 4 ) / (animDuration / 4)
            realX = baseX + progress * xTotal
            realY = baseY + progress * yTotal
        }
        else if(area == rightBottom){
            val baseX = (rootViewWidth - waterMarkWidth)
            val baseY = (rootViewHeight - waterMarkHeight) / 2
            val progress = (end  - animDuration / 2 ) / (animDuration / 4)
            realX = baseX - progress * xTotal
            realY = baseY + progress * yTotal
        }
        else{
            val baseX = (rootViewWidth - waterMarkWidth) / 2
            val baseY = (rootViewHeight - waterMarkHeight)
            val progress = (end  - animDuration * 3 / 4 ) / (animDuration / 4)
            realX = baseX - progress * xTotal
            realY = baseY - progress * yTotal
        }
        return Pair(realX , realY)
    }

    //获取4个顶点坐标
    private fun getPointPosition(area : Int,
                                 waterMarkWidth: Double,
                                 waterMarkHeight : Double,
                                 rootViewWidth : Double,
                                 rootViewHeight : Double):Pair<Double ,Double>{
       return if(area == leftPoint){
            Pair(0.0 , (rootViewHeight - waterMarkHeight) / 2)
        }
        else if(area == topPoint){
           Pair((rootViewWidth - waterMarkWidth) / 2 , 0.0)
        }
        else if(area == rightPoint){
           Pair(rootViewWidth - waterMarkWidth , (rootViewHeight - waterMarkHeight) / 2)
        }
        else{
           Pair((rootViewWidth - waterMarkWidth) / 2 , rootViewHeight - waterMarkHeight)
        }
    }

    //获取在哪块位置
    private fun getPositionArea(animDuration : Double, timestamp: Double):Int{
        val dur = timestamp % animDuration
        if(dur == 0.0){
            return leftPoint
        }
        else if(dur == animDuration / 4){
            return topPoint
        }
        else if(dur == animDuration / 2){
            return rightPoint
        }
        else if(dur == animDuration * 3 / 4){
            return bottomPoint
        }
        else {
            return if(Range(0.0 , animDuration / 4).contains(dur)){
                leftTop
            } else if(Range(animDuration / 4 , animDuration / 2).contains(dur)){
                rightTop
            } else if(Range(animDuration / 2 , animDuration * 3 / 4).contains(dur)){
                rightBottom
            } else{
                leftBottom
            }
        }

    }
}