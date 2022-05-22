package com.example.modutracker

//import android.R

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
//날짜 뒤에 이미지 삽입
class BackDecorator (context : Activity, currentDay: CalendarDay) : DayViewDecorator {
    private var drawable: Drawable = context?.getDrawable(R.drawable.ic_baseline_favorite_24)!!
    private var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
       //view.setSelectionDrawable(drawable!!)
        view.setBackgroundDrawable(drawable)
    }


    init {
        // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_favorite_24)!!


    }
}
