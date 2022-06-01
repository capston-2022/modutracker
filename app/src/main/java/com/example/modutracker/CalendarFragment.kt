package com.example.modutracker
import Data.TodayData
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
class CalendarFragment(private var jwt : String):Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    val calData = mutableListOf<CalendarData>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        CoroutineScope(Main).launch {
            CoroutineScope(IO).async {
                getMoodTracker(jwt)
            }.await()
            calendar()
        }
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
//        val calList = ArrayList<CalendarDay>()
//        for (data in calData) {
//            val year = data.date.substring(0,4).toInt()
//            val month = data.date.substring(5,7).toInt() - 1
//            val day = data.date.substring(8,10).toInt()
//            calList.add(CalendarDay.from(year, month, day))
//        }
//        calList.add(CalendarDay.from(2022, 4, 2))
//        calList.add(CalendarDay.from(2022, 4, 11))
//        calList.add(CalendarDay.from(2022, 4, 12))
//        calList.add(CalendarDay.from(2022, 4, 13))




        val calGroup = calData.groupBy{it.date}
        val calList = mutableListOf<CalendarData>()
        for (day in calGroup){
            var emotionStr = ""
            for(value in day.value){
                emotionStr += value.emotion.toString()
            }
            calList.add(CalendarData(
                day.key,
                emotionStr.toInt()
                ))
        }
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

    private fun getMoodTracker(jwt : String){
        val url = "http://modutracker.shop/diary/calendar"

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization",jwt)
            .build()

        val client = OkHttpClient()

        val response : Response = client.newCall(request).execute()

        var jsonObject = JSONObject(response.body?.string())

        var jsonArray = jsonObject.getJSONArray("data")
        for(i in 0 until jsonArray.length()){
            val entry : JSONObject = jsonArray.getJSONObject(i)
            var date = entry.get("datedata") as String
            val year = date.substring(0,4).toInt()
            val month = date.substring(5,7).toInt() - 1
            val day = date.substring(8,10).toInt()
            var tmp = CalendarData(
                CalendarDay.from(year, month, day),
                entry.get("emotionidx") as Int,
            )
            calData.add(tmp)
        }
        Log.d("데이터 조회", jsonArray.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}