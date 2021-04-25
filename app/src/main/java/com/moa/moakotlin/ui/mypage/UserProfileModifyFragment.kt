package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            navController.navigate(R.id.userImagePickerFragment)
        }
        binding.model = viewModel
        initView()
                  return binding.root
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