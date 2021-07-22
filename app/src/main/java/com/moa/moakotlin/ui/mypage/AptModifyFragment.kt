package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class AptModifyFragment : Fragment() {

    companion object {
        fun newInstance() = AptModifyFragment()
    }

    private lateinit var viewModel: AptModifyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.apt_modify_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifyViewModel::class.java)

    }

}