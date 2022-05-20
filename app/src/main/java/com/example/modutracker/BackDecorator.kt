package com.example.calendarview

import android.R
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable

import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*


//캘린더 뒤 배경 삽입(아직 미완)

class BackDecorator (context: Activity?, currentDay: CalendarDay) : DayViewDecorator {
    private val drawable: Drawable?=context?.getDrawable(R.drawable.ic_dialog_email)!!
    var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        //view.setSelectionDrawable(drawable!!)
        if (drawable != null) {
            view.setBackgroundDrawable(drawable)
        }
        view.setDaysDisabled(true)
    }

    init {
        // You can set background for Decorator via drawable here
        //drawable = ContextCompat.getDrawable(context!!, R.drawable.checkbox_off_background)


    }
}
