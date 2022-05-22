package com.example.modutracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_set_group.*
import kotlinx.android.synthetic.main.activity_set_name.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.Normalizer
import java.util.concurrent.TimeUnit

class SetGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_group)

        //SetNameActivity 에서 값 받아오기
        var getIntent = getIntent()
        var jwt = getIntent.getStringExtra("jwt")
        var name = getIntent.getStringExtra("name")

        //새그룹 생성 버튼
        btn_newGroup.setOnClickListener {
            var code = RandomString()

            AddGroup(name!!, code, jwt!!)
        }

        //그룹 참가 버튼
        btn_joinGroup.setOnClickListener {
            var intent = Intent(this, JoinGroupActivity::class.java)
            intent.putExtra("jwt", jwt)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    //랜덤한 문자열 생성(ex. TU3af)
    fun RandomString() : String {
        var charPool :  List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..5)
            .map{kotlin.random.Random.nextInt(0,charPool.size)}
            .map(charPool::get)
            .joinToString ("")
    }

    //새 그룹 추가
    fun AddGroup(name : String, code : String, jwt : String){
        val url = "http://modutracker.shop/group"

        var formbody : RequestBody = FormBody.Builder()
            .add("groupcode", code)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "Add group Success")

                SignUp(jwt, name, code)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", e.toString())
                e.printStackTrace()
            }
        })
    }

    //회원가입
    fun SignUp(jwt : String, name : String, code : String){

        var intent = Intent(this, GroupCodeActivity::class.java)

        val url = "http://modutracker.shop/user"

        var formbody : RequestBody = FormBody.Builder()
            .add("name", name)
            .build()

        val request = Request.Builder()
            .addHeader("Authorization",jwt)
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "Sign up Success")

                intent.putExtra("jwt", jwt)
                intent.putExtra("code", code)
                startActivity(intent)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", e.toString())
                e.printStackTrace()
            }
        })
    }
}