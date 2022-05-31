package com.example.modutracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.modutracker.ColorData
import com.example.modutracker.R

class AnalyzeDialog(content : Context) {
    private val dlg = Dialog(content)
    private lateinit var emotion : ImageView
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var textNotice : TextView
    private lateinit var listener : AnalyzeDialogClickedListener

    val color = listOf<ColorData>(ColorData("#FFEDAC","기분좋은"),ColorData("#BBF1C5","평범한"),ColorData("#AFE6E9","우울한"),ColorData("#C2C2C2","뒤죽박죽한") )

    fun start(content : Int){

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_analyze)
        dlg.setCancelable(true)

        //감정 색 설정
        emotion = dlg.findViewById(R.id.image_emotion)
        emotion.setColorFilter((Color.parseColor(color[content-1].code)))

        textNotice = dlg.findViewById(R.id.notice)
        textNotice.text = "나의 감정은, " + color[content-1].emotion

        btnOK = dlg.findViewById(R.id.positive_btn)

        btnOK.setOnClickListener {
            listener.onOKClicked(content.toString())
            dlg.dismiss()
        }

        btnCancel = dlg.findViewById(R.id.negative_btn)
        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    fun setOnOKClickedListener(listener : (String) -> Unit) {
        this.listener = object : AnalyzeDialogClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }
    interface AnalyzeDialogClickedListener {
        fun onOKClicked(content : String)
    }
}