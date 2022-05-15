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
import com.example.modutracker.dialog.AnalyzeDialog
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

class DiaryFragment(private var jwt : String):Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

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

        Log.d("받아온 jwt값 ", jwt )

        //날짜
        val now = System.currentTimeMillis()
        val date = SimpleDateFormat("MMMM dd").format(now)
        textDate.text = date

        //비동기 처리
        CoroutineScope(Main).launch {
            CoroutineScope(IO).async {
                getDiaryData(jwt)
            }.await()

            initRecyclerView()
        }

        //감정 분석 버튼
        btnAnalyze.setOnClickListener{
            var diary = textdiary?.text.toString()

            if(diary != null) {
                CoroutineScope(Main).launch {
                    CoroutineScope(IO).async {
                        //AnalyzeEmotion(diary)
                    }.await()

                    //Dialog
                    /*
                    val dlg = AnalyzeDialog(requireContext())
                    dlg.setOnOKClickedListener {

                    }
                    dlg.start("")
                     */

                    AddDiary(jwt)
                }
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

    //일기 데이터베이스에 입력
    fun AddDiary(jwt : String){
        val url = "http://modutracker.shop/diary"

        var formbody : RequestBody = FormBody.Builder()
            .add("content", editText_diary.text.toString())
            .add("emotionidx", "1")
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "Success")
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", e.toString())
                e.printStackTrace()
            }
        })
    }

    //일기 조회
    private fun getDiaryData(jwt : String){
        val url = "http://modutracker.shop/diary"

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
        val adapter = TodayDiaryAdapter()
        adapter.datalist = diaryData
        if(adapter.datalist.size == 0){
            Log.d("recyclerview", "data is null")
        }
        else{
            binding.diaryRecyclerview.adapter = adapter
            binding.diaryRecyclerview.layoutManager = LinearLayoutManager(activity)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}