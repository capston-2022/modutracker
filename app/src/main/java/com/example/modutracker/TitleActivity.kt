package com.example.modutracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause.*
import kotlinx.android.synthetic.main.activity_title.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.kakao.sdk.common.util.Utility


class TitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        //kakao 로그인
        var keyHash = Utility.getKeyHash(this)

        Log.d("Hash", keyHash)
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                CheckMember(token.accessToken)
                println(token.accessToken)
            }
        }
        btn_login.setOnClickListener{
            if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            }
            else{
                LoginClient.instance.loginWithKakaoAccount(this, callback=callback)
            }
        }
    }

    //회원인지 아닌지 확인

    fun CheckMember(accesstoken : String) {

        var signInIntent = Intent(this, SetNameActivity::class.java)
        var signUpIntent = Intent(this, MainActivity::class.java)

        val url = "http://modutracker.shop/user/member"
        var formbody : RequestBody = FormBody.Builder()
            .add("accesstoken", accesstoken)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "Success")
                var jsonObject = JSONObject(response.body?.string())

                //멤버 유무 값, 1이면 멤버 0이면 회원가입 필요
                var result = jsonObject.getJSONObject("data").getInt("result")
                var jwt = jsonObject.getJSONObject("data").getString("jwt")

                println("result 값 : $result" );
                println("jwt 값 : $jwt" );

                if(result == 1){
                    signUpIntent.putExtra("jwt", jwt)
                    startActivity(signUpIntent)
                }
                else if(result == 0){
                    signInIntent.putExtra("jwt", jwt)
                    startActivity(signInIntent)
                }
                else{
                    println("잘못된 result 값" );
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", e.toString())
                e.printStackTrace()
            }
        })
    }
}
