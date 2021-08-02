package com.moa.moakotlin.ui.imagepicker

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.SingleImagePickerFragmentBinding
import com.moa.moakotlin.recyclerview.imagepickrcv.ConciergeImagePickerAdapter

class SingleImagePickerFragment(var selectedPictures : ArrayList<String>) : Fragment() {

    private lateinit var viewModel: SingleImagePickerViewModel

    private lateinit var binding : SingleImagePickerFragmentBinding

    private lateinit var adapter : ConciergeImagePickerAdapter

    var list = ArrayList<String>()

    var myActivity : ImagePickerActivity ?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        myActivity = activity as ImagePickerActivity

    }
    override fun onDetach() {
        super.onDetach()

        myActivity = null
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.single_image_picker_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SingleImagePickerViewModel::class.java)

        binding.model  = viewModel

        viewModel.selectedPictureList.observe(viewLifecycleOwner, Observer {
            myActivity?.selectedPictures = viewModel.selectedPictureList.value!!
        })
        getGalleryPhotos()

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                if(adapter.selectedPicture.contains(adapter.list[position])){
                    adapter.selectedPicture.remove(adapter.list[position])
                    adapter.checkBoxList.clear()
                    adapter.resetting(position)
                    viewModel.selectedPictureList.value = adapter.selectedPicture

                }else{
                    if(adapter.selectedPicture.size==1){
                        var index = adapter.checkBoxList.get(0)
                        adapter.resetting(index)
                        adapter.selectedPicture.clear()
                        adapter.checkBoxList.clear()
                    }
                    adapter.selectedPicture.add(adapter.list[position])
                    adapter.checkBoxList.add(position)
                    adapter.resetting(position)
                    viewModel.selectedPictureList.value = adapter.selectedPicture
                }

            }
        })
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

        adapter = ConciergeImagePickerAdapter(activity?.applicationContext!!,list)

        binding.imageRcv.adapter = adapter

        binding.imageRcv.setHasFixedSize(true)
        binding.imageRcv.setItemViewCacheSize(30)
        binding.imageRcv.layoutManager = GridLayoutManager(context,3)
    }
}