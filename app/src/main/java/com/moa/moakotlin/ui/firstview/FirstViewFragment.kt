package com.moa.moakotlin.ui.firstview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentFirstViewBinding


class FirstViewFragment : Fragment() {
    lateinit var binding: FragmentFirstViewBinding

    lateinit var viewModel: FirstViewViewModel

    lateinit var navController: NavController
    var images = intArrayOf(
        R.drawable.moa_temp_login,
        R.drawable.moa_temp_login2,
        R.drawable.moa_temp_login3
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_first_view,container,false)
        binding.carousview.pageCount = images.size
        navController =  findNavController()
        viewModel = context?.let {FirstViewViewModel(navController,it)}!!
        binding.model = viewModel
        binding.carousview.setImageListener{position, imageView -> imageView.setImageResource(images[position]) }
     return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//      navController =  Navigation.findNavController(view)
//        viewModel = FirstViewViewModel(navController)
//        binding.model = viewModel

    }


}