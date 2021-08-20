package com.moa.moakotlin.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.UserProfileModifyFragmentBinding
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import com.moa.moakotlin.ui.voice.VoiceRoomMakeFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class UserProfileModifyFragment : BaseFragment() {

    private lateinit var viewModel: UserProfileModifyViewModel

    private lateinit var binding: UserProfileModifyFragmentBinding


    private lateinit var navController: NavController

    private var selectedPictureList = ArrayList<String>()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(UserProfileModifyViewModel::class.java)
        myActivity.bottomNavigationGone()

        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_modify_fragment, container, false)
        (context as MainActivity).backListener = this
        binding.UserModifyCamera.setOnClickListener { checkPermission() }
        binding.model = viewModel

        navController  = findNavController()

        binding.UserModifySubmit.setOnClickListener {
            if(viewModel.checkable()){
                binding.UserProfileModifyLoading.isVisible = true
                binding.UserProfileModifyLoading.show()
                activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                CoroutineScope(Dispatchers.Main).launch {
                   if(viewModel.submit()){
                       Toast.makeText(context,"프로필 수정이 완료 되었습니다!",Toast.LENGTH_SHORT).show()
                       activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                       binding.UserProfileModifyLoading.isVisible = false
                       binding.UserProfileModifyLoading.hide()
                       var bundle = Bundle()
                       bundle.putParcelable("user",User.getInstance())
                       User.getInstance().nickName = viewModel.nickname.value.toString()
                       User.getInstance().introduction = viewModel.introduction.value.toString()
                       User.getInstance().profileImage = viewModel.image
                       navController.popBackStack()

                   }else{
                       Toast.makeText(context,"프로필 수정을 실패 했습니다!",Toast.LENGTH_SHORT).show()
                       activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                       binding.UserProfileModifyLoading.isVisible = false
                       binding.UserProfileModifyLoading.hide()

                   }
                }

            }else{
                Toast.makeText(context,"닉네임 중복 확인을 해주세요!",Toast.LENGTH_SHORT).show()
            }
        }
        binding.UserModifyNickNameCheckBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(viewModel.checkNickName()){
                    Toast.makeText(context,"사용 가능한 닉네임 입니다",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"이미 사용중인 닉네임 입니다",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.nickname.observe(viewLifecycleOwner, Observer {
            if(it.equals(User.getInstance().nickName)){
                binding.UserModifyNickNameCheckBtn.setBackgroundResource(R.drawable.shape_unable_radius_15)
                binding.UserModifyNickNameCheckBtn.setTextColor(Color.BLACK)
                binding.UserModifyNickNameCheckBtn.isClickable = false
            }else{
                binding.UserModifyNickNameCheckBtn.setBackgroundResource(R.drawable.button_shape_black_radius_15)
                binding.UserModifyNickNameCheckBtn.setTextColor(Color.WHITE)
                binding.UserModifyNickNameCheckBtn.isClickable = true
            }
        })
        setProfileImage()
        return binding.root

    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun setProfileImage(){

    if(User.getInstance().profileImage.isNotEmpty()){
        Glide.with(binding.root).load(User.getInstance().profileImage).into(binding.UserProfileModifyImage)
    }
}
    private fun checkPermission() {
        when {

            ContextCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {

                goToAlbum()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                //교육용!!
                    showContextPopupPermission()
            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }
    private fun showContextPopupPermission() {
        AlertDialog.Builder(context).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기") { _, _ ->
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
                .setNegativeButton("취소하기") { _, _ -> }
                .create()
                .show()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToAlbum()
                } else {
                    Toast.makeText(context, "권한이 거부되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }

    private fun goToAlbum() {
        var intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra("selectedPictureList", selectedPictureList)
        intent.putExtra("singleImage", "singleImage")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == VoiceRoomMakeFragment.REQUEST_PICTURE_CODE && resultCode == VoiceRoomMakeFragment.REQUEST_PICTURE_CODE) {
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                if(list?.size!=0){
                    Glide.with(activity?.applicationContext!!).load(list.get(0))
                            .into(binding.UserProfileModifyImage)
                    viewModel.image = list.get(0)
                }
            }
        }
    }
}