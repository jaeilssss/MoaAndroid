package com.moa.moakotlin.ui.kid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentKidModifyBinding
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory


class KidModifyFragment : Fragment() {


    lateinit var binding : FragmentKidModifyBinding

    lateinit var model : KidModifyViewModel

    lateinit var navController: NavController

    lateinit var bundle :Bundle

    lateinit var kid : Kid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_kid_modify,container,false)

        navController = findNavController()

        model = ViewModelProvider(this, KidViewModelFactory(navController)).get(KidModifyViewModel::class.java)

        arguments?.let {
            bundle = arguments as Bundle
            kid = bundle.getParcelable("kid")!!
        }

        model.initDataSetting(kid)


        var manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        binding.kidModifyRcv.layoutManager = manager
        var adapter = context?.let { kid?.images?.let { it1 -> KidWritePictureAdapter(it1, it,resources) } }
        if(Picture.getInstance()!=null){
            adapter?.list = Picture.getInstance()
        }
        binding.kidModifyRcv.adapter = adapter
        binding.kidModifySubmit.setOnClickListener {
            adapter?.list?.let { it1 -> model.submit(it1) }
        }
        binding.kidModifyGoToAlbum.setOnClickListener {
            navController.navigate(R.id.action_kidModifyFragment_to_kidImagePicker)
        }
        binding.model = model
        return binding.root
    }


}