package com.example.modutracker

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_set_name.*

class SetNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_name)

        var getIntent = getIntent()
        var jwt = getIntent.getStringExtra("jwt")

        btn_next.setOnClickListener {
            if(editText_name.text.length >= 2){
                val builder = AlertDialog.Builder(this)
                builder.setMessage("'" +editText_name.text.toString() + "'이(가) 맞나요?")
                    .setPositiveButton("네",
                    DialogInterface.OnClickListener { dialog, id ->
                        var intent = Intent(this, SetGroupActivity::class.java)
                        intent.putExtra("jwt", jwt)
                        intent.putExtra("name", editText_name.text.toString())
                        startActivity(intent)
                    })
                    .setNegativeButton("아니요",
                    DialogInterface.OnClickListener{ dialog, id ->

                    })
                builder.show()
            }
            else{
                Toast.makeText(this, "두 글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}