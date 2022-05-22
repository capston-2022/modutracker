package com.example.modutracker

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_group_code.*
import kotlinx.android.synthetic.main.activity_set_name.*

class GroupCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_code)

        var getIntent = getIntent()
        var jwt = getIntent.getStringExtra("jwt")
        var code = getIntent.getStringExtra("code")

        text_groupCode.text = code

        btn_complete.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("jwt", jwt)
            startActivity(intent)
        }

        //클립보드에 복사 버튼
        btn_copy.setOnClickListener {
            var clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("code", code)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


}