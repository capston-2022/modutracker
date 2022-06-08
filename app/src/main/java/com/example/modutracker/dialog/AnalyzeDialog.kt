package com.example.modutracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import android.widget.*
import com.example.modutracker.ColorData
import com.example.modutracker.MainActivity
import com.example.modutracker.R

class AnalyzeDialog(content : Context) {
    private val dlg = Dialog(content)
    private lateinit var emotion : ImageView
    private lateinit var btnOK : ImageButton
    private lateinit var btnCancel : ImageButton
    private lateinit var listener : AnalyzeDialogClickedListener
    private lateinit var spinner : Spinner

    val color = listOf<ColorData>(ColorData("#FFEDAC","기분좋은"),
        ColorData("#BBF1C5","평범한"),
        ColorData("#AFE6E9","우울한"),
        ColorData("#C2C2C2","뒤죽박죽한"),
        ColorData("#FE7878","화난"),
        ColorData("#D4CEFA","설레는") )

    fun start(content : Int){

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_analyze)
        dlg.setCancelable(true)

        //감정 색 설정
        emotion = dlg.findViewById(R.id.image_emotion)
        emotion.setColorFilter((Color.parseColor(color[content-1].code)))

        //spinner 연결
        val emtList = listOf<String>("기분좋은","평범한","우울한","뒤죽박죽한","화난","설레는")
        spinner = dlg.findViewById(R.id.spinner)
        spinner.adapter = ArrayAdapter(dlg.context, android.R.layout.simple_spinner_item, emtList)

        var selected = 0
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selected = position + 1
                emotion.setColorFilter((Color.parseColor(color[position].code)))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        btnOK = dlg.findViewById(R.id.positive_btn)

        btnOK.setOnClickListener {
            listener.onOKClicked(selected.toString())
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