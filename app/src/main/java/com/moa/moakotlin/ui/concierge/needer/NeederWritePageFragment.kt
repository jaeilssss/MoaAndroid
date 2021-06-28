package com.moa.moakotlin.ui.concierge.needer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentNeederWritePageBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter
import com.moa.moakotlin.ui.concierge.category.NeederCategoryActivity
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class NeederWritePageFragment : BaseFragment() {

    lateinit var binding: FragmentNeederWritePageBinding

    lateinit var navController: NavController

    lateinit var model: NeederWritePageViewModel

    lateinit var adapter : CertificationImageAdapter

    var selectedPictureList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_needer_write_page, container, false)
        navController = findNavController()

        model = ViewModelProvider(this).get(NeederWritePageViewModel::class.java)
        binding.model = model

        binding.NeederWriteCategoryLayout.setOnClickListener {

            var intent = Intent(activity,NeederCategoryActivity::class.java)
            startActivityForResult(intent,2000)
        }
        binding.NeederWriteAlbum.setOnClickListener {
            checkPermission()
        }


        initAdapter()




        return binding.root
    }

    fun initAdapter(){
        adapter  = CertificationImageAdapter()
        binding.NeederWriteRcv.adapter = adapter

        binding.NeederWriteRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==2000 && requestCode==2000){

            binding.NeederWriteCategory.text = data?.getStringExtra("selectedMainCategory")
            model.category.value = data?.getStringExtra("selectedMainCategory")


        }else if(resultCode==1000 && requestCode==1000){
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                selectedPictureList.addAll(list)
                adapter.submitList(selectedPictureList)
                adapter.notifyDataSetChanged()
            }


        }
    }

    private fun checkPermission(){
        when{
                ContextCompat.checkSelfPermission(
                        activity?.applicationContext!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED ->{

                    goToAlbum()

                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                    //교육용!!
                    showContextPopupPermission()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }
    }
    private fun showContextPopupPermission(){
        AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기"){ _, _ ->
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
                .setNegativeButton("취소하기") { _, _ ->}
                .create()
                .show()
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            1000->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    goToAlbum()
                }else{
                    Toast.makeText(context,"권한이 거부되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else->{

            }
        }
    }

    private fun goToAlbum(){
        var intent = Intent(activity,ImagePickerActivity::class.java)
        startActivityForResult(intent,1000)
    }
    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        TODO("Not yet implemented")
    }
}




