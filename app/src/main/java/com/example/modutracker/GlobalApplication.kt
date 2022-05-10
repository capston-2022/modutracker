package com.example.modutracker

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate(){
        super.onCreate()

        KakaoSdk.init(this, "3e273895ac7b942c248c30abd5030837")
    }
}