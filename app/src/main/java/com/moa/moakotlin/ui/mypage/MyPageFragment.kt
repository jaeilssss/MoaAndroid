package com.moa.moakotlin.ui.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentMyPageBinding


class MyPageFragment : BaseFragment() {

    lateinit var binding : FragmentMyPageBinding

    lateinit var navController: NavController

    lateinit var model :MyPageViewModel
    var lastTimeBackPressed : Long = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page,container,false)



        myActivity.bottomNavigationVisible()

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
        if(User.getInstance().certificationStatus.equals("인증")){
            showAlertDialog(resources.getString(R.string.ReAptCertification))
        }else if(User.getInstance().certificationStatus.equals("심사중")){
                showAlertDialog()
        } else{
            navController.navigate(R.id.action_MyPageFragment_to_aptModifyFragment)
        }
        }

        binding.myPageTalentSharingText.setOnClickListener { navController.navigate(R.id.action_MyPageFragment_to_myConciergeListFragment) }
        binding.myPageMyAccountSetting.setOnClickListener { navController.navigate(R.id.action_MyPageFragment_to_myAccountSettingFragment) }
        binding.myPageAlarmSetting.setOnClickListener { navController.navigate(R.id.action_MyPageFragment_to_alarmSettingFragment) }
        binding.myPageQuestion.setOnClickListener { navController.navigate(R.id.action_MyPageFragment_to_questionFragment) }
        binding.myPageNotice.setOnClickListener { navController.navigate(R.id.action_MyPageFragment_to_noticeFragment) }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            activity?.finish()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(context,"종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show()
    }

    fun setViewData(){
        binding.MyPageNickName.text = User.getInstance().nickName
        if(User.getInstance().profileImage.isNotEmpty()){
            Glide.with(binding.root).load(User.getInstance().profileImage).into(binding.MyPageUserProfile)
        }

        var strList = User.getInstance().address.split(" ")
        binding.MyPageUserAptInfoText.text = "${strList.get(strList.size-3)} ${strList.get(strList.size-2)} ${strList.get(strList.size-1)}"


    }


    fun showAlertDialog(){
        context?.let { it1 ->
            SinglePositiveButtonDialog(it1)
                    .setMessage("지금 심사중입니다\n인증절차는 최대 2일 소요됩니다")
                    .setPositiveButton("네"){

                    }.show()
        }
    }
    fun showAlertDialog(str : String){
        context?.let { it1 ->
            AptCertificationImageAlertDialog(it1)
                .setMessage(str)
                .setPositiveButton("네"){
                    if(User.getInstance().certificationStatus.equals("인증")) {
                        navController.navigate(R.id.action_MyPageFragment_to_aptModifyFragment)
                    }else{
                        navController.navigate(R.id.action_MyPageFragment_to_aptModifyCertificationNoticeFragment)
                    }


                }.setNegativeButton {

                }.show()
        }
    }
}