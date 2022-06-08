package com.example.modutracker

import Data.TodayData
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modutracker.databinding.ItemCalendardiaryBinding
import com.example.modutracker.databinding.ItemTodaydiaryBinding

class CalendarDiaryAdapter :
    RecyclerView.Adapter<CalendarDiaryAdapter.ViewHolder>() {

    var datalist = mutableListOf<TodayData>()
    val color = listOf<ColorData>(ColorData("#FFEDAC","기분좋은"),
        ColorData("#BBF1C5","평범한"),
        ColorData("#AFE6E9","우울한"),
        ColorData("#C2C2C2","뒤죽박죽한"),
        ColorData("#FE7878","화난"),
        ColorData("#D4CEFA","설레는") )

    inner class ViewHolder(private val binding : ItemCalendardiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diaryData : TodayData){
            binding.nameText.text = diaryData.name
            var emotionidx = diaryData.emotionidx
            binding.emotion.setColorFilter(Color.parseColor(color[emotionidx-1].code))
            binding.contentText.text = diaryData.content
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: CalendarDiaryAdapter.ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    //만들어진 뷰홀더 없을 때 레이아웃 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarDiaryAdapter.ViewHolder {
        val binding = ItemCalendardiaryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }
}