package com.example.modutracker

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView


import com.example.modutracker.MyReceiver.Constant.Companion.NOTIFICATION_ID
import com.example.modutracker.databinding.ActivityNotificationBinding

import java.util.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // 뷰바인딩
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this,MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 토글버튼 활성화 시 알림을 생성하고 토스트 메세지로 출력
        binding.toggleButton.setOnCheckedChangeListener { _, check ->
            val toastMessage = if (check) {
                //val repeatInterval = AlarmManager.INTERVAL_DAY
                /*
                1. INTERVAL_FIFTEEN_MINUTES : 15분
                2. INTERVAL_HALF_HOUR : 30분
                3. INTERVAL_HOUR : 1시간
                4. INTERVAL_HALF_DAY : 12시간
                5. INTERVAL_DAY : 1일
                 */
//                val triggerTime = (SystemClock.elapsedRealtime()
//                        + repeatInterval)

                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 22)
                }
                alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                ) // setInexactRepeating : 반복 알림
                "매일 저녁 10시에 알림이 발생합니다."
            } else {
                alarmManager.cancel(pendingIntent)
                "알림 예약을 취소하였습니다."
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }


        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)



        }
    }
}