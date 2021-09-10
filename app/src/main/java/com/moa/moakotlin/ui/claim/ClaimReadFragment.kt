package com.moa.moakotlin.ui.claim

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.ClaimReadFragmentBinding
import com.moa.moakotlin.viewpageradapter.ConciergeReadViewpagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClaimReadFragment : Fragment() {

    companion object {
        fun newInstance() = ClaimReadFragment()
    }

    private lateinit var viewModel: ClaimReadViewModel

    private lateinit var binding : ClaimReadFragmentBinding

    private lateinit var complaint : Complaint

    private lateinit var viewPagerAdapter : ConciergeReadViewpagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.claim_read_fragment,container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimReadViewModel::class.java)
        binding.model = viewModel
        arguments?.let {
            complaint = it.getParcelable<Complaint>("complaint")!!
        }
        setViewData()
        viewPagerAdapter = context?.let { ConciergeReadViewpagerAdapter(it,complaint.images) }!!
        binding.ClaimReadViewPager.adapter = viewPagerAdapter

        // TODO: Use the ViewModel
    }


    fun setViewData(){
        binding.ClaimReadTitleText.text = complaint.title
        binding.ClaimReadContentText.text = complaint.content
        binding.ClaimReadCategoryText.text = complaint.category
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

    }


}