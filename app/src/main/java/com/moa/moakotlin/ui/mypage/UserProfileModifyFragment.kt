package com.moa.moakotlin.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.UserProfileModifyFragmentBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class UserProfileModifyFragment : Fragment() {

    private lateinit var viewModel: UserProfileModifyViewModel

    private lateinit var binding : UserProfileModifyFragmentBinding


    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(UserProfileModifyViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater,R.layout.user_profile_modify_fragment,container, false)

        Toast.makeText(context,"테스트,",Toast.LENGTH_SHORT).show()
        navController = findNavController()


        binding.userModifyBtn.setOnClickListener {
            var data =false
            CoroutineScope(Dispatchers.Main).launch {
                 data = viewModel.submit()
                if(data==true){
                    showToast("성공!")
                    var bundle =Bundle()
                    var user = User.getInstance()
                    user.introduction = viewModel.introduction.get().toString()
                    user.nickName = viewModel.nickname.get().toString()
                    bundle.putParcelable("user", user)
                    navController.navigate(R.id.action_userProfileModifyFragment_to_userProfileFragment,bundle)
                }else{
                    showToast("실패!")
                }
            }
        }
        binding.userProfileModifyImage.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(activity?.applicationContext!!,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED ->{
                    // 권한이 잘 부여됬을 떄
                    navController.navigate(R.id.userImagePickerFragment)
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                    // 교육용 팝 확인 후 권한 팝업 띄우는 기능
                    showContextPopupPermission()
                }
                else->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }
        }
        binding.model = viewModel
        initView()
                  return binding.root
    }

    override fun startIntentSenderForResult(intent: IntentSender?, requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options)
        Toast.makeText(context,"여기서 감지하네요22!!",Toast.LENGTH_SHORT).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(context,"여기서 감지하네요!!",Toast.LENGTH_SHORT).show()
        println("여기서 감지할수있다....")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==1000){
            navController.navigate(R.id.userImagePickerFragment)
        }
    }

    fun showContextPopupPermission(){
        AlertDialog.Builder(context).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기") { _, _ ->
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
                .setNegativeButton("취소하기") { _, _ ->}
                .create()
                .show()
    }
fun showToast(msg : String){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}
    fun initView(){
        if(Picture.getInstance().size==0){
            if(User.getInstance().profileImage!=null){
                context?.let { Glide.with(it).load(User.getInstance().profileImage).into(binding.userProfileModifyImage) }
            }
        }else{
            context?.let { Glide.with(it).load(Picture.getInstance().get(0)).into(binding.userProfileModifyImage) }
        }
        viewModel.nickname.set(User.getInstance().nickName)
        viewModel.introduction.set(User.getInstance().introduction)
        binding.userProfileModifyAptName.text = User.getInstance().aptName
    }

}