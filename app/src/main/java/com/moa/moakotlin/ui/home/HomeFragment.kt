package com.moa.moakotlin.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.MyPhone
import com.moa.moakotlin.R
import com.moa.moakotlin.WebViewActivity
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.base.onBackPressedListener
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.data.Banner
import com.moa.moakotlin.data.Megazin
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentHomeBinding
import com.moa.moakotlin.recyclerview.home.MegazinAdapter
import com.moa.moakotlin.viewpageradapter.HomeViewPagerAdapter
import kotlinx.android.synthetic.main.item_moa_megazin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment(){
    var lastTimeBackPressed : Long = 0
lateinit var transfer: Transfer

    lateinit var binding: FragmentHomeBinding

    val groupUrl = "https://m.post.naver.com/viewer/postView.naver?volumeNo=32031386&memberNo=25160931"
    lateinit var navController: NavController
    var megazinList = ArrayList<Megazin>()
    var megazinAdapter = MegazinAdapter()

    lateinit var model: HomeViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null){
            transfer = activity as Transfer
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        (context as MainActivity).backListener = this
        navController = findNavController()

        model = ViewModelProvider(this).get(HomeViewModel::class.java)

        model.init()

        binding.UserAptName.text = "${User.getInstance().aptName} ${User.getInstance().nickName}님"

        var list = ArrayList<Int>()

        binding.model = model

        transfer.bottomVisible()

        binding.homeMegazinRcv.adapter = megazinAdapter

        megazinAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(activity,WebViewActivity::class.java)

                intent.putExtra("url",megazinAdapter.currentList[position].url)

                startActivity(intent)
            }
        })
        binding.homeMegazinRcv.layoutManager = GridLayoutManager(activity?.applicationContext!!,2)

        binding.homeTalentSharingBnt.setOnClickListener { navController.navigate(R.id.ConciergeMainFragment) }
        binding.homeMoaVoiceChatBtn.setOnClickListener { navController.navigate(R.id.voiceMainFragment) }
        binding.homeClaimBtn.setOnClickListener { homeClaimAlertDialog() }
        binding.homeGroupBuyingBtn.setOnClickListener{goToGroupBuyingBtn()}


        getMoaMagazine()
        getHomeBanner()

        return binding.root
    }

    fun goToGroupBuyingBtn(){
        var intent = Intent(activity,WebViewActivity::class.java)

        intent.putExtra("url",groupUrl)

        startActivity(intent)
    }
    fun homeClaimAlertDialog(){
        context?.let {
            SinglePositiveButtonDialog(it)
                .setMessage("민원신청 기능은 아파트와 계약이 필요해요\n관리 사무소에 문의해주세요")
                .setPositiveButton("확인"){

                }.show()
        }
    }
    fun setOnClickListener(){

    }
    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            activity?.finish()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(context,"종료하려면 한번 더 누르세요",Toast.LENGTH_SHORT).show()
    }

    private fun setUpBoardingIndicators(list : ArrayList<Banner>){
        binding.indicators?.removeAllViews()
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
            binding.indicators?.addView(indicators[i])
        }
    }
    private fun setCurrentOnboardingIndicator(index : Int){
        var childCount = binding.indicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.indicators?.getChildAt(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_inactive))
            }
        }
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    fun getMoaMagazine(){
        CoroutineScope(Dispatchers.Main).launch {
            model.getMoaMagazine()
        }
       model.magazineList.observe(viewLifecycleOwner, Observer {
           megazinAdapter.submitList(it)
       })

    }

    fun getHomeBanner(){
        CoroutineScope(Dispatchers.Main).launch {
            model.getHomeBanner()

        }
        model.homeBannerList.observe(viewLifecycleOwner, Observer {
            setBannerViewPager(it)

        })
    }

    fun setBannerViewPager(list : ArrayList<Banner>){
        var adapter = HomeViewPagerAdapter(list)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.homeViewPager.adapter = adapter

//        binding.homeViewPager.offscreenPageLimit =3

//        binding.homeViewPager.getChildAt(0).overScrollMode=View.OVER_SCROLL_ALWAYS

        setUpBoardingIndicators(list)

        setCurrentOnboardingIndicator(0)

        binding.homeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })

        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(activity,WebViewActivity::class.java)

                intent.putExtra("url",adapter.list[position].url)

                startActivity(intent)

            }

        })
    }
}