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
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.PartnerNoticeFragmentBinding
import com.moa.moakotlin.recyclerview.partner.PartnerNoticeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PartnerNoticeFragment(var navController: NavController) : Fragment() {



    private lateinit var viewModel: PartnerNoticeViewModel

    private lateinit var adapter : PartnerNoticeAdapter

    private lateinit var binding : PartnerNoticeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.partner_notice_fragment , container ,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PartnerNoticeViewModel::class.java)
        binding.model = viewModel

        adapter = PartnerNoticeAdapter()

        binding.PartnerNoticeRcv.adapter = adapter

        binding.PartnerNoticeRcv.layoutManager = LinearLayoutManager(requireContext())


        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var bundle = Bundle()
                bundle.putParcelable("notice",adapter.currentList[position])
                navController.navigate(R.id.action_partnerNoticeMainFragment_to_partnerNoticeReadFragment,bundle)
            }
        })
        setDataView()
        viewModel.noticeList.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)

        })

    }

    fun setDataView(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDocumentList()
        }

    }

}