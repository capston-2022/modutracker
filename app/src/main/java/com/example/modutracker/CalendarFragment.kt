package com.example.modutracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendarview.MaxDecorator
import com.example.calendarview.SaturdayDecorateor
import com.example.calendarview.SundayDecorateor
import com.example.modutracker.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*


class CalendarFragment: Fragment() {
    private var _binding: FragmentCalendarBinding? = null
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

    fun calendar() {
        val materialCalendar: MaterialCalendarView = binding.calendar
        //findViewById(R.id.calendar)

        var startTimeCalendar = Calendar.getInstance()
        var endTimeCalendar = Calendar.getInstance()

        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDay = startTimeCalendar.get(Calendar.DATE)

        //달력 설정

        materialCalendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY) //일 월 화 수 목 금 토
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth, 31))
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


        //캘린더 배경 이미지 삽입(아직 미완)
//        lateinit var widget: MaterialCalendarView
//        val activity = getActivity() as MainActivity
//        widget = view.findViewById(R.id.calendarView) as MaterialCalendarView

//        val mydate= CalendarDay.from(2022,  5, 15)
//        val backDecorator = BackDecorator(activity,mydate)


        materialCalendar.addDecorators(
            maxDecorator,
            saturdayDecorateor,
            sundayDecorateor,
            // backDecorator
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
