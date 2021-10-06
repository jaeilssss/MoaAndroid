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
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Contract
import com.moa.moakotlin.databinding.PartnerContractReadFragmentBinding
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PartnerContractReadFragment : BaseFragment() {

    companion object {
        fun newInstance() = PartnerContractReadFragment()
    }

    private lateinit var viewModel: PartnerContractReadViewModel

    private lateinit var binding : PartnerContractReadFragmentBinding


    private lateinit var contract : Contract

    private lateinit var viewPagerAdapter : ConciergeReadViewpagerAdapter

    private lateinit var navController: NavController

    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT.png?alt=media&token=8623aaa7-4f88-44fb-a05e-64d2a02cb683"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.partner_contract_read_fragment,container,false)
        myActivity.bottomNavigationGone()
        binding.back.setOnClickListener { onBackPressed() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PartnerContractReadViewModel::class.java)

        navController= findNavController()
        arguments?.let {
            contract = it.getParcelable<Contract>("contract")!!
            setDataView()
        }


    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


    fun setDataView(){
       binding.partnerContractReadName.text = contract.companyName
        binding.partnerContractReadInfo.text = "${contract.contractInfo} / ${contract.transaction}"
        binding.partnerContractReadPrice.text = contract.price.toString()
        val dateFormat = SimpleDateFormat("yyyy.MM.dd.")
        binding.partnerContractReadDate.text = "${dateFormat.format(contract.contractStartDate.toDate())}~${dateFormat.format(contract.contractEndDate.toDate())}"
        if(contract.images.size==0){
            var list = ArrayList<String>()
            list.add(defaultUrl)
            viewPagerAdapter = ConciergeReadViewpagerAdapter(requireContext(),list)
        }else{
            viewPagerAdapter = ConciergeReadViewpagerAdapter(requireContext(),contract.images)
        }

        binding.PartnerContractReadViewPager.adapter = viewPagerAdapter


        setCurrentOnboardingIndicator(0)

        setUpBoardingIndicators(contract.images.size)
        binding.PartnerContractReadViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })

    }

    private fun setUpBoardingIndicators(size: Int){
        binding.PartnerViewPagerReadIndicator.removeAllViews()
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
            binding.PartnerViewPagerReadIndicator?.addView(indicators[i])
        }
    }


    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.PartnerViewPagerReadIndicator?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.PartnerViewPagerReadIndicator?.getChildAt(i) as ImageView
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