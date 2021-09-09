package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.CategoryNeederMainFragmentBinding
import com.moa.moakotlin.recyclerview.chat.ChatAdapter
import com.moa.moakotlin.recyclerview.concierge.CategoryHelperMainAdapter
import com.moa.moakotlin.recyclerview.concierge.CategoryNeederMainAdapter
import com.moa.moakotlin.recyclerview.concierge.NeederMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryNeederMainFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoryNeederMainFragment()
    }

    private lateinit var viewModel: CategoryNeederMainViewModel

    private lateinit var binding : CategoryNeederMainFragmentBinding

    private lateinit var navController: NavController

//    private lateinit var myActivity  : MainActivity

    private  var adapterNeeder = CategoryNeederMainAdapter()

    private var neederList = ArrayList<Needer>()

    var mainCategory= ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.category_needer_main_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryNeederMainViewModel::class.java)
        binding.CategoryNeederMainRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        binding.CategoryNeederMainRcv.adapter = adapterNeeder
        adapterNeeder.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.back.setOnClickListener {
            navController.popBackStack()
        }

        navController = findNavController()
        viewModel.neederList.observe(viewLifecycleOwner, Observer {
            neederList = it
            var newDataSize = it.size

            adapterNeeder.submitList(it)
            adapterNeeder.notifyDataSetChanged()
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            binding.CategoryNeederMainSwipeRefreshLayout.isRefreshing = false
        })
        onScrollListener(binding.CategoryNeederMainRcv,adapterNeeder)
        setAdapterClickListener()
        arguments.let {
             mainCategory = it?.getString("mainCategory")!!
            if (mainCategory != null) {
                getList(mainCategory)
                binding.CategoryNeederMainText.text = mainCategory
            }
        }

        binding.CategoryNeederMainSwipeRefreshLayout.setOnRefreshListener {
            myActivity?.getWindow()?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                getList(mainCategory)

        }
    }

    override fun onBackPressed() {
        navController.popBackStack(R.id.neederMainFragment,false)
    }

    fun setAdapterClickListener(){
        adapterNeeder.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                when(v.id){
                    R.id.CategoryHelperLayout ->{
                        CoroutineScope(Dispatchers.Main).launch {
                            var writer = viewModel.getWriterData(adapterNeeder.currentList[position].uid)
                            if(writer!=null){
                                var bundle = Bundle()
                                bundle.putParcelable("needer",adapterNeeder.currentList[position])
                                bundle.putParcelable("writer",writer)
                                navController.navigate(R.id.action_categoryNeederMainFragment_to_neederReadFragment,bundle)
                            }
                        }
                    }
                }
            }

        })
    }
    fun getList(mainCategory : String){
        CoroutineScope(Dispatchers.Main).launch {
                 viewModel.getList(mainCategory)
        }
    }

    fun onScrollListener(rcv: RecyclerView, adapter: CategoryNeederMainAdapter){
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                var firstCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                var lastCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(lastCompletelyVisibleItemPosition == adapterNeeder.itemCount-1){

                    if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.Scrolling(adapterNeeder.currentList[0].mainCategory,
                                adapterNeeder.currentList[adapterNeeder.itemCount-1].timeStamp)
                        }
                    }

                }
            }
        })
    }
}