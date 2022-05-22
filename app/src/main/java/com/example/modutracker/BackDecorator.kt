package com.example.modutracker

//import android.R

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlin.math.pow
import kotlin.math.sqrt

//날짜 뒤에 이미지 삽입
class BackDecorator (context : Activity, currentDay: CalendarDay) : DayViewDecorator {
    private var drawable: Drawable = context?.getDrawable(R.drawable.ic_baseline_favorite_24)!!
    private var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
       //view.setSelectionDrawable(drawable!!)
        val image = EmotionImage()
        view.setBackgroundDrawable(image)
    }

    fun EmotionImage() : Drawable{
        val width = 256
        val height = 256
        val bitmap : Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for(i in 0 until width){
            for(j in 0 until height){
                val sideX = (width/2 - 1 - i).toDouble()
                val sideY = (height/2 - 1 - j).toDouble()
                val distance = sideX.pow(2) + sideY.pow(2) //원의 중심과 점 사이의 거리의 제곱
                val radius = (width / 2).toDouble() - 20
                var color = 0
                if(distance <= (radius.pow(2))) //거리가 반지름의 길이보다 작으면(원 안에 있는 점이면)
                {
                    color = Color.BLUE
                }
                else //거리가 반지름의 길이보다 크면(원 밖의 점이면)
                {
                    color = Color.alpha(Color.TRANSPARENT)
                }
                bitmap.setPixel(i,j,color)
            }
        }

        val drawable = BitmapDrawable(Resources.getSystem(), bitmap)
        return drawable
    }

    init {
        // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_favorite_24)!!


    }
}
