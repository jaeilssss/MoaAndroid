package com.moa.moakotlin.custom

import android.app.AlertDialog
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.moa.moakotlin.R

class PrivateNoticeDialog(private val context : Context){

    private val builder : AlertDialog.Builder by lazy{
        AlertDialog.Builder(context).setView(view)
    }

    private val view : View by lazy{
        View.inflate(context, R.layout.private_single_positive_dialog,null)
    }
    private var dialog : AlertDialog?  =null
    //터치 리스너 구현

    private val onTouchListener = View.OnTouchListener{_ ,motionEvent->
        if(motionEvent.action==MotionEvent.ACTION_UP){
            android.os.Handler().postDelayed({
                dismiss()

            },3)
        }
        false
    }


    fun setMessage(message : CharSequence) : PrivateNoticeDialog{
        view.findViewById<TextView>(R.id.singlePositiveMessage).text = message
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): PrivateNoticeDialog {
        view.findViewById<Button>(R.id.dialogPositiveBtn).apply {
            setOnClickListener(listener)

            setOnTouchListener(onTouchListener)
        }
        return this
    }


    fun show() {
        dialog = builder.create()
        dialog?.show()
    }
    private fun dismiss() {
        dialog?.dismiss()
    }

}
