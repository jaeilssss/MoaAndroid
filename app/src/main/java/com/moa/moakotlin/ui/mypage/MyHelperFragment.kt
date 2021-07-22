package com.moa.moakotlin.ui.mypage

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.MyHelperFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.CategoryHelperMainAdapter
import com.moa.moakotlin.recyclerview.concierge.HelperMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyHelperFragment : BaseFragment() {


    private lateinit var viewModel: MyHelperViewModel

    private lateinit var navController: NavController

    private lateinit var adapter : CategoryHelperMainAdapter

    private lateinit var binding: MyHelperFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.my_helper_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyHelperViewModel::class.java)
        navController = findNavController()

        setAdapter()

        binding.MyHelperRcv.adapter = adapter
        binding.MyHelperRcv.layoutManager = LinearLayoutManager(context)
        viewModel.initData()
        viewModel.myHelperDataList.observe(viewLifecycleOwner, Observer {
            println("mHelperData list - > ${it.size}")
            adapter.submitList(it)
        })

    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun setAdapter(){
        adapter = CategoryHelperMainAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    var writer = viewModel.getUserInfo(adapter.currentList[position].uid)

                    var bundle = Bundle()

                    bundle.putParcelable("writer",writer)
                    bundle.putParcelable("data",adapter.currentList[position])

                    navController.navigate(R.id.action_myConciergeListFragment_to_HelperReadFragment,bundle)

                }
            }

        })
    }

}