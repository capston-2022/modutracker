package com.example.modutracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Window
import android.widget.Button
import com.example.modutracker.R

class AnalyzeDialog(content : Context) {
    private val dlg = Dialog(content)
    private lateinit var emotion : Drawable
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var listener : AnalyzeDialogClickedListener

    fun start(content : String){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_analyze)
        dlg.setCancelable(true)

        btnOK = dlg.findViewById(R.id.positive_btn)
        btnOK.setOnClickListener {
            listener.onOKClicked("")
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