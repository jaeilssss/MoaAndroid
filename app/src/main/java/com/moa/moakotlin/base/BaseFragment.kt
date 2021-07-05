package com.moa.moakotlin.base

import android.app.Activity
import android.content.Context
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.moa.moakotlin.MainActivity

abstract class BaseFragment() : Fragment() ,onBackPressedListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).backListener = this
    }

     fun showToast(context: Context,msg:String){
         Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
     }


}