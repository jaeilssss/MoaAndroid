package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class MyNeederFragment : Fragment() {

    companion object {
        fun newInstance() = MyNeederFragment()
    }

    private lateinit var viewModel: MyNeederViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_needer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNeederViewModel::class.java)
        // TODO: Use the ViewModel
    }

}