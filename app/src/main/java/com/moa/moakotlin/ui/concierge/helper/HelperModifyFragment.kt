package com.moa.moakotlin.ui.concierge.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.databinding.FragmentHelpterModifyBinding
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter
import com.moa.moakotlin.viewmodelfactory.SitterViewModelFactory

class HelperModifyFragment : Fragment() {

    lateinit var binding : FragmentHelpterModifyBinding

    lateinit var model : HelperModifyViewModel

    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_helpter_modify,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,SitterViewModelFactory(navController)).get(HelperModifyViewModel::class.java)

        binding.model= model

        var sitter : Sitter
        arguments.let {
            sitter = (arguments as Bundle).getParcelable("sitter")!!
        }

        model.title.set(sitter.title)
        model.content.set(sitter.content)
        model.isNego.set(sitter.isNegotiable)
        model.type.set(sitter.type)
        model.wage.set(sitter.wage)

        model.sitter = sitter
        // pictureCount 는 보류...
//        model.pictureCount.set
        Picture.getInstance()
        sitter.images?.let { Picture.setInstance(it) }
        var adapter = context?.let { KidWritePictureAdapter(Picture.getInstance(), it,resources) }
        binding.sitterWritePictureRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.sitterWritePictureRcv.adapter = adapter

        binding.sitterWritePictureLayout.setOnClickListener {
            navController.navigate(R.id.kidImagePicker)
        }
        binding.sitterWritePictureBtn.setOnClickListener {
            navController.navigate(R.id.kidImagePicker)
        }
        binding.submit.setOnClickListener {
            if(model.check().equals("success")){
                if (adapter != null) {
                    model.submit(adapter.list)
                }
            }else{
                Toast.makeText(context,model.check(),Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }

}