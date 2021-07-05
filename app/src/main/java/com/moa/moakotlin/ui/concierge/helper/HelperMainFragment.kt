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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.FragmentHelperMainBinding
import com.moa.moakotlin.recyclerview.concierge.HelperMainAdapter
import com.moa.moakotlin.viewpageradapter.HomeViewPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelperMainFragment : BaseFragment() {

        lateinit var binding : FragmentHelperMainBinding

        lateinit var navController: NavController
        lateinit var model: HelperMainViewModel
    var kidAdapter = HelperMainAdapter()
    var interiorAdapter = HelperMainAdapter()
    var etcAdapter= HelperMainAdapter()
    var educationAdapter = HelperMainAdapter()
    var petAdapter = HelperMainAdapter()
    lateinit var myActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_helper_main,container, false)

        navController = findNavController()

        model = ViewModelProvider(this).get(HelperMainViewModel::class.java)

        myActivity.bottomNavigationVisible()

        binding.HelperMainKidRcv.adapter = kidAdapter
        binding.HelperMainpetRcv.adapter = petAdapter
        binding.HelperMainEducationRcv.adapter =educationAdapter
        binding.HelperMainInteriorRcv.adapter =interiorAdapter
        binding.HelperMainEtcRcv.adapter = etcAdapter

        binding.HelperMainKidRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.HelperMainpetRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.HelperMainEducationRcv.layoutManager  = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.HelperMainInteriorRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.HelperMainEtcRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)

        binding.model = model

        binding.HelperMainKidAllBtn.setOnClickListener {
//             키드 데이터를 가지고 오는 viewmodel 메소드 호출 필요
            navController.navigate(R.id.categoryMainFragment)

        }

        initGetData(kidAdapter,"육아")
        initGetData(interiorAdapter,"인테리어")
        initGetData(petAdapter,"반려동물케어")
        initGetData(educationAdapter,"교육")
        initGetData(etcAdapter,"기타")
        setClickListener(kidAdapter)
        setClickListener(interiorAdapter)
        setClickListener(petAdapter)
        setClickListener(educationAdapter)
        setClickListener(etcAdapter)

        setViewPager()
        return binding.root
    }

    fun setViewPager(){
        var adapter = HomeViewPagerAdapter(arrayListOf(R.drawable.banner_help_main))

        binding.HelperMainBanner.adapter = adapter
        binding.HelperMainBanner.offscreenPageLimit =3

        binding.HelperMainBanner.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER

        setUpBoardingIndicators(arrayListOf(R.drawable.banner_help_main))

        setCurrentOnboardingIndicator(0)

        binding.HelperMainBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })
    }
//    fun initGetData(){
//
//        arguments?.let {
//            var map  = it.getSerializable("HelperData")!! as HashMap<String, ArrayList<Helper>>
//            kidAdapter.submitList(map.get("육아"))
//            interiorAdapter.submitList(map.get("인테리어"))
//            petAdapter.submitList(map.get("반려동물케어"))
//            etcAdapter.submitList(map.get("기타"))
//            educationAdapter.submitList(map.get("교육"))
//
//        }
//
//    }

    override fun onBackPressed( ) {
        navController.popBackStack()
    }
    fun initGetData(adapter: HelperMainAdapter,mainCategory : String){
        CoroutineScope(Dispatchers.Main).launch {
            var list = model.getData(mainCategory)
            adapter.submitList(list)
        }
    }

    fun setClickListener(adapter: HelperMainAdapter){
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    var writer = model.getWriterData(adapter.currentList[position].uid)
                    if(writer!=null){
                        var bundle = Bundle()
                        bundle.putParcelable("data",adapter.currentList[position])
                        bundle.putParcelable("writer",writer)
                        navController.navigate(R.id.HelperReadFragment,bundle)
                    }
                }
            }

        })
    }

    private fun setUpBoardingIndicators(list : ArrayList<Int>){
        val indicators =
                arrayOfNulls<ImageView>(list.size)

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

            binding.HelperMainIndicators?.addView(indicators[i])
        }
    }
    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.HelperMainIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.HelperMainIndicators?.getChildAt(i) as ImageView
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