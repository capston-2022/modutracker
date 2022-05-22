package com.example.modutracker
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarview.MaxDecorator
import com.example.calendarview.SaturdayDecorateor
import com.example.calendarview.SundayDecorateor
import com.example.modutracker.databinding.FragmentCalendarBinding

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
class CalendarFragment:Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    //private lateinit var adapter: TodayDiaryAdapter

    //일기 조회 받아올 리스트
    //val diaryData = mutableListOf<TodayData>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        calendar()
        return binding.root

    }

    fun calendar(){
        val materialCalendar: MaterialCalendarView = binding.calendar
        //findViewById(R.id.calendar)

        var startTimeCalendar = Calendar.getInstance()
        var endTimeCalendar = Calendar.getInstance()

        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth =startTimeCalendar.get(Calendar.MONTH)
        val currentDay =startTimeCalendar.get(Calendar.DATE)

        //달력 설정

        materialCalendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY) //일 월 화 수 목 금 토
            .setMaximumDate(CalendarDay.from(currentYear,currentMonth,31))
            .commit()

        //최대날짜설정
        val enClendarDay = CalendarDay(
            endTimeCalendar.get(Calendar.YEAR),
            endTimeCalendar.get(Calendar.MONTH),
            endTimeCalendar.get(Calendar.DATE)
        )

        val maxDecorator = MaxDecorator(enClendarDay)
        val saturdayDecorateor = SaturdayDecorateor()
        val sundayDecorateor = SundayDecorateor()


        lateinit var widget: MaterialCalendarView
        val activity = getActivity() as Activity
        widget = binding.calendar as MaterialCalendarView

        // val mydate=CalendarDay.from(2022,  5,18) // year, month, date

        //val backDecorator = BackDecorator(activity,mydate)

        //뒤에 배경 넣을 날짜 리스트(1달 이전의 날짜에 표시 됨.. 이건 수정 해야할 것 같아요..)
        val calList = ArrayList<CalendarDay>()
        calList.add(CalendarDay.from(2022, 4, 2))
        calList.add(CalendarDay.from(2022, 4, 11))
        calList.add(CalendarDay.from(2022, 4, 12))
        calList.add(CalendarDay.from(2022, 4, 13))
        for (calDay in calList){
            // materialCalendar.addDecorators(backDecorator)
            widget.addDecorators(BackDecorator(activity,calDay))
        }
        //widget.addDecorators(BackDecorator(activity,mydate))
        materialCalendar.addDecorators(
            maxDecorator,
            saturdayDecorateor,
            sundayDecorateor
            //backDecorator
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}