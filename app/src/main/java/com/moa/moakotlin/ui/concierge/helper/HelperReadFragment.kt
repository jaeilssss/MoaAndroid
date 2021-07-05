package com.moa.moakotlin.ui.concierge.helper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentHelperReadBinding
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter


class HelperReadFragment : Fragment() {

    lateinit var binding : FragmentHelperReadBinding

    lateinit var navController: NavController

    lateinit var model: HelperReadViewModel

    lateinit var kid : Kid
    lateinit var  bundle : Bundle
    var data = Helper()
    var writer = User()
    var myActivity = MainActivity()
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
            data = it.getParcelable<Helper>("data")!!
            writer = it.getParcelable<User>("writer")!!

        }

        setCurrentOnboardingIndicator(0)
        setUpFragment(ConciergeReadIntroduceFragment(data))


        setWriterInfo()
        setUpBoardingIndicators(data?.images!!.size)
        setHelperData()

        var adapter = data.images?.let { it1 -> ConciergeReadViewpagerAdapter(activity?.applicationContext!!, it1) }

        binding.HelperReadViewPager.adapter = adapter


        binding.HelperMainIntroduce.setOnClickListener {
            setUpFragment(ConciergeReadIntroduceFragment(data))
        }
        binding.HelperMainReview.setOnClickListener {
            setUpFragment(HelperReadReviewFragment())
        }

        binding.HelperReadViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })
        return binding.root
    }
    private fun setUpBoardingIndicators(size : Int){
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
    private fun setHelperData(){
        binding.HelperReadMainCategory.text = data.mainCategory
        binding.HelperMainTitle.text = data.title
    }
    private fun setWriterInfo(){
        binding.HelperReadNickName.text = writer.nickName
        binding.HelperReadAptName.text = writer.aptName
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
}