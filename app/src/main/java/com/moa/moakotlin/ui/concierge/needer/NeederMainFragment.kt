package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
import android.content.Intent
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
import com.moa.moakotlin.WebViewActivity
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Banner
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

//    lateinit var myActivity : MainActivity

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
        (context as MainActivity).backListener = this
        myActivity.bottomNavigationVisible()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederMainViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()

        setBanner()
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
        binding.back.setOnClickListener { onBackPressed() }

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

        binding.NeederMainSwipeRefreshLayout.setOnRefreshListener {

            initGetData(kidAdapter,"육아")
            initGetData(interiorAdapter,"인테리어")
            initGetData(petAdapter,"반려동물케어")
            initGetData(educationAdapter,"교육")
            initGetData(etcAdapter,"기타")
            initGetData(borrowAdapter,"빌려주세요")
            initGetData(sharingAdapter,"품앗이")

            binding.NeederMainKidRcv.scrollToPosition(0)
            binding.NeederMainSwipeRefreshLayout.isRefreshing = false


        }

    }


    fun setFocusing(){
        binding.NeederMainBorrowRcv.scrollToPosition(0)
        binding.NeederMainSharingRcv.scrollToPosition(0)
        binding.NeederMainEducationRcv.scrollToPosition(0)
        binding.NeederMainEtcRcv.scrollToPosition(0)
        binding.NeederMainInteriorRcv.scrollToPosition(0)
        binding.NeederMainKidRcv.scrollToPosition(0)
        binding.NeederMainpetRcv.scrollToPosition(0)
    }
    private fun setGoingAllCategory(){
        binding.NeederMainBorrowAllBtn.setOnClickListener {
            if(borrowAdapter.currentList[0].documentID.equals("-1").not())
            goToAllCategory("빌려주세요") }
        binding.NeederMaininteriorAllBtn.setOnClickListener {
            if(interiorAdapter.currentList[0].documentID.equals("-1").not())
                goToAllCategory("인테리어") }
        binding.NeederMainEduAllBtn.setOnClickListener {
            if(educationAdapter.currentList[0].documentID.equals("-1").not())
            goToAllCategory("교육") }
        binding.NeederMainEtcAllBtn.setOnClickListener {
            if(etcAdapter.currentList[0].documentID.equals("-1").not())
            goToAllCategory("기타") }
        binding.NeederMainKidAllBtn.setOnClickListener {
            if(kidAdapter.currentList[0].documentID.equals("-1").not())
            goToAllCategory("육아") }
        binding.NeederMainPetAllBtn.setOnClickListener {
            if(petAdapter.currentList[0].documentID.equals("-1").not())
            goToAllCategory("반려동물케어") }
        binding.NeederMainSharingAllBtn.setOnClickListener {
            if(sharingAdapter.currentList[0].documentID.equals("-1").not())
            goToAllCategory("품앗이") }
    }

    private fun goToAllCategory(mainCategory : String){

        var bundle = Bundle()

        bundle.putString("mainCategory",mainCategory)

        navController.navigate(R.id.action_neederMainFragment_to_categoryNeederMainFragment ,bundle)

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


                if(mainCategory.equals("육아")){
                    binding.NeederMainKidRcv.scrollToPosition(0)
                }else if(mainCategory.equals("교육")){
                    binding.NeederMainEducationRcv.scrollToPosition(0)
                }else if(mainCategory.equals("인테리어")){
                    binding.NeederMainInteriorRcv.scrollToPosition(0)
                }else if(mainCategory.equals("반려동물케어")){
                    binding.NeederMainpetRcv.scrollToPosition(0)
                }else if(mainCategory.equals("빌려주세요")){
                    binding.NeederMainBorrowRcv.scrollToPosition(0)
                }else if(mainCategory.equals("품앗이")){
                    binding.NeederMainSharingRcv.scrollToPosition(0)
                }else if(mainCategory.equals("기타")){
                    binding.NeederMainEtcRcv.scrollToPosition(0)
                }


        }
    }

    fun setBanner(){
        CoroutineScope(Dispatchers.Main).launch {
          var list = viewModel.getBanner()
            var adapter = HomeViewPagerAdapter(list)

            binding.NeederMainBanner.adapter = adapter
//            binding.NeederMainBanner.offscreenPageLimit =3

//            binding.NeederMainBanner.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER

            setUpBoardingIndicators(list)

            setCurrentOnboardingIndicator(0)

            adapter.setOnItemClickListener(object :OnItemClickListener{
                override fun onItemClick(v: View, position: Int) {
                    adapter.setOnItemClickListener(object :OnItemClickListener{
                        override fun onItemClick(v: View, position: Int) {
                            var intent = Intent(activity, WebViewActivity::class.java)

                            intent.putExtra("url",adapter.list[position].url)

                            startActivity(intent)
                        }

                    })
                }
            })
            binding.NeederMainBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setCurrentOnboardingIndicator(position)
                }
            })

        }

    }
    private fun setUpBoardingIndicators(list : ArrayList<Banner>){
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