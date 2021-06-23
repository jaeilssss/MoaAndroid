package com.moa.moakotlin.ui.concierge.needer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.FragmentNeederMainBinding
import com.moa.moakotlin.recyclerview.concierge.NeederMainAdapter
import com.moa.moakotlin.recyclerview.kid.KidAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederMainFragment : BaseFragment() {

        lateinit var binding : FragmentNeederMainBinding

        lateinit var navController: NavController
        lateinit var model: NeederMainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_needer_main,container, false)

        navController = findNavController()

        model = ViewModelProvider(this).get(NeederMainViewModel::class.java)

        var kidAdapter = NeederMainAdapter()
        var kidAdapter2 = NeederMainAdapter()
        var kidAdapter3= NeederMainAdapter()
        var kidAdapter4 = NeederMainAdapter()
        var kidAdapter5 = NeederMainAdapter()
        var list = ArrayList<Needer>()
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())

        binding.NeederMainKidAllBtn.setOnClickListener {

        }
        binding.NeederMainKidRcv.adapter = kidAdapter
        binding.NeederMainpetRcv.adapter = kidAdapter2
        binding.NeederMainEducationRcv.adapter =kidAdapter3
        binding.NeederMainInteriorRcv.adapter =kidAdapter4
        binding.NeederMainEtcRcv.adapter = kidAdapter5

        binding.NeederMainKidRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainpetRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEducationRcv.layoutManager  = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainInteriorRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEtcRcv .layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        kidAdapter.submitList(list)
        kidAdapter2.submitList(list)
        kidAdapter3.submitList(list)
        kidAdapter4.submitList(list)
        kidAdapter5.submitList(list)

        binding.model = model

        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}