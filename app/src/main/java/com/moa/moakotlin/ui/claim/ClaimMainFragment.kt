package com.moa.moakotlin.ui.claim

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.PartnerApart
import com.moa.moakotlin.databinding.ClaimMainFragmentBinding
import com.moa.moakotlin.recyclerview.complaint.ComplaintAdapter
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

    private lateinit var adapter : ComplaintAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (context as MainActivity).backListener = this
        binding = DataBindingUtil.inflate(inflater,R.layout.claim_main_fragment,container,false)
        binding.ClaimMainReceiveLayout.setOnClickListener { goToDetailView("requested") }
        binding.ClaimMainOnGoingLayout.setOnClickListener { goToDetailView("inProgress") }
        binding.ClaimMainCompleteLayout.setOnClickListener { goToDetailView("completed") }
        binding.ClaimMainGotoAllContent.setOnClickListener { goToDetailView("All") }
        binding.back.setOnClickListener { navController.popBackStack() }
        binding.ClaimMainWriteBtn.setOnClickListener { navController.navigate(R.id.action_claimMainFragment_to_claimWriteFragment) }
        binding.back.setOnClickListener { onBackPressed() }

        myActivity?.bottomNavigationGone()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ClaimMainViewModel::class.java)

        navController = findNavController()

        arguments?.let {
            partnerApart = it.getParcelable<PartnerApart>("partnerApart")!!
        }
        adapter = ComplaintAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var bundle = Bundle()
                bundle.putParcelable("complaint",adapter.currentList[position])
                bundle.putBoolean("detail",false)
                navController.navigate(R.id.action_claimMainFragment_to_ClaimReadFragment,bundle)
            }
        })

        binding.ClaimMainRcv.adapter = adapter
        binding.ClaimMainRcv.layoutManager = LinearLayoutManager(context)

        viewModel.complaintList.observe(viewLifecycleOwner, Observer {
            if(it.size==0){
                binding.ClaimMainRcv.isVisible = false
                binding.ClaimMainEmptyLayout.isVisible =true
            }else{
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })
        setDataView()
        getMyClaimList()
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun goToDetailView(type : String){
        var bundle = Bundle()
        bundle.putString("type",type)
        navController.navigate(R.id.action_claimMainFragment_to_claimDetailViewFragment,bundle)
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