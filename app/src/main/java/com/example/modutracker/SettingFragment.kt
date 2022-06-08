package com.example.modutracker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.modutracker.databinding.FragmentSettingBinding

class SettingFragment:Fragment() {
    private lateinit var binding : FragmentSettingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_setting, container,false)
        // 뷰바인딩
        binding = FragmentSettingBinding.inflate(layoutInflater)

        //알림 설정 버튼 클릭
        binding.btnPush.setOnClickListener(View.OnClickListener {

            requireActivity().run{
                startActivity(Intent(this, NotificationActivity::class.java))
                finish()
            }
        })
        return binding.root
    }
}