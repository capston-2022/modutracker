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
import kotlinx.android.synthetic.main.item_todaydiary.*
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
    private lateinit var sentiment : String

    //일기 조회 받아올 리스트
    val diaryData = mutableListOf<TodayData>()

    val emotionData = mutableListOf<EmotionData>()

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
            var emotionIdx = 0
            //데이터 초기화
            diaryData.clear()

            if(diary != "") {
                CoroutineScope(Main).launch {
                    CoroutineScope(IO).async {
                        AnalyzeEmotion(diary)
                    }.await()

                    CoroutineScope(IO).async {
                        emotionIdx = SetEmotionColor()
                    }.await()


                    //Dialog
                    val dlg = AnalyzeDialog(requireContext())
                    dlg.setOnOKClickedListener {
                        CoroutineScope(Main).launch {
                            CoroutineScope(IO).async {
                                AddDiary(jwt, emotionIdx)
                                textdiary.text.clear()

                            }.await()

                            CoroutineScope(IO).async {
                                getDiaryData(jwt)
                            }.await()

                            initRecyclerView()
                        }
                    }
                    dlg.start("")
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

        val response : Response = client.newCall(request).execute()

        var jsonObject = JSONObject(response.body?.string())
        var document = jsonObject.getJSONObject("document")
        var sentences = jsonObject.getJSONArray("sentences")

        //데이터 초기화
        emotionData.clear()

        for (i in 0 until sentences.length()){
            var entry : JSONObject = sentences.getJSONObject(i)
            var tmp = EmotionData(
                entry.get("sentiment") as String,
                entry.getJSONObject("confidence").get("negative") as Double,
                entry.getJSONObject("confidence").get("neutral") as Double,
                entry.getJSONObject("confidence").get("positive") as Double
            )
            emotionData.add(tmp)
        }

        sentiment = document.get("sentiment") as String
        //var degree = document.getJSONObject("confidence").get(sentiment) as Double

    }

    //일기 데이터베이스에 입력
    fun AddDiary(jwt : String, emotionIdx : Int){
        val url = "http://modutracker.shop/diary"

        var formbody : RequestBody = FormBody.Builder()
            .add("content", editText_diary.text.toString())
            .add("emotionidx", emotionIdx.toString())
            .build()

        val request = Request.Builder()
            .addHeader("Authorization",jwt)
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        val response : Response = client.newCall(request).execute()

        Log.d("데이터 입력", response.body?.string().toString())
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

    //감정 색 지정
    private fun SetEmotionColor() : Int{
        var countNeg = 0
        var countPos = 0
        var countNeu = 0

        var emotionIdx = 0
        for (i in 0 until emotionData.size){
            when(emotionData[i].sentiment) {
                "negative" -> countNeg++
                "positive" -> countPos++
                "neutral" -> countNeu++
            }
        }

        if(countNeg - countPos <=  1 && countNeg - countPos >= -1 && countNeg != 0 && countPos != 0){
            emotionIdx = 4
        }
        else{
            emotionIdx = when(sentiment) {
                "positive" -> 1
                "neutral" -> 2
                "negative" -> 3
                else -> 0
            }
        }
        Log.d("감정분석", emotionIdx.toString())

        return emotionIdx
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}