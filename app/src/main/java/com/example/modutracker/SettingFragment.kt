package com.example.modutracker

import Data.TodayData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.modutracker.databinding.FragmentSettingBinding
import kotlinx.android.synthetic.main.activity_set_name.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat

class SettingFragment(private var jwt : String):Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private lateinit var name : String
    private lateinit var code : String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_setting, container,false)
        // 뷰바인딩
        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.btnPush.setOnClickListener {
            var intent = Intent(this.context, NotificationActivity::class.java)
            intent.putExtra("jwt", jwt)
            startActivity(intent)
        }
        CoroutineScope(Main).launch {
            CoroutineScope(IO).async {
                getInfo(jwt)
            }.await()

            binding.nickname.text = name
            binding.groupCode.text = code
        }


        return binding.root
    }

    private fun getInfo(jwt : String) {
        val url = "http://modutracker.shop/user/info"

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization",jwt)
            .build()

        val client = OkHttpClient()

        val response : Response = client.newCall(request).execute()

        var jsonObject = JSONObject(response.body?.string())

        name = jsonObject.getJSONArray("data").getJSONObject(0).getString("name")
        code = jsonObject.getJSONArray("data").getJSONObject(0).getString("groupcode")
    }
}