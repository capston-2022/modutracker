package com.example.modutracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }


}