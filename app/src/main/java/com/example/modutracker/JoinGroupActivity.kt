package com.example.modutracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_join_group.*
import okhttp3.*
import java.io.IOException

class JoinGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        var code = editText_code.text
    }

    //회원가입
    fun SignUp(jwt : String, name : String, code : String){

        var intent = Intent(this, GroupCodeActivity::class.java)

        val url = "http://modutracker.shop/user"

        var formbody : RequestBody = FormBody.Builder()
            .add("accesstoken", jwt)
            .add("name", name)
            .add("groupcode", code)
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
}