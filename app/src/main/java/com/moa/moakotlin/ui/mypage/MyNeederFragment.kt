package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.MyNeederFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.CategoryNeederMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyNeederFragment : BaseFragment() {


    private lateinit var viewModel: MyNeederViewModel

    private lateinit var binding : MyNeederFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapter: CategoryNeederMainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.my_needer_fragment,container , false)

        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNeederViewModel::class.java)

        navController = findNavController()
        viewModel.initData()
        setAdapter()
        binding.MyNeederRcv.adapter = adapter
        binding.MyNeederRcv.layoutManager = LinearLayoutManager(context)

        viewModel.myNeederDataList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun setAdapter(){
        adapter = CategoryNeederMainAdapter()

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    var writer = viewModel.getUserInfo(adapter.currentList[position].uid)

                    var bundle = Bundle()

                    bundle.putParcelable("writer",writer)
                    bundle.putParcelable("needer",adapter.currentList[position])

                    navController.navigate(R.id.action_myConciergeListFragment_to_neederReadFragment,bundle)

                }
            }

        })
    }

}