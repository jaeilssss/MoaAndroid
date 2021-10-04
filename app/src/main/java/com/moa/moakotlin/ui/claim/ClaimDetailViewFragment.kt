package com.moa.moakotlin.ui.claim

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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.BottomNavController
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.custom.PrivateNoticeDialog
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.ClaimDetailViewFragmentBinding
import com.moa.moakotlin.recyclerview.complaint.ComplaintAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClaimDetailViewFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClaimDetailViewFragment()
    }

    private lateinit var viewModel: ClaimDetailViewViewModel


    private lateinit var binding: ClaimDetailViewFragmentBinding

    private lateinit var category : String

    private lateinit var adapter : ComplaintAdapter

    private lateinit var navController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (context as MainActivity).backListener = this
        binding = DataBindingUtil.inflate(inflater,R.layout.claim_detail_view_fragment,container,false)
        binding.back.setOnClickListener { navController.popBackStack() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimDetailViewViewModel::class.java)
        binding.model = viewModel

        arguments?.let {
            category = it.getString("type").toString()

            setData(category)
        }

        navController = findNavController()

        adapter = ComplaintAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {

                var complaint = adapter.currentList[position]

                if(complaint.isPrivate){
                    if(complaint.uid.equals(User.getInstance().uid).not()){
                        PrivateNoticeDialog(requireContext())
                                .setMessage("비공개 민원입니다")
                                .setPositiveButton("예"){

                                }.show()
                    }else{
                        goToReadView(adapter.currentList[position])
                    }
                }else{
                    goToReadView(adapter.currentList[position])
                }
            }

        })
        binding.ClaimDetailRcv.adapter = adapter
        binding.ClaimDetailRcv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.complaintList.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun goToReadView(complaint : Complaint){

        var bundle = Bundle()
        bundle.putParcelable("complaint",complaint)
        bundle.putBoolean("detail",true)
        navController.navigate(R.id.action_claimDetailViewFragment_to_claimReadFragment,bundle)

    }


    fun setData(type : String){
        CoroutineScope(Dispatchers.Main).launch {
                if(type.equals("All")){
                    viewModel.getDocumentList()
                }else{
                    viewModel.getDocumentListType(type)
                }
        }

    }
}