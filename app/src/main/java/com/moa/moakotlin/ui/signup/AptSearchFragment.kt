package com.moa.moakotlin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.FragmentAptSearchBinding
import com.moa.moakotlin.recyclerview.algoria.SearchAptAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AptSearchFragment : BaseFragment() {

lateinit var binding : FragmentAptSearchBinding

lateinit var navController: NavController

lateinit var model : AptSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_apt_search,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(AptSearchViewModel::class.java)

        binding.model = model

        var adapter = SearchAptAdapter()

        (context as MainActivity).backListener = this
        binding.back.setOnClickListener { navController.popBackStack() }
        binding.searchAptRcv.adapter = adapter

        binding.searchAptRcv.layoutManager = LinearLayoutManager(context)

        model.searchContent.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                model.updateSearchView()
            }
        })

        model.AptList.observe(viewLifecycleOwner, Observer {
            if(it.size==0){
                binding.searchAptRcv.visibility = View.GONE
                binding.aptModifyNotFoundTxt.visibility = View.VISIBLE
            }else{
                adapter.submitList(it)
                binding.searchAptRcv.visibility = View.VISIBLE
                binding.aptModifyNotFoundTxt.visibility = View.GONE
            }
        })

        binding.claimMyAptBtn.setOnClickListener {
            navController.navigate(R.id.claimNewAptFragment)
        }

        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                User.getInstance().aptName = adapter.currentList[position].aptName
                User.getInstance().aptCode = adapter.currentList[position].aptCode
                User.getInstance().address = adapter.currentList[position].address
                aptList.getInstance().aroundApt = adapter.currentList[position].aroundApt
                navController.popBackStack()

            }
        })


        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}