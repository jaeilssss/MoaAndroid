package com.moa.moakotlin.ui.concierge.helper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.costumdialog.CostumAlertDialog

import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentHelperReadBinding
import com.moa.moakotlin.ui.bottomsheet.ConciergeReadBottomSheetFragment
import com.moa.moakotlin.ui.concierge.ConciergeWriteActivity
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class HelperReadFragment : BaseFragment() {

    lateinit var binding : FragmentHelperReadBinding

    lateinit var navController: NavController

    lateinit var model: HelperReadViewModel

    lateinit var  bundle : Bundle
    var helper = Helper()
    var writer = User()
    var myActivity = MainActivity()
    lateinit var adapter : ConciergeReadViewpagerAdapter
    companion object{
        val REQUEST_MODIFY_CODE = 4000
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        myActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_helper_read, container, false)

        navController = findNavController()

        model = ViewModelProvider(this).get(HelperReadViewModel::class.java)
        
        binding.model = model
        myActivity.bottomNavigationGone()
        arguments?.let {
            helper = it.getParcelable<Helper>("data")!!
            writer = it.getParcelable<User>("writer")!!

        }

        binding.HelperReadOption.setOnClickListener {
        val option : ConciergeReadBottomSheetFragment = ConciergeReadBottomSheetFragment {
            when(it){
                0 -> {
                    goToModify()
                }
                1 -> {
                    costumDialog()
                }
            }

        }
            option.show(activity?.supportFragmentManager!!,"bottomsheet")
        }


         adapter = helper.images?.let { it1 -> ConciergeReadViewpagerAdapter(activity?.applicationContext!!, it1) }!!

        binding.HelperReadViewPager.adapter = adapter

        binding.back.setOnClickListener {
            navController.popBackStack()
        }

        binding.HelperMainIntroduce.setOnClickListener {
            setUpFragment(ConciergeReadIntroduceFragment(helper,null))
        }
        binding.HelperMainReview.setOnClickListener {
            setUpFragment(HelperReadReviewFragment())
        }

        binding.HelperReadViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })

        model.newHelper.observe(viewLifecycleOwner, Observer {
                            helper = it
                adapter.list = ArrayList()
                adapter.list.addAll(it.images!!)
            binding.HelperReadViewPager.adapter = adapter

                setHelperData(it)
                setUpBoardingIndicators(it.images!!.size)
                setUpFragment(ConciergeReadIntroduceFragment(it,null))
                setCurrentOnboardingIndicator(0)

        })
        setUpBoardingIndicators(helper?.images!!.size)
        setCurrentOnboardingIndicator(0)
        setUpFragment(ConciergeReadIntroduceFragment(helper,null))
        checkVisible()
        setWriterInfo()
        setHelperData(helper)
        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun checkVisible(){
        if(helper.uid != User.getInstance().uid){
            binding.HelperReadOption.isVisible = false
        }else{
            binding.HelperReadChatLayout.isVisible = false
        }
    }

    private fun setUpBoardingIndicators(size : Int){
        binding.HelperReadIndicators.removeAllViews()
        val indicators =
                 arrayOfNulls<ImageView>(size)

        var layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(8,0,8,0)

        for( i in indicators.indices){
            indicators[i] = ImageView(context)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.onboarding_indicator_inactive
            ))
            indicators[i]?.layoutParams = layoutParams
            binding.HelperReadIndicators?.addView(indicators[i])
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode== REQUEST_MODIFY_CODE && resultCode == REQUEST_MODIFY_CODE){

            data?.getParcelableExtra<Helper>("newHelper")?.let {
                model.newHelper.value = it
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setHelperData(helper : Helper){
        binding.HelperReadMainCategory.text = helper.mainCategory
        binding.HelperMainTitle.text = helper.title
    }
    private fun setWriterInfo(){
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")
        binding.HelperReadNickName.text = writer.nickName
        binding.HelperReadDate.text = dateFormat.format(helper.timeStamp.toDate())
    }
    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.HelperReadIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.HelperReadIndicators?.getChildAt(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_inactive))
            }
        }
    }
    fun setUpFragment(fragment : Fragment){
        val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.HelperMainFragmentView, fragment).commit()
    }

    fun costumDialog(){
        context?.let {
            CostumAlertDialog(it)
                .setMessage("게시글을 정말 삭제하시겠습니까?")
                .setPositiveButton("예"){
                    CoroutineScope(Dispatchers.Main).launch {
                        if(model.delete(helper.mainCategory,helper.documentID)){
                            showToast(activity?.applicationContext!!,"삭제가 완료되었습니다")
                            navController.popBackStack()
                        }
                    }
                }
                .setNegativeButton {

                }.show()
        }



    }
    fun goToModify(){
        var intent = Intent(activity,ConciergeWriteActivity::class.java)
        intent.putExtra("helper",helper)
        startActivityForResult(intent ,REQUEST_MODIFY_CODE )
    }
}