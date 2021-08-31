package com.moa.moakotlin.ui.claim

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.BottomNavController
import com.moa.moakotlin.data.PartnerApart
import com.moa.moakotlin.databinding.ClaimMainFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClaimMainFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClaimMainFragment()
    }

    private lateinit var viewModel: ClaimMainViewModel

    private lateinit var binding : ClaimMainFragmentBinding

    private lateinit var navController: NavController

    private lateinit var partnerApart : PartnerApart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.claim_main_fragment,container,false)
        binding.ClaimMainReceiveLayout.setOnClickListener {  }
        binding.ClaimMainOnGoingLayout.setOnClickListener {  }
        binding.ClaimMainCompleteLayout.setOnClickListener {  }
        binding.back.setOnClickListener { onBackPressed() }


        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this).get(ClaimMainViewModel::class.java)

        navController = findNavController()
        arguments?.let {
            partnerApart = it.getParcelable<PartnerApart>("partnerApart")!!
        }

        viewModel.complaintList.observe(viewLifecycleOwner, Observer {
            if(it.size==0){
                binding.ClaimMainRcv.isVisible = false
                binding.ClaimMainEmptyLayout.isVisible =true
            }
        })
        
        setDataView()
        getMyClaimList()
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun getMyClaimList(){

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getMyClaimList()
        }


    }

    fun setDataView(){

        binding.ClaimMainCompleteCounting.text = "${partnerApart.completed} 건"
        binding.ClaimMainOnGoingCounting.text = "${partnerApart.inProgress} 건"
        binding.ClaimMainReceiveCounting.text = "${partnerApart.requested} 건"

        binding.ClaimMainMyAptText.text = "${partnerApart.aptName}"

    }

}