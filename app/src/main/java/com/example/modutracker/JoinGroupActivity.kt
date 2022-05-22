package com.example.modutracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_join_group.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class JoinGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        var getIntent = getIntent()
        var jwt = getIntent.getStringExtra("jwt").toString()
        var name = getIntent.getStringExtra("name").toString()

        var result = 0

        btn_complete.setOnClickListener {
            CoroutineScope(Main).launch {
                CoroutineScope(IO).async {
                    result = SignUp(jwt, name, editText_code.text.toString())
                }.await()

                when(result) {
                    300 -> Toast.makeText(this@JoinGroupActivity, "유효하지 않은 코드입니다.", Toast.LENGTH_SHORT).show()
                    200 -> { Toast.makeText(this@JoinGroupActivity, "회원가입 성공", Toast.LENGTH_LONG).show()
                        var intent = Intent(this@JoinGroupActivity, MainActivity::class.java)
                        intent.putExtra("jwt", jwt)
                        startActivity(intent)
                    }
                    0 -> Toast.makeText(this@JoinGroupActivity, "결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this@JoinGroupActivity, "알 수 없는 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //회원가입 - 그룹참가
    fun SignUp(jwt : String, name : String, code : String) : Int{
        var intent = Intent(this, GroupCodeActivity::class.java)

        val url = "http://modutracker.shop/user"

        var formbody : RequestBody = FormBody.Builder()
            .add("name", name)
            .add("groupcode", code)
            .build()

        val request = Request.Builder()
            .addHeader("Authorization",jwt)
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        val response : Response = client.newCall(request).execute()

        var jsonObject = JSONObject(response.body?.string())

        return jsonObject.getInt("code")
    }
}