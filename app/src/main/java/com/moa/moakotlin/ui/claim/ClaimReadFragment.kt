package com.moa.moakotlin.ui.claim

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.BottomNavController
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.data.Comment
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.ClaimReadFragmentBinding
import com.moa.moakotlin.recyclerview.comment.CommentAdapter
import com.moa.moakotlin.ui.bottomsheet.ConciergeReadBottomSheetFragment
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ClaimReadFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClaimReadFragment()
    }

    private lateinit var viewModel: ClaimReadViewModel

    private lateinit var binding : ClaimReadFragmentBinding

    private lateinit var complaint : Complaint

    private lateinit var adapter  : CommentAdapter

    private lateinit var viewPagerAdapter : ConciergeReadViewpagerAdapter

    private lateinit var navController: NavController

    private  var checkDetailView  = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (context as MainActivity).backListener = this
        binding = DataBindingUtil.inflate(inflater,R.layout.claim_read_fragment,container, false)

        binding.ChatSend.setOnClickListener {
            if(viewModel.comment.value!!.isNotEmpty()){
                CoroutineScope(Dispatchers.Main).launch {
                   if( viewModel.sendComment(complaint.documentId,User.getInstance().aptCode)){
                       getComment(complaint.documentId)
                       viewModel.comment.value = ""
                       binding.ChatEdit.text .clear()
                       binding.ClaimReadCommentRcv.scrollToPosition(adapter.currentList.size-1)
                       Toast.makeText(context,"댓글 작성이 완료됬습니다!",Toast.LENGTH_SHORT).show()
                       val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                       inputMethodManager.hideSoftInputFromWindow(binding.ChatEdit.windowToken, 0)
                   }
                }
            }
        }

        binding.ClaimReadOption.setOnClickListener {
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
        binding.back.setOnClickListener { navController.popBackStack() }

        return binding.root
    }

    private fun customDialog() {
        AptCertificationImageAlertDialog(requireContext()).
                setMessage("게시글 을 삭제하시겠습니까?")
                .setPositiveButton("네"){
                    CoroutineScope(Dispatchers.Main).launch {
                        if(viewModel.delete(complaint)){
                            Toast.makeText(requireContext(),"삭제가 완료되었습니다!",Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                }.setNegativeButton {

                }.show()
    }


    fun goToModify() {
        var bundle = Bundle()

        bundle.putParcelable("complaint",complaint)
        navController.navigate(R.id.action_claimReadFragment_to_claimModifyFragment,bundle)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimReadViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        arguments?.let {
            complaint = it.getParcelable<Complaint>("complaint")!!
            checkDetailView = it.getBoolean("detail")

        }

        setViewData()
        viewPagerAdapter = context?.let { ConciergeReadViewpagerAdapter(it,complaint.images) }!!
        binding.ClaimReadViewPager.adapter = viewPagerAdapter
        adapter = CommentAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                when(v.id){
                    R.id.commentDelete ->deleteComment(position)

                }
            }

        })
        binding.ClaimReadCommentRcv.adapter = adapter

        binding.ClaimReadCommentRcv.layoutManager = LinearLayoutManager(context)
        setCurrentOnboardingIndicator(0)
        setUpBoardingIndicators(complaint.images.size)
        binding.ClaimReadViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }
        })

        getComment(complaint.documentId)
    }

    override fun onBackPressed() {

        if(checkDetailView){
            navController.popBackStack()
        }else{
            navController.popBackStack(R.id.claimMainFragment,false)
        }
    }

    fun deleteComment(position : Int){
        context?.let { it1 ->
            AptCertificationImageAlertDialog(it1)
                    .setMessage("댓글을 삭제 하시겠습니까?")
                    .setPositiveButton("네"){
                        CoroutineScope(Dispatchers.Main).launch {
                            if(viewModel.deleteComment(complaint.documentId,User.getInstance().aptCode,adapter.currentList[position].documentId)){
                                Toast.makeText(context, "댓글이 삭제되었습니다",Toast.LENGTH_SHORT).show()
                                getComment(complaint.documentId)
                            }
                        }
                    }
                    .setNegativeButton {

                    }.show()
        }
    }
    fun getComment(documentId : String){
        CoroutineScope(Dispatchers.Main).launch {
            var list = viewModel.getComment(documentId,User.getInstance().aptCode)
            adapter.submitList(list)
        }
    }

    fun setViewData(){
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm ")

        binding.ClaimReadDate.text = dateFormat.format(complaint.timeStamp.toDate())
        binding.ClaimReadTitleText.text = complaint.title
        binding.ClaimReadContentText.text = complaint.content
        binding.ClaimReadCategoryText.text = complaint.category
        if(complaint.status.equals("requested")){
            binding.ClaimStatusText.text = "요청중"
        }else if(complaint.status.equals("inProgress")){
            binding.ClaimStatusText.text = "진행중"
        }else{
            binding.ClaimStatusText.text = "완료"
        }

        CoroutineScope(Dispatchers.Main).launch {
            if(complaint.uid.equals(User.getInstance().uid)){
                binding.ClaimReadNickName.text = User.getInstance().nickName
                Glide.with(binding.root).load(User.getInstance().profileImage)
                        .into(binding.ClaimReadProfileImage)

            }else{
                var user = viewModel.getWriterUser(complaint.uid)
                if(user!=null){
                    binding.ClaimReadNickName.text = user.nickName
                    Glide.with(binding.root).load(user.profileImage)
                            .into(binding.ClaimReadProfileImage)
                }

                binding.ClaimReadOption.isVisible = false
            }

        }

        if(complaint.uid.equals(User.getInstance().uid).not()){
            binding.ClaimEditLayout.isVisible = false
        }
    }

    private fun setUpBoardingIndicators(size : Int){
        binding.ClaimReadIndicators.removeAllViews()
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
            binding.ClaimReadIndicators?.addView(indicators[i])
        }
    }

    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = binding.ClaimReadIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = binding.ClaimReadIndicators?.getChildAt(i) as ImageView
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