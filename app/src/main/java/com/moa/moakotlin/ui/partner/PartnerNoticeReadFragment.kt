package com.moa.moakotlin.ui.partner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.PartnerNotice
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.PartnerNoticeReadFragmentBinding
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat

class PartnerNoticeReadFragment : BaseFragment() {


    private lateinit var viewModel: PartnerNoticeReadViewModel

    private lateinit var binding : PartnerNoticeReadFragmentBinding

    private lateinit var navController: NavController

    private lateinit var partnerNotice : PartnerNotice

    private lateinit var viewPagerAdapter : ConciergeReadViewpagerAdapter

    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT.png?alt=media&token=8623aaa7-4f88-44fb-a05e-64d2a02cb683"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.partner_notice_read_fragment , container , false)
        binding.back.setOnClickListener { onBackPressed() }
        myActivity.bottomNavigationGone()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PartnerNoticeReadViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        arguments?.let {
            partnerNotice = it.getParcelable<PartnerNotice>("notice")!!
            setViewData()
        }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun setViewData(){
        binding.partnerNoticeReadTitle.text = partnerNotice.title
        binding.partnerNoticeReadContent.text = partnerNotice.content
        if(User.getInstance().aptCode.equals(partnerNotice.aptCode)){
            binding.partnerNoticeReadType.text = "관리실 공지"
        }else{
            binding.partnerNoticeReadType.text = "${partnerNotice.aptCode}청 공지"
        }
        var dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm ")
        binding.partnerNoticeReadDate.text = dateFormat.format(partnerNotice.timeStamp.toDate())
        CoroutineScope(Dispatchers.Main).launch {
            var writer = viewModel.getWriterInfo(partnerNotice.uid)
            binding.partnerNoticeReadNickname.text = writer.nickName
            Glide.with(binding.root).load(writer.profileImage).into(binding.partnerNoticeReadPrifile)
        }
        if(partnerNotice.images.size==0){
            var list = ArrayList<String>()
            list.add(defaultUrl)
            viewPagerAdapter = ConciergeReadViewpagerAdapter(requireContext(),list)
        }else{
            viewPagerAdapter = ConciergeReadViewpagerAdapter(requireContext(),partnerNotice.images)
        }

        binding.partnerNoticeReadViewPager.adapter = viewPagerAdapter
        setCurrentOnboardingIndicator(0)
        setUpBoardingIndicators(partnerNotice.images.size)
        binding.partnerNoticeReadViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })
    }





private fun setUpBoardingIndicators(size: Int){
    binding.partnerNoticeReadIndicators.removeAllViews()
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
        binding.partnerNoticeReadIndicators?.addView(indicators[i])
    }
}


    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.partnerNoticeReadIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.partnerNoticeReadIndicators?.getChildAt(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_inactive))
            }
        }
    }

}