package com.moa.moakotlin.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.costumdialog.AptCertificationImageAlertDialog
import com.moa.moakotlin.costumdialog.CostumAlertDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentMyPageBinding
import com.moa.moakotlin.ui.signup.AptSearchActivity


class MyPageFragment : Fragment() {

    lateinit var binding : FragmentMyPageBinding

    lateinit var navController: NavController

    lateinit var model :MyPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        model = ViewModelProvider(this).get(MyPageViewModel::class.java)
        binding.model = model
        navController = findNavController()
        setViewData()


        binding.myPageGoToProfileBtn.setOnClickListener {
            var bundle = Bundle()
            bundle.putParcelable("user",User.getInstance())
            navController.navigate(R.id.action_MyPageFragment_to_userProfileFragment,bundle)
        }

        binding.myPageAptCertification.setOnClickListener {
            context?.let { it1 ->
                AptCertificationImageAlertDialog(it1)
                        .setMessage(resources.getString(R.string.ReAptCertification))
                        .setPositiveButton("네"){
                            var intent = Intent(activity?.applicationContext,AptSearchActivity::class.java)

                            startActivity(intent)
                        }.setNegativeButton {

                        }.show()
            }
        }

        binding.myPageTalentSharingText.setOnClickListener { navController.navigate(R.id.action_MyPageFragment_to_myConciergeListFragment) }

    }

    fun setViewData(){
        binding.MyPageNickName.text = User.getInstance().nickName
        if(User.getInstance().profileImage.isNotEmpty()){
            Glide.with(binding.root).load(User.getInstance().profileImage).into(binding.MyPageUserProfile)
        }

        binding.MyPageUserAptInfoText.text = "${User.getInstance().aptName}"


    }
}