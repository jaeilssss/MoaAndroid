package com.moa.moakotlin.ui.concierge.helper

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.CategoryMainFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.CategoryHelperMainAdapter
import com.moa.moakotlin.recyclerview.concierge.CategoryNeederMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryMainFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoryMainFragment()
    }

    private lateinit var viewModel: CategoryMainViewModel

    private lateinit var binding : CategoryMainFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapterHelper : CategoryHelperMainAdapter
    private var helperList = ArrayList<Helper>()
    private lateinit var mainCategory : String
    var lastSize = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.category_main_fragment,container,false)
        (context as MainActivity).backListener = this
        myActivity.bottomNavigationGone()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myActivity.bottomNavigationGone()
        viewModel = ViewModelProvider(this).get(CategoryMainViewModel::class.java)
        adapterHelper = CategoryHelperMainAdapter()
        binding.CategoryMainRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        binding.CategoryMainRcv.adapter = adapterHelper
        adapterHelper.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.back.setOnClickListener {
            navController.popBackStack()
        }

        navController = findNavController()

        arguments.let {
             mainCategory = it?.getString("mainCategory")!!
                getList(mainCategory)
                binding.CategoryMainText.text = mainCategory
        }

        viewModel.neederList.observe(viewLifecycleOwner, Observer {
            helperList = it
            var newDataSize = it.size
            adapterHelper.submitList(it)
            adapterHelper.notifyDataSetChanged()

            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            binding.CategoryMainSwipeRefreshLayout.isRefreshing = false
        })

        setAdapterClickListener()
        onScrollListener(binding.CategoryMainRcv,adapterHelper)

    binding.CategoryMainSwipeRefreshLayout.setOnRefreshListener {
        myActivity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        getList(mainCategory)

    }

        binding.back.setOnClickListener { navController.popBackStack() }
    }



    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun setAdapterClickListener(){
        adapterHelper.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                when(v.id){
                    R.id.CategoryHelperLayout ->{
                        CoroutineScope(Dispatchers.Main).launch {
                            var writer = viewModel.getWriterData(adapterHelper.currentList[position].uid)
                            if(writer!=null){
                                var bundle = Bundle()
                                bundle.putParcelable("data",adapterHelper.currentList[position])
                                bundle.putParcelable("writer",writer)
                                viewModel.lastPosition = position
                                navController.navigate(R.id.HelperReadFragment,bundle)
                            }
                        }
                    }
                }
            }
        })
    }
    fun getList(mainCategory : String){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.list = viewModel.getList(mainCategory)
            viewModel.neederList.value = viewModel.list

        }
    }
    fun onScrollListener(rcv: RecyclerView, adapter: CategoryHelperMainAdapter){
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                var firstCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                var lastCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == adapter.itemCount-1){
                    if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.Scrolling(adapter.currentList[0].mainCategory,
                                adapter.currentList[adapter.itemCount-1].timeStamp)

                    }
                }
            }
        })
    }
}