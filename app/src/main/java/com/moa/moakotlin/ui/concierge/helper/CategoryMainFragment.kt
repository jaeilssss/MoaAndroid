package com.moa.moakotlin.ui.concierge.helper

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.CategoryMainFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.CategoryHelperMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryMainFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryMainFragment()
    }

    private lateinit var viewModel: CategoryMainViewModel

    private lateinit var binding : CategoryMainFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapterHelper : CategoryHelperMainAdapter

    lateinit var myActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.category_main_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myActivity.bottomNavigationGone()
        viewModel = ViewModelProvider(this).get(CategoryMainViewModel::class.java)

        binding.CategoryMainRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!)

        adapterHelper = CategoryHelperMainAdapter()
        binding.back.setOnClickListener {
            navController.popBackStack()
        }
        navController = findNavController()
        binding.CategoryMainRcv.adapter = adapterHelper
        arguments.let {
            var mainCategory = it?.getString("mainCategory")
            if (mainCategory != null) {
                getList(mainCategory)
                binding.CategoryMainText.text = mainCategory
            }
        }

        setAdapterClickListener()
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
            var list = viewModel.getList(mainCategory)
            adapterHelper.submitList(list)
        }
    }

}