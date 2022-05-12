package com.example.modutracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.modutracker.databinding.FragmentCalendarBinding
import com.example.modutracker.databinding.FragmentInitialBinding
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment:Fragment(){

    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding

    private  var calendarAdapter:CalendarAdapter? = null
    private lateinit var textMonth:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        //val view=inflater.inflate(com.example.modutracker.R.layout.fragment_calendar, container,false)


        val binding:FragmentCalendarBinding=FragmentCalendarBinding.inflate(inflater,container,false)
        val calendarAdapter=CalendarAdapter(this)
        val calendar=Calendar.getInstance()

//recycler 안에 GRID!
        binding.recyclerView.layoutManager=GridLayoutManager(view?.context ,7)
        binding.recyclerView.adapter=calendarAdapter
        binding.textMonth.setText(SimpleDateFormat("yyyy.MM").format(calendar.time))
        binding.btnLeft.setOnClickListener{
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1)
            calendarShow(calendar)

        }
        binding.btnRight.setOnClickListener {
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1)
            calendarShow(calendar)
        }

        calendarShow(calendar)
        //Log.d("hhh",firstDay.toString()+"/"+lastDay.toShort())
        return binding.root
    }

    private fun calendarShow(calendar:Calendar){

        val firstDay=calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        val lastDay=calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val arrayDay=ArrayList<Long>()
        for(i:Int in firstDay..lastDay){
            calendar.set(Calendar.DAY_OF_MONTH,i)
            val dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK)
            if(i==1&&dayOfWeek>1){
                for(j:Int in 1..dayOfWeek-1){
                    val lastCalendar=Calendar.getInstance()
                    lastCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1)
                    val lastMonth_lastDay=
                        (lastCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)-(j-1))
                    lastCalendar.add(Calendar.DAY_OF_MONTH,lastMonth_lastDay)
                    arrayDay.add(lastCalendar.timeInMillis)
                    Collections.sort(arrayDay)
                }
            }
            Log.d("hhh",i.toString())
            arrayDay.add(calendar.timeInMillis)
        }
        calendarAdapter?.setList(arrayDay,calendar.get(Calendar.MONTH))
    }
}
