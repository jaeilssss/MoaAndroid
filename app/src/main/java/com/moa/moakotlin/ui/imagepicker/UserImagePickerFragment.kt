package com.moa.moakotlin.ui.imagepicker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
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
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.UserImagePickerFragmentBinding
import com.moa.moakotlin.recyclerview.imagepickrcv.KidImagePickerAdapter
import com.moa.moakotlin.recyclerview.imagepickrcv.UserImageAdapter

class UserImagePickerFragment : Fragment() {

    lateinit var transfer: Transfer
    private lateinit var viewModel: UserImagePickerViewModel

    private lateinit var binding : UserImagePickerFragmentBinding
    lateinit var handler: Handler
    private lateinit var navController: NavController
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null){
            transfer = activity as Transfer
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.user_image_picker_fragment,container,false)
        viewModel = ViewModelProvider(this).get(UserImagePickerViewModel::class.java)
        binding.model = viewModel

        navController= findNavController()

        var rcv = binding.userImagePickerRcv
        rcv.setHasFixedSize(true)
        rcv.setItemViewCacheSize(30)
        var manager = GridLayoutManager(context,3)
        rcv.layoutManager = manager
        var list = ArrayList<String>()
        var mcontext = context
        var adapter = context?.let { list = Picture.getGalleryPhotos(it)
            UserImageAdapter(list,it)
        }!!

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                when(v.id){
                    R.id.image_picker_layout->{

                        Picture.getInstance().clear()
                        Picture.getInstance().add(adapter.list.get(position))

                        if(adapter.checkBox==-1){
                            adapter.checkBox = position
                            adapter.resetting()
                        }else{
                            adapter.checkBox = position
                            adapter.resetting()
                        }
                    }

                }
            }

        })

        binding.imagePickerBack.setOnClickListener {
            Picture.getInstance().clear()
            navController.popBackStack()
        }

        binding.userImagePickerSubmit.setOnClickListener {
            navController.popBackStack()
        }
        rcv.adapter = adapter
        handler = Handler()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                        mcontext!!,
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}