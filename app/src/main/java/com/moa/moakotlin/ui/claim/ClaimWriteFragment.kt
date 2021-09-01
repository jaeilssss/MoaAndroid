package com.moa.moakotlin.ui.claim

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.ClaimWriteFragmentBinding
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity

class ClaimWriteFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClaimWriteFragment()
        var REQUEST_CATEGORY_SELECTION = 2000
        var REQUEST_HOPE_DATE_SELECT_CODE = 6000
        val REQUEST_PHOTO_CODE=1000
    }

    private lateinit var viewModel: ClaimWriteViewModel

    private lateinit var binding : ClaimWriteFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.claim_write_fragment ,container , false)
        binding.back.setOnClickListener { navController.popBackStack() }
        binding.ClaimWriteCategoryLayout.setOnClickListener {
            var intent = Intent(context,ClaimCategorySelectActivity::class.java)

            startActivityForResult(intent , REQUEST_CATEGORY_SELECTION)
        }
        binding.ClaimWriteAlbum.setOnClickListener {

            checkPermission()
        }
        binding.ClaimWriteSubmit.setOnClickListener { }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimWriteViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        viewModel.title.observe(viewLifecycleOwner, Observer {
            setChangeBackgroundBtn()
        })

        viewModel.content.observe(viewLifecycleOwner, Observer {
            setChangeBackgroundBtn()
        })
        viewModel.category.observe(viewLifecycleOwner, Observer {
            setChangeBackgroundBtn()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode== REQUEST_CATEGORY_SELECTION
                && resultCode==REQUEST_CATEGORY_SELECTION){

            viewModel.category.value= data?.getStringExtra("category")
            binding.ClaimWriteCategory.text = viewModel.category.value
        }else if(requestCode==REQUEST_PHOTO_CODE && resultCode ==REQUEST_PHOTO_CODE){

        }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun setChangeBackgroundBtn(){
        if(viewModel.check()){

            binding.ClaimWriteSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.ClaimWriteSubmit.setTextColor(Color.BLACK)
            binding.ClaimWriteSubmit.isClickable = true

        }else{
            binding.ClaimWriteSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.ClaimWriteSubmit.isClickable = false
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
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                //교육용!!
                showContextPopupPermission()
            }
            else ->{
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
        }
    }

    private fun goToAlbum(){

        var intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra("selectedPictureList",ArrayList<String>())
        startActivityForResult(intent,1000)
        
    }
    private fun showContextPopupPermission(){
        AlertDialog.Builder(context).setTitle("권한이 필요합니다")
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
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    goToAlbum()
                }else{
                    Toast.makeText(context,"권한이 거부되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else->{

            }
        }
    }

}