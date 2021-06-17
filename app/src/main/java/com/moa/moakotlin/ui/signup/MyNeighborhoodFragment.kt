package com.moa.moakotlin.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.MyNeighborhoodFragmentBinding
import com.moa.moakotlin.recyclerview.apt.MyNeighborhoodListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyNeighborhoodFragment : Fragment() {

    private lateinit var viewModel: MyNeighborhoodViewModel

    private lateinit var binding : MyNeighborhoodFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.my_neighborhood_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNeighborhoodViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel


        settingMyNeighborhood()

    }

    private fun settingMyNeighborhood(){
        var list = aptList.getInstance().aroundApt
        println("${list.size}+dddd))!!")
        var test = ArrayList<String>()

        CoroutineScope(Dispatchers.Main).launch {
            var AptNameList = viewModel.getMyAroundNeighborhood(list)

            var adapter = MyNeighborhoodListAdapter()
            println("여기 사이즈 몇일까?? ${AptNameList.size}")
            adapter.submitList(AptNameList)

            binding.myNeighborhoodRcv.adapter = adapter

            binding.myNeighborhoodRcv.layoutManager = LinearLayoutManager(context)

        }
    }

}