package com.moa.moakotlin.ui.kid

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentKidWritePageBinding
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory
import java.time.LocalDateTime


class KidWritePageFragment : Fragment() {

    lateinit var binding: FragmentKidWritePageBinding

    lateinit var navController: NavController

    lateinit var model: KidWritePageViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_kid_write_page, container, false)
        navController = findNavController()

        model = ViewModelProvider(this, KidViewModelFactory(navController))
            .get(KidWritePageViewModel::class.java)
        binding.model = model
        var kidtypeselect = binding.kidTypeSelect
        var now = LocalDateTime.now()

        model.year.set(now.year)
        model.month.set(now.monthValue)
        model.day.set(now.dayOfMonth)

        binding.kidWriteDatePicker.init(binding.kidWriteDatePicker.year, binding.kidWriteDatePicker.month, binding.kidWriteDatePicker.dayOfMonth,
            OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

            })
    var adapter = context?.let { KidWritePictureAdapter(ArrayList<String>(), it,resources) }
        var manager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        if(Picture.getInstance()!=null){
            adapter?.list = Picture.getInstance()
            binding.kidWritePictureCount.text = adapter?.list?.size.toString()
        }

        binding.kidWriteRcv.layoutManager = manager
        binding.kidWriteRcv.adapter = adapter

        binding.kidWriteGoToAlbum.setOnClickListener {
            navController.navigate(R.id.action_kidWritePageFragment_to_kidImagePicker)
        }

        kidtypeselect.setOnClickListener {
            selectKidType()
        }
        binding.kidWriteSubmit.setOnClickListener {
            adapter?.list?.let { it1 -> model.submit(it1)
            return@setOnClickListener
            Toast.makeText(context,"사진을 한장 이상 넣어주세요!",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun selectKidType(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val items = resources.getStringArray(R.array.kidType)
        builder.setItems(R.array.kidType, DialogInterface.OnClickListener { dialog, pos ->
            val items = resources.getStringArray(R.array.kidType)
            model.type.set(items[pos])
                selectKidCount()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setTitle("자녀의 유형을 선택해주세요")
        alertDialog.show()
    }

    fun selectKidCount(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setItems(R.array.kidCount,DialogInterface.OnClickListener{dialog, pos ->
            val items = resources.getStringArray(R.array.kidCount)
            model.count.set(items[pos])
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setTitle("자녀가 몇명인가요?")
        alertDialog.show()
    }
}




