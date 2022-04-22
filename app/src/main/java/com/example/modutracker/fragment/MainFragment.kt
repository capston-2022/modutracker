package com.example.modutracker.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.modutracker.databinding.FragmentMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

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
                Log.d("요청", "Fail")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}