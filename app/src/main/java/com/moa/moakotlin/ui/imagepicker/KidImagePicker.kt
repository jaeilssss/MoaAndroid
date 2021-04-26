package com.moa.moakotlin.ui.imagepicker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentKidImagePickerBinding
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerAdapter
import com.moa.moakotlin.recyclerview.imagepickrcv.KidImagePickerAdapter

class KidImagePicker : Fragment() {

    lateinit var transfer: Transfer

    lateinit var binding : FragmentKidImagePickerBinding
    lateinit var handler: Handler
    lateinit var navController: NavController
    lateinit var mcontext : Context
    lateinit var adapter : KidImagePickerAdapter
    lateinit var model : KidImagePickerViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null){
            transfer = activity as Transfer
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        transfer.bottomGone()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_kid_image_picker,container,false)

        navController = findNavController()
        var rcv = binding.kidImagePickerRv
        rcv.setHasFixedSize(true)
        rcv.setItemViewCacheSize(30)
        var manager = GridLayoutManager(context,3)
        rcv.layoutManager = manager
        var list = ArrayList<String>()

        context?.let {
            mcontext  = it
        }
        adapter = context?.let { list = Picture.getGalleryPhotos(it)
            KidImagePickerAdapter(navController,it,list)
        }!!
       model = KidImagePickerViewModel(navController)
        rcv.adapter = adapter
        handler = Handler()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                mcontext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //lay hinh tu camera
            Libraries.requestPermissionStorage(activity)
        } else {
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    handler.post {
                        list.clear()
                        list.addAll(Picture.getGalleryPhotos(context!!))
                        adapter!!.notifyDataSetChanged()
                    }
                    Looper.loop()
                }
            }.start()
        }
        binding.model = model

        binding.kidImagePickerSubmit.setOnClickListener {
                navController.popBackStack()
        }
        return binding.root
    }
}