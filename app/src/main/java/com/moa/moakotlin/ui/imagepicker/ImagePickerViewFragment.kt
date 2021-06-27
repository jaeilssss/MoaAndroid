package com.moa.moakotlin.ui.imagepicker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ImagePickerViewFragmentBinding
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerAdapter
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerViewAdapter

class ImagePickerViewFragment : Fragment() {


    private lateinit var binding : ImagePickerViewFragmentBinding

    private lateinit var navController: NavController

    private lateinit var viewModel: ImagePickerViewViewModel

    var list = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


       binding = DataBindingUtil.inflate(inflater,R.layout.image_picker_view_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImagePickerViewViewModel::class.java)
        binding.model = viewModel

        getGalleryPhotos()
        // TODO: Use the ViewModel
    }


    private fun getGalleryPhotos(){
//        list.add(" ")
        var uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var colums =
                arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media._ID

        var cursor = context?.contentResolver!!.query(uri,colums,null,null,orderBy)

        if(cursor !=null && cursor.count>0){
            while(cursor.moveToNext()){


                var indexPath = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                list.add(cursor.getString(indexPath))

            }
        }else{
            Log.e("getGalleryPhotos","error")

        }
        list.reverse()


        var adapter = ImagePickerViewAdapter(activity?.applicationContext!!,list)

        binding.imageRcv.adapter = adapter

        binding.imageRcv.setHasFixedSize(true)
        binding.imageRcv.setItemViewCacheSize(30)
        binding.imageRcv.layoutManager = GridLayoutManager(context,3)
    }
}