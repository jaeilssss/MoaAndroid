package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.NeederMainFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.HelperMainAdapter
import com.moa.moakotlin.recyclerview.concierge.NeederMainAdapter
import com.moa.moakotlin.viewpageradapter.HomeViewPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederMainFragment : BaseFragment() {

    private lateinit var viewModel: NeederMainViewModel

    private lateinit var binding : NeederMainFragmentBinding

    private lateinit var navController: NavController

    lateinit var myActivity : MainActivity

    var kidAdapter = NeederMainAdapter()
    var interiorAdapter = NeederMainAdapter()
    var etcAdapter= NeederMainAdapter()
    var educationAdapter = NeederMainAdapter()
    var petAdapter = NeederMainAdapter()
    var sharingAdapter = NeederMainAdapter()
    var borrowAdapter = NeederMainAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.needer_main_fragment,container,false)

        myActivity.bottomNavigationVisible()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederMainViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()

        setViewPager()
        myActivity.bottomNavigationVisible()

        binding.NeederMainKidRcv.adapter = kidAdapter
        binding.NeederMainpetRcv.adapter = petAdapter
        binding.NeederMainEducationRcv.adapter =educationAdapter
        binding.NeederMainInteriorRcv.adapter =interiorAdapter
        binding.NeederMainEtcRcv.adapter = etcAdapter
        binding.NeederMainSharingRcv.adapter = sharingAdapter
        binding.NeederMainBorrowRcv.adapter = borrowAdapter


        binding.NeederMainKidRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainpetRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEducationRcv.layoutManager  = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainInteriorRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEtcRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainBorrowRcv.layoutManager =  LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainSharingRcv.layoutManager =  LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)


        initGetData(kidAdapter,"육아")
        initGetData(interiorAdapter,"인테리어")
        initGetData(petAdapter,"반려동물케어")
        initGetData(educationAdapter,"교육")
        initGetData(etcAdapter,"기타")
        initGetData(borrowAdapter,"빌려주세요")
        initGetData(sharingAdapter,"품앗이")

        setClickListener(kidAdapter)
        setClickListener(interiorAdapter)
        setClickListener(petAdapter)
        setClickListener(educationAdapter)
        setClickListener(etcAdapter)
        setClickListener(borrowAdapter)
        setClickListener(sharingAdapter)

        setGoingAllCategory()
    }

    private fun setGoingAllCategory(){
        binding.NeederMainBorrowAllBtn.setOnClickListener { goToAllCategory("빌려주세요") }
        binding.NeederMaininteriorAllBtn.setOnClickListener { goToAllCategory("인테리어") }
        binding.NeederMainEduAllBtn.setOnClickListener { goToAllCategory("교육") }
        binding.NeederMainEtcAllBtn.setOnClickListener { goToAllCategory("기타") }
        binding.NeederMainKidAllBtn.setOnClickListener { goToAllCategory("육아") }
        binding.NeederMainPetAllBtn.setOnClickListener { goToAllCategory("반려동물케어") }
        binding.NeederMainSharingAllBtn.setOnClickListener { goToAllCategory("품앗이") }
    }

    private fun goToAllCategory(mainCategory : String){

        var bundle = Bundle()

        bundle.putString("mainCategory",mainCategory)
        navController.navigate(R.id.categoryNeederMainFragment,bundle)

    }
    override fun onBackPressed() {
        navController.popBackStack(R.id.ConciergeMainFragment,false)
    }

    fun setClickListener(adapter: NeederMainAdapter){
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    var writer = viewModel.getWriterData(adapter.currentList[position].uid)
                    var bundle = Bundle()
                    bundle.putParcelable("needer",adapter.currentList[position])
                    bundle.putParcelable("writer",writer)
                    navController.navigate(R.id.action_neederMainFragment_to_neederReadFragment,bundle)
                }

            }
        })
    }
    // 이 부분 수정해야함.. 어댑터를 하나 더 만들어야 함
    fun initGetData(adapter: NeederMainAdapter,mainCategory : String){
        CoroutineScope(Dispatchers.Main).launch {
            var list = viewModel.getData(mainCategory)
            if(list.size==0){
                var emptyNeeder = Needer()
                emptyNeeder.documentID = (-1).toString()
                list.add(emptyNeeder)
            }
            adapter.submitList(list)
        }
    }

    fun setViewPager(){
        var adapter = HomeViewPagerAdapter(arrayListOf(R.drawable.banner_help_main))

        binding.NeederMainBanner.adapter = adapter
        binding.NeederMainBanner.offscreenPageLimit =3

        binding.NeederMainBanner.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER

        setUpBoardingIndicators(arrayListOf(R.drawable.banner_help_main))

        setCurrentOnboardingIndicator(0)

        binding.NeederMainBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
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

            binding.NeederMainIndicators?.addView(indicators[i])
        }
    }
    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.NeederMainIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.NeederMainIndicators?.getChildAt(i) as ImageView
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