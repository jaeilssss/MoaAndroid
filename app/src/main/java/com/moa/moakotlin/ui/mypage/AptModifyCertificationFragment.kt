package com.moa.moakotlin.ui.mypage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.AptModifyCertificationFragmentBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AptModifyCertificationFragment : BaseFragment() {

    companion object {
        fun newInstance() = AptModifyCertificationFragment()
    }


    val REQUEST_IMAGE_CAPTURE = 1000 // 카메라 사진찰영 요청 코드

    lateinit var curPhotoPath : String

    private lateinit var binding : AptModifyCertificationFragmentBinding

    private lateinit var viewModel: AptModifyCertificationViewModel

    private lateinit var navController: NavController

    private lateinit var adapter : CertificationImageAdapter
    private lateinit var list : ArrayList<String>
    private val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.apt_modify_certification_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifyCertificationViewModel::class.java)

        navController = findNavController()

        list = ArrayList<String>()

        adapter = CertificationImageAdapter()

        binding.aptModifyCertificationRcv.adapter = adapter



        adapter.setOnItemCLickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                list.removeAt(position)
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
                binding.itemGoToAlbum.isVisible = list.size != 3
                binding.AptModifyCertificationPlusImage.isVisible = list.size !=3
            }
        })

    binding.aptModifyCertificationRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    binding.itemGoToAlbum.setOnClickListener {
        when {
            activity?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED  &&
                    ContextCompat.checkSelfPermission(
                        activity?.applicationContext!!,
                        android.Manifest.permission.CAMERA
                    )== PackageManager.PERMISSION_GRANTED -> {
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

    binding.AptModifyCertificationNext.setOnClickListener {

        context?.let { it1 ->
            AptCertificationImageAlertDialog(it1)

                .setMessage(getString(R.string.AptCertificationImagesText))
                .setPositiveButton("예"){
                    activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    uploadCertificationImage()
                    activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    //
                }
                .setNegativeButton() {

                }
                .show()
        }

    }
    settingEnableButton()
}

    fun uploadCertificationImage(){


            CoroutineScope(Dispatchers.Main).launch {

                viewModel.certification(adapter.currentList)
                User.getInstance().certificationStatus = "심사중"
                viewModel.userCertificationModify()


            }

            certificationAlertDialog()

    }
private fun settingEnableButton(){
    if(adapter.itemCount==0){
        binding.AptModifyCertificationNext.setBackgroundResource(R.drawable.shape_unable_radius_15)
        binding.AptModifyCertificationNext.setTextColor(Color.WHITE)
        binding.AptModifyCertificationNext.isClickable = false
    }else{
        binding.AptModifyCertificationNext.setBackgroundResource(R.drawable.button_shape_main_color)
        binding.AptModifyCertificationNext.setTextColor(Color.BLACK)
        binding.AptModifyCertificationNext.isClickable = true
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
        binding.itemGoToAlbum.isVisible = list.size != 3
        binding.AptModifyCertificationPlusImage.isVisible = list.size !=3
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

    fun certificationAlertDialog(){
        var dialog = SinglePositiveButtonDialog(activity?.applicationContext!!)
        context?.let { it1 ->
           dialog= SinglePositiveButtonDialog(it1)

                .setMessage(getString(R.string.CertificationEndMessage))
                .setPositiveButton("예"){

                    navController.popBackStack(R.id.MyPageFragment,false)
                }


        }

        dialog.show()
        Thread.sleep(3000)
        navController.popBackStack(R.id.MyPageFragment,false)

    }
}