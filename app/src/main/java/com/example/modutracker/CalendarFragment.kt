package com.example.modutracker
import Data.TodayData
import android.annotation.SuppressLint
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
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
class CalendarFragment(private var jwt : String):Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    val calData = mutableListOf<CalendarData>()
    val diaryData = mutableListOf<TodayData>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            binding.today.text = (date.month + 1).toString() + "월 " + date.day.toString() + "일"
            CoroutineScope(Main).launch {
                CoroutineScope(IO).async {
                    val year = date.year.toString()
                    var month = ""
                    var day = ""
                    if (date.month + 1 >= 10) {
                        month = (date.month + 1).toString()
                    }
                    else{
                        month = "0" + (date.month + 1).toString()
                    }
                    if (date.day >= 10) {
                        day = date.day.toString()
                    }
                    else{
                        day = "0" + date.day.toString()
                    }

                    val dateStr = "$year-$month-$day"
                    diaryData.clear()
                    getDiaryData(jwt, dateStr)
                }.await()
                initRecyclerView()
            }


        }



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

    //일기 조회
    private fun getDiaryData(jwt : String, date : String){

        diaryData.clear()

        val url = "http://modutracker.shop/diary/inquire"

        var formbody : RequestBody = FormBody.Builder()
            .add("date",date)
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization",jwt)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        val response : Response = client.newCall(request).execute()

        var jsonObject = JSONObject(response.body?.string())

        var jsonArray = jsonObject.getJSONArray("data")
        for(i in 0 until jsonArray.length()){
            val entry : JSONObject = jsonArray.getJSONObject(i)
            var tmp = TodayData(
                entry.get("name") as String,
                entry.get("emotionidx") as Int,
                entry.get("content") as String
            )
            diaryData.add(tmp)
        }
        Log.d("데이터 조회", jsonArray.toString())
    }

    //리사이클러뷰에 어댑터 연결
    private fun initRecyclerView(){
        val adapter = CalendarDiaryAdapter()
        adapter.datalist = diaryData
        binding.diaryRecyclerview.adapter = adapter
        binding.diaryRecyclerview.layoutManager = LinearLayoutManager(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}