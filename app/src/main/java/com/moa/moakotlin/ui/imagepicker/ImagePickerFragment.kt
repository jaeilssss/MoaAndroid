package com.moa.moakotlin.ui.imagepicker


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentImagePickerBinding
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImagePickerFragment : BaseFragment() {

    lateinit var binding: FragmentImagePickerBinding
    lateinit var adapter : ImagePickerAdapter
    lateinit var navController: NavController

    lateinit var handler: Handler
    lateinit var model : ImagePickerViewModel
    lateinit var mcontext : Context
    lateinit var transfer: Transfer
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
        transfer.bottomGone()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_image_picker,container,false)
        navController = findNavController()
        var roomId = arguments?.get("roomId") as String
        var opponentUid = arguments?.get("opponentUid") as String
        var rcv = binding.imagePickerRv
        rcv.setHasFixedSize(true)
        rcv.setItemViewCacheSize(30)
        var manager = GridLayoutManager(context,3)
        rcv.layoutManager = manager
        var list = ArrayList<String>()
        context?.let { mcontext = it }!!
        binding.imagePickerSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(adapter.checkBox!=-1){
                   var result =  model.submit(adapter.list.get(adapter.checkBox),roomId,opponentUid)
                    if(result ==true){
                        navController.popBackStack()
                    }
                }
            }
        }

        binding.imagePickerBack.setOnClickListener { navController.popBackStack() }
        adapter = context?.let { list = Picture.getGalleryPhotos(it)
            ImagePickerAdapter(navController,it,list)
        }!!

        model = ViewModelProvider(this).get(ImagePickerViewModel::class.java)
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
                        //imageListRecyclerAdapter.addAll(getGalleryPhotos());
                        //checkImageStatus();
                    }
                    Looper.loop()
                }
            }.start()
        }
        binding.model = model
        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}