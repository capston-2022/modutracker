package com.example.modutracker

//import android.R

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlin.math.pow
import kotlin.math.sqrt

//날짜 뒤에 이미지 삽입
class BackDecorator (context : Activity, currentDay: CalendarData) : DayViewDecorator {
    val color = listOf<ColorData>(ColorData("#FFEDAC","기분좋은"),
        ColorData("#BBF1C5","평범한"),
        ColorData("#AFE6E9","우울한"),
        ColorData("#FFCF83","뒤죽박죽한"),
        ColorData("#FFA4A4","화난"),
        ColorData("#D4CEFA","설레는") )
    private var drawable: Drawable = context?.getDrawable(R.drawable.ic_baseline_favorite_24)!!
    private var myDay = currentDay.date
    private var colorIdx = currentDay.emotion.toString()
    val colorList = mutableListOf<String>()
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
       //view.setSelectionDrawable(drawable!!)
        setColor()
        val image = EmotionImage()
        view.setBackgroundDrawable(image)
    }

    fun EmotionImage(): Drawable {
        //이미지 크기
        val width = 256
        val height = 256

        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (i in 0 until width) {
            for (j in 0 until height) {
                //그림 크기
                val margin = 40
                val imgWidth = (width - margin * 2).toDouble()
                val imgHeight = (height - margin * 2).toDouble()

                //반지름
                val radius = imgWidth * sqrt(2.0) / (1 + sqrt(2.0)) / 2

                //원1
                val center1 = radius + margin //원의 중심

                val sideX1 = (center1 - 1 - i)
                val sideY1 = (center1 - 1 - j)
                val distance1 = sideX1.pow(2) + sideY1.pow(2) //원의 중심과 점 사이의 거리의 제곱
                var color = 0
                if (distance1 <= (radius.pow(2))) //거리가 반지름의 길이보다 작으면(원 안에 있는 점이면)
                {
                    color = Color.parseColor(colorList[0])
                }

                //원2
                val centerX2 = imgWidth - radius + margin//원의 중심 x좌표
                val centerY2 = radius + margin //원의 중심 y좌표

                val sideX2 = (centerX2 - 1 - i)
                val sideY2 = (centerY2 - 1 - j)
                val distance2 = sideX2.pow(2) + sideY2.pow(2) //원의 중심과 점 사이의 거리의 제곱
                if (distance2 <= (radius.pow(2))) //거리가 반지름의 길이보다 작으면(원 안에 있는 점이면)
                {
                    color = Color.parseColor(colorList[1])
                }

                //원4
                val centerX4 = imgWidth - radius + margin//원의 중심 x좌표
                val centerY4 = imgWidth - radius + margin //원의 중심 y좌표

                val sideX4 = (centerX4 - 1 - i)
                val sideY4 = (centerY4 - 1 - j)
                val distance4 = sideX4.pow(2) + sideY4.pow(2) //원의 중심과 점 사이의 거리의 제곱
                if (distance4 <= (radius.pow(2))) //거리가 반지름의 길이보다 작으면(원 안에 있는 점이면)
                {
                    color = Color.parseColor(colorList[2])
                }

                //원3
                val centerX3 = radius + margin//원의 중심 x좌표
                val centerY3 = imgWidth - radius + margin //원의 중심 y좌표

                val sideX3 = (centerX3 - 1 - i)
                val sideY3 = (centerY3 - 1 - j)
                val distance3 = sideX3.pow(2) + sideY3.pow(2) //원의 중심과 점 사이의 거리의 제곱
                if (distance3 <= (radius.pow(2))) //거리가 반지름의 길이보다 작으면(원 안에 있는 점이면)
                {
                    color = Color.parseColor(colorList[3])
                    //color = Color.BLUE
                }

                if (distance1 <= (radius.pow(2)) && distance3 <= (radius.pow(2))) {
                    color = Color.parseColor(colorList[0])
                }

                bitmap.setPixel(i, j, color)
            }
        }

        return BitmapDrawable(Resources.getSystem(), bitmap)
    }

    fun setColor() {
        when(colorIdx.length) {
            4 ->{
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[1].toString().toInt()-1].code)
                colorList.add(color[colorIdx[2].toString().toInt()-1].code)
                colorList.add(color[colorIdx[3].toString().toInt()-1].code)
            }
            3 ->{
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[1].toString().toInt()-1].code)
                colorList.add(color[colorIdx[2].toString().toInt()-1].code)
            }
            2 ->{
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[1].toString().toInt()-1].code)
                colorList.add(color[colorIdx[1].toString().toInt()-1].code)
            }
            1 ->{
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
            }
            else ->{
                colorList.add(color[colorIdx[0].toString().toInt()-1].code)
                colorList.add(color[colorIdx[1].toString().toInt()-1].code)
                colorList.add(color[colorIdx[2].toString().toInt()-1].code)
                colorList.add(color[colorIdx[3].toString().toInt()-1].code)
            }
        }
    }
    init {
        // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_favorite_24)!!


    }
}
