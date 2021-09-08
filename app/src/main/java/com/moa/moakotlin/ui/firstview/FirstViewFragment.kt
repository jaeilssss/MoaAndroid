package com.moa.moakotlin.ui.firstview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentFirstViewBinding
import com.moa.moakotlin.viewpageradapter.FirstViewPagerAdapter


class FirstViewFragment : BaseFragment() {
    lateinit var binding: FragmentFirstViewBinding
    var lastTimeBackPressed : Long = 0
    lateinit var viewModel: FirstViewViewModel

    lateinit var navController: NavController

   lateinit var adapter : FirstViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // 혹시 몰라서 User 인스터스 클리어


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_first_view,container,false)
        (context as MainActivity).backListener = this
        navController =  findNavController()
        viewModel = context?.let {FirstViewViewModel(navController,it)}!!
        binding.model = viewModel
        myActivity.bottomNavigationGone()
        adapter = FirstViewPagerAdapter(childFragmentManager)

        binding.FirstViewFragmentViewPager.adapter = adapter
        setUpBoardingIndicators()
        setCurrentOnboardingIndicator(0)

        binding.FirstViewFragmentViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
     return binding.root
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            activity?.finish()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(context,"종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show()
    }

    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.indicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.indicators?.getChildAt(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(activity?.applicationContext!!,
                    R.drawable.onboarding_indicator_active))
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(activity?.applicationContext!!,
                    R.drawable.onboarding_indicator_inactive))
            }
        }
    }

    private fun setUpBoardingIndicators(){
        val indicators =
            arrayOfNulls<ImageView>(3)

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
            binding.indicators?.addView(indicators[i])
        }
    }

}