package com.moa.moakotlin.ui.concierge.helper

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.moa.moakotlin.databinding.FragmentHelperWriteBinding
import com.moa.moakotlin.recyclerview.sitter.SitterWritePictureAdapter
import com.moa.moakotlin.viewmodelfactory.SitterViewModelFactory

class HelperWriteFragment : Fragment() {


    lateinit var binding : FragmentHelperWriteBinding

    lateinit var navController: NavController

    lateinit var model : HelperWriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_helper_write,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,SitterViewModelFactory(navController))
                .get(HelperWriteViewModel::class.java)
        binding.model = model


        binding.sitterWritePictureRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        var adapter : SitterWritePictureAdapter
        if(Picture.getInstance()!=null){
            adapter = context?.let { SitterWritePictureAdapter(navController,Picture.getInstance(), it) }!!
        }else{
            adapter = context?.let { SitterWritePictureAdapter(navController, ArrayList(), it) }!!
        }

        binding.typeLayout.setOnClickListener {
            selectType()
        }

        binding.sitterWritePictureRcv.adapter = adapter

        binding.pictureImage.setOnClickListener {
                navController.navigate(R.id.kidImagePicker)
        }
        binding.sitterWritePictureLayout.setOnClickListener{
            navController.navigate(R.id.kidImagePicker)
        }
        binding.submit.setOnClickListener {
            if(model.title.get()?.length==0){
                Toast.makeText(context,"제목을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else if(model.type.get()?.length==0){
                Toast.makeText(context,"시터 유형을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else if(model.wage.get()?.length==0){
                Toast.makeText(context,"희망 시급을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else if(model.content.get()?.length==0){
                Toast.makeText(context,"내용을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else{
                model.submit(adapter.list)
            }
        }
        return binding.root
    }

    fun selectType(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val items = resources.getStringArray(R.array.sitterType)
        builder.setItems(R.array.sitterType, DialogInterface.OnClickListener { dialog, pos ->
            val items = resources.getStringArray(R.array.sitterType)
            model.type.set(items[pos])

        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setTitle("당신의 유형을 선택해주세요")
        alertDialog.show()
    }

}