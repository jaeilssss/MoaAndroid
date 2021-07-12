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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.costumdialog.CostumAlertDialog
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.NeederReadFragmentBinding
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerViewAdapter
import com.moa.moakotlin.ui.bottomsheet.ConciergeReadBottomSheetFragment
import com.moa.moakotlin.ui.bottomsheet.NeederHireStatusBottomSheet
import com.moa.moakotlin.ui.concierge.ConciergeWriteActivity
import com.moa.moakotlin.ui.concierge.helper.ConciergeReadIntroduceFragment
import com.moa.moakotlin.ui.concierge.helper.HelperReadFragment
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class NeederReadFragment : BaseFragment() {


    private lateinit var viewModel: NeederReadViewModel

    private lateinit var myActivity : MainActivity

    private lateinit var navController: NavController

    private lateinit var binding: NeederReadFragmentBinding
    private lateinit var needer : Needer
    private lateinit var writer : User
    private lateinit var adapter : ConciergeReadViewpagerAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }

    companion object{
        var REQUEST_MODIFY_CODE = 4000
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.needer_read_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederReadViewModel::class.java)
        navController = findNavController()
        myActivity.bottomNavigationGone()
        binding.model = viewModel



        arguments?.let {
            needer = it.getParcelable<Needer>("needer")!!
            writer = it.getParcelable<User>("writer")!!
            // 서브 카테고리는 어디서 보여주지??...
        }

        binding.NeederReadChatBtn.setOnClickListener { goToChat() }
        binding.NeederReadGearImg.setOnClickListener {
            val option = NeederHireStatusBottomSheet{
                hireCompleteDialog()
        }
            option.show(activity?.supportFragmentManager!!,"bottomsheet")

        }
        binding.NeederReadOption.setOnClickListener {
            val option : ConciergeReadBottomSheetFragment = ConciergeReadBottomSheetFragment {
                when(it){
                    0 -> {
                        goToModify()
                    }
                    1 -> {
                        customDialog()
                    }
                }

            }
            option.show(activity?.supportFragmentManager!!,"bottomsheet")
        }


        viewModel.roomId.observe(viewLifecycleOwner, Observer {
            var bundle = Bundle()
            bundle.putString("roomId",it)
            bundle.putParcelable("opponentUser",writer)
            bundle.putParcelable("Needer",needer)
            navController.navigate(R.id.ChatFragment,bundle)

        })
        adapter = needer.images?.let { it1 -> ConciergeReadViewpagerAdapter(activity?.applicationContext!!, it1) }!!

        binding.NeederReadViewPager.adapter = adapter
        binding.NeederReadViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })
        setUpBoardingIndicators(needer?.images!!.size)
        setCurrentOnboardingIndicator(0)
        setUpFragment(ConciergeReadIntroduceFragment(null,needer))
        setWriterInfo()
        setNeederData()
        checkVisible()

    }


    private fun checkVisible(){
        if(needer.uid != User.getInstance().uid){
            binding.NeederReadOption.isVisible = false
            binding.NeederReadGearImg.isVisible = false
        }else{
            binding.NeederReadChatLayout.isVisible = false
        }
        if(needer.hireStatus.equals("모집완료")){
            binding.NeederReadGearImg.isVisible = false
        }
    }
    private fun setWriterInfo(){
        binding.NeederReadNickName.text = writer?.nickName
    }
    private fun setNeederData(){
        binding.NeederMainTitle.text = needer.title
        binding.NeederReadNickName.text = writer?.nickName
        binding.NeederReadMainCategory.text = "${needer.mainCategory} / ${needer.subCategory}"
        binding.NeederReadHopeDate.text = needer.hopeDate
        binding.NeederReadHireStatusText.text = needer.hireStatus
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm ")
        binding.NeederReadDate.text=dateFormat.format(needer.timeStamp.toDate())
    }
    fun setUpFragment(fragment : Fragment){
        val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.NeederMainFragmentView, fragment).commit()
    }
    override fun onBackPressed() {
        navController.popBackStack()
    }
    private fun setUpBoardingIndicators(size : Int){
        binding.NeederReadIndicators.removeAllViews()
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
            binding.NeederReadIndicators?.addView(indicators[i])
        }
    }

    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.NeederReadIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.NeederReadIndicators?.getChildAt(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(activity?.applicationContext!!,
                        R.drawable.onboarding_indicator_inactive))
            }
        }
    }

    fun goToModify(){
        var intent = Intent(activity, ConciergeWriteActivity::class.java)
        intent.putExtra("Needer",needer)
        startActivityForResult(intent , REQUEST_MODIFY_CODE)
    }

    private fun customDialog(){
        context?.let {
            CostumAlertDialog(it)
                    .setMessage("게시글을 정말 삭제하시겠습니까?")
                    .setPositiveButton("예"){
                        CoroutineScope(Dispatchers.Main).launch {
                            if(viewModel.delete(needer.mainCategory,needer.documentID!!)){
                                showToast(activity?.applicationContext!!,"삭제가 완료되었습니다")
                                navController.popBackStack()
                            }
                        }
                    }
                    .setNegativeButton {

                    }.show()
        }
    }

   private fun hireCompleteDialog(){
        context?.let {
            CostumAlertDialog(it)
                .setMessage("모집을 완료하시겠습니까??")
                .setPositiveButton("예"){
                        goToReview()
                }
                .setNegativeButton {

                }.show()
        }
    }

    fun goToChat(){
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getChattingRoom(needer.uid)
            }
    }
    fun goToReview(){
        var bundle = Bundle()
        bundle.putParcelable("Needer",needer)
        navController.navigate(R.id.action_neederReadFragment_to_neederCompletionFragment,bundle)
    }
}