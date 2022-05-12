package com.example.modutracker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(val context: CalendarFragment) : RecyclerView.Adapter<CalendarAdapter.ItemView>(){
    private val array=ArrayList<Long>()
    private var month=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ItemView{
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_day,parent,false)
    return ItemView(view)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        val calendar=Calendar.getInstance()
        calendar.timeInMillis=array.get(position)
        val month=calendar.get(Calendar.MONTH)

        if(this.month != month){
            holder.background.setBackgroundColor(Color.parseColor("44cccccc"))
        }else{
            holder.background.setBackgroundColor(Color.WHITE)
        }
        holder.textDay.setText(SimpleDateFormat("dd").format(calendar.time))


    }

    override fun getItemCount(): Int {
        return array.size
    }
    fun setList (array:ArrayList<Long>,month:Int){
        this.month=month
        this.array.clear()
        this.array.addAll(array)
        notifyDataSetChanged()
    }

    class ItemView(view: View) : RecyclerView.ViewHolder(view){

        val textDay= view.findViewById<TextView>(R.id.text_day)
        val background: ConstraintLayout=view.findViewById(R.id.background)
    }
}

