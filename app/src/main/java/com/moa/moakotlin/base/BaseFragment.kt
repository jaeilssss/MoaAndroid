package com.moa.moakotlin.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.moa.moakotlin.MainActivity

abstract class BaseFragment() : Fragment() ,onBackPressedListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).backListener = this
    }

     fun ShowToast(context: Context,msg:String){
         Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
     }
}