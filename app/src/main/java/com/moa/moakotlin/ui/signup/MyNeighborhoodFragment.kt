package com.moa.moakotlin.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.MyNeighborhoodFragmentBinding

class MyNeighborhoodFragment : Fragment() {

    companion object {
        fun newInstance() = MyNeighborhoodFragment()
    }

    private lateinit var viewModel: MyNeighborhoodViewModel

    private lateinit var binding : MyNeighborhoodFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<>()
        return inflater.inflate(R.layout.my_neighborhood_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNeighborhoodViewModel::class.java)
        // TODO: Use the ViewModel
    }

}