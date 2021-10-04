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
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Contract
import com.moa.moakotlin.databinding.PartnerContractReadFragmentBinding
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PartnerContractReadFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerContractReadFragment()
    }

    private lateinit var viewModel: PartnerContractReadViewModel

    private lateinit var binding : PartnerContractReadFragmentBinding


    private lateinit var contract : Contract

    private lateinit var viewPagerAdapter : ConciergeReadViewpagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.partner_contract_read_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PartnerContractReadViewModel::class.java)


        arguments?.let {
            contract = it.getParcelable<Contract>("contract")!!
            setDataView()
        }


    }


    fun setDataView(){
       binding.partnerContractReadName.text = contract.companyName
        binding.partnerContractReadInfo.text = contract.contractInfo
        binding.partnerContractReadPrice.text = contract.price
        val dateFormat = SimpleDateFormat("yyyy.MM.dd.")
        binding.partnerContractReadDate.text = "${dateFormat.format(contract.contractStartDate.toDate())}~${dateFormat.format(contract.contractEndDate.toDate())}"
        viewPagerAdapter = ConciergeReadViewpagerAdapter(requireContext(),contract.images)
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