package com.moa.moakotlin.ui.concierge.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.FragmentNeederMainBinding
import com.moa.moakotlin.recyclerview.concierge.HelperMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelperMainFragment : BaseFragment() {

        lateinit var binding : FragmentNeederMainBinding

        lateinit var navController: NavController
        lateinit var model: HelperMainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_needer_main,container, false)

        navController = findNavController()

        model = ViewModelProvider(this).get(HelperMainViewModel::class.java)


        var kidAdapter = HelperMainAdapter()

        var interiorAdapter = HelperMainAdapter()
        var kidAdapter3= HelperMainAdapter()
        var kidAdapter4 = HelperMainAdapter()
        var kidAdapter5 = HelperMainAdapter()
        var list = ArrayList<Helper>()
        list.add(Helper())
        list.add(Helper())
        list.add(Helper())
        list.add(Helper())
        list.add(Helper())

        binding.NeederMainKidAllBtn.setOnClickListener {

        }
        binding.NeederMainKidRcv.adapter = kidAdapter
        binding.NeederMainpetRcv.adapter = interiorAdapter
        binding.NeederMainEducationRcv.adapter =kidAdapter3
        binding.NeederMainInteriorRcv.adapter =kidAdapter4
        binding.NeederMainEtcRcv.adapter = kidAdapter5

        binding.NeederMainKidRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainpetRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEducationRcv.layoutManager  = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainInteriorRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEtcRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)


        binding.model = model

        binding.NeederMainKidAllBtn.setOnClickListener {
            // 키드 데이터를 가지고 오는 viewmodel 메소드 호출 필요
//            navController.navigate(R.id.categoryMainFragment)
            navController.navigate(R.id.HelperReadFragment)
        }
        binding.NeederMaininteriorAllBtn.setOnClickListener {

        }
        binding.NeederMainEduAllBtn.setOnClickListener {

        }
        binding.NeederMainEtcAllBtn.setOnClickListener {

        }
        binding.NeederMainPetAllBtn.setOnClickListener {

        }
        initGetData(kidAdapter,"육아")
//        initGetData(interiorAdapter,"인테리어")
        return binding.root
    }


    fun initGetData(adapter: HelperMainAdapter,mainCategory : String){
        CoroutineScope(Dispatchers.Main).launch {
            var list = model.getData(mainCategory)
            adapter.submitList(list)
        }
    }
    override fun onBackPressed( ) {
        navController.popBackStack()
    }


}