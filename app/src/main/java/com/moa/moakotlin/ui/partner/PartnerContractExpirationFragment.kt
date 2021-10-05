package com.moa.moakotlin.ui.partner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.PartnerContractExpirationFragmentBinding
import com.moa.moakotlin.recyclerview.partner.PartnerContractAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PartnerContractExpirationFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerContractExpirationFragment()
    }

    private lateinit var viewModel: PartnerContractExpirationViewModel

    private lateinit var binding : PartnerContractExpirationFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapter : PartnerContractAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.partner_contract_expiration_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PartnerContractExpirationViewModel::class.java)
        setDataView()
        adapter = PartnerContractAdapter()
        binding.partnerContractExRcv.adapter = adapter
        binding.partnerContractExRcv.layoutManager = LinearLayoutManager(requireContext())
        navController = findNavController()
        viewModel.contractList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var bundle = Bundle()
                bundle.putParcelable("contract",adapter.currentList[position])
                navController.navigate(R.id.action_partnerContractExpirationFragment_to_partnerContractReadFragment,bundle)
            }

        })


    }
    fun setDataView(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDocumentList()
        }
    }

}