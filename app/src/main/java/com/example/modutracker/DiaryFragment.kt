package com.example.modutracker

import Data.TodayData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modutracker.databinding.FragmentMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

class DiaryFragment:Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TodayDiaryAdapter

    //일기 조회 받아올 리스트
    val diaryData = mutableListOf<TodayData>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val textdiary = binding.editTextDiary
        val btnAnalyze = binding.btnAnalyze
        val textDate = binding.textDate

        //날짜
        val now = System.currentTimeMillis()
        val date = SimpleDateFormat("MMMM dd일").format(now)
        textDate.text = date

        getDiaryData("")
        initRecyclerView()


        //감정 분석 버튼
        btnAnalyze.setOnClickListener{
            var diary = textdiary?.text.toString()

            if(diary != null) {
                AnalyzeEmotion(diary)
            }
        }

        return binding.root
    }

    //감정 분석 - Clova Sentiment
    fun AnalyzeEmotion(diarytext : String) {

        val url = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze"

        val json = JSONObject()
        json.put("content", diarytext)

        val requestBody = json.toString().toRequestBody("application/json;charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .addHeader("X-NCP-APIGW-API-KEY-ID","r2g6zb12fz")
            .addHeader("X-NCP-APIGW-API-KEY","mcYRLy2wvNaJfqN7NpMqDmHWHG3ReyZyKN3S4rJw")
            .addHeader("Content-Type","application/json")
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "Success")

                var jsonObject = JSONObject(response.body?.string())
                var sentiment = jsonObject.getJSONObject("document").get("sentiment") as String
                var neutral = jsonObject.getJSONObject("document").getJSONObject("confidence").get("neutral") as Double
                var positive = jsonObject.getJSONObject("document").getJSONObject("confidence").get("positive") as Double
                var negative = jsonObject.getJSONObject("document").getJSONObject("confidence").get("negative") as Double

                binding.textText.text = sentiment
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", e.toString())
            }
        })
    }

    fun getDiaryData(jwt : String) {
        val url = "http://modutracker.shop/diary"

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization",jwt)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "Success")

//                var jsonArray = JSONArray(response.body?.string())

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", e.toString())
            }
        })
    }

    //리사이클러뷰에 어댑터 연결
    fun initRecyclerView(){
        val adapter = TodayDiaryAdapter()
        adapter.datalist = diaryData
        binding.diaryRecyclerview.adapter = adapter
        binding.diaryRecyclerview.layoutManager = LinearLayoutManager(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}