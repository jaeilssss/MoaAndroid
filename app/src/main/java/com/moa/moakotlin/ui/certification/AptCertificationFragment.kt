package com.moa.moakotlin.ui.certification

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.customdialog.AptCertificationImageAlertDialog
import com.moa.moakotlin.databinding.AptCertificationFragmentBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AptCertificationFragment : BaseFragment() {


    val REQUEST_IMAGE_CAPTURE = 1000 // 카메라 사진찰영 요청 코드

    lateinit var curPhotoPath : String // 문자열 형태의 사진 경로 값

    companion object {
        fun newInstance() = AptCertificationFragment()
    }

    private lateinit var binding : AptCertificationFragmentBinding

    private lateinit var viewModel: AptCertificationViewModel

    private lateinit var navController: NavController

    private lateinit var adapter : CertificationImageAdapter

    private lateinit var list : ArrayList<String>
    private val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.apt_certification_fragment, container, false)

        viewModel = ViewModelProvider(this).get(AptCertificationViewModel::class.java)

        navController = findNavController()

        binding.model = viewModel

        list = ArrayList<String>()


        adapter = CertificationImageAdapter()

        binding.aptCertificationRcv.adapter = adapter

        binding.aptCertificationRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.itemGoToAlbum.setOnClickListener {
            when {
                activity?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                            it1,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } ==PackageManager.PERMISSION_GRANTED  &&
                        ContextCompat.checkSelfPermission(
                                activity?.applicationContext!!,
                                android.Manifest.permission.CAMERA
                        )==PackageManager.PERMISSION_GRANTED -> {
                    takeCapture()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    //교육용 팝업
                    showContextPopUpPermission()
                }
                else -> {
                    requestPermissions(requiredPermissions, 1000)
                }

            }
        }

        binding.AptCertificationNext.setOnClickListener {


//
            context?.let { it1 ->
                AptCertificationImageAlertDialog(it1)

                        .setMessage(getString(R.string.AptCertificationImagesText))
                        .setPositiveButton("예"){
                            CoroutineScope(Dispatchers.Main).launch {
                                viewModel.certification(adapter.currentList)
                            }
                            var bundle = Bundle()
                            bundle.putBoolean("isCertified",true)
                            navController.navigate(R.id.signUpResultFragment,bundle)
                        }
                        .setNegativeButton() {

                        }
                        .show()
            }



            // 여기서 어떻게 해야 하띾 고민중
        }
        settingEnableButton()
            return binding.root
        }

    private fun settingEnableButton(){
        if(adapter.itemCount==0){
            binding.AptCertificationNext.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.AptCertificationNext.setTextColor(Color.WHITE)
            binding.AptCertificationNext.isClickable = false
        }else{
            binding.AptCertificationNext.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.AptCertificationNext.setTextColor(Color.BLACK)
            binding.AptCertificationNext.isClickable = true
        }
    }
    private fun takeCapture() {
        // 기본 카메라 앱 실행

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                val photoFile : File? = try{
                    createImageFile()
                }catch (ex : IOException){
                    null
                }
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                            activity?.applicationContext!!,
                            "com.moa.moakotlin.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)

                }
            }
        }
    }
    // 이미지 파일 생성
    private fun createImageFile(): File {
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir)
                .apply {
                    curPhotoPath = absolutePath
                }
    }
    override fun onBackPressed() {
        navController.popBackStack()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1000){
                takeCapture()
        }
    }

    fun showContextPopUpPermission(){
        AlertDialog.Builder(context).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기"){ _, _->
                    requestPermissions(requiredPermissions, 1000)
                }
                .setNegativeButton("취소하기"){ _, _->

                }
                .create()
                .show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            //  이미지를 성공적으로 가져왔다면

            val bitmap : Bitmap

            val file = File(curPhotoPath)

            list.add(curPhotoPath)
            adapter.submitList(list)
            adapter.notifyDataSetChanged()

            settingEnableButton()

//            if(Build.VERSION.SDK_INT < 28){
                // 안드로이드 9.0 (PIE) 버전 보다 낮을 경우

//                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver,Uri.fromFile(file))

//                image.setImageBitmap(bitmap)
//            }else {
                //pie 버번 보다 이상 일 경우

//                val decode = ImageDecoder.createSource(
//                        activity?.contentResolver!!,
//                        Uri.fromFile(file)
//                )
//                bitmap = ImageDecoder.decodeBitmap(decode)
//                image.setImageBitmap(bitmap)
//            }
//            savePhoto(bitmap)
        }
    }
}