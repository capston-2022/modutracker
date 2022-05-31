package com.example.modutracker

import Data.TodayData
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modutracker.databinding.ItemTodaydiaryBinding

class TodayDiaryAdapter :
RecyclerView.Adapter<TodayDiaryAdapter.ViewHolder>() {

    var datalist = mutableListOf<TodayData>()
    val color = listOf<ColorData>(ColorData("#FFEDAC","기분좋은"),ColorData("#BBF1C5","평범한"),ColorData("#AFE6E9","우울한"),ColorData("#C2C2C2","뒤죽박죽한") )

    inner class ViewHolder(private val binding : ItemTodaydiaryBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onBindViewHolder(holder: TodayDiaryAdapter.ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    //만들어진 뷰홀더 없을 때 레이아웃 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodayDiaryAdapter.ViewHolder {
        val binding = ItemTodaydiaryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }
}