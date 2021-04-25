package com.moa.moakotlin.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.moa.moakotlin.MainActivity

abstract class BaseFragment : Fragment() ,onBackPressedListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).backListener = this
    }
}