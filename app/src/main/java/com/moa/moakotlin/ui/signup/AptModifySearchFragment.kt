package com.moa.moakotlin.ui.signup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.AptModifySearchFragmentBinding
import com.moa.moakotlin.recyclerview.algoria.SearchAptAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AptModifySearchFragment : Fragment() {

    private lateinit var viewModel: AptModifySearchViewModel

    private lateinit var binding : AptModifySearchFragmentBinding

    private lateinit var myActivity : AptModifySearchActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater , R.layout.apt_modify_search_fragment,container,false)


        return binding.root
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myActivity = activity as AptModifySearchActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifySearchViewModel::class.java)
        binding.model  = viewModel
        var adapter = SearchAptAdapter()

        binding.searchAptActivityRcv.adapter = adapter
        binding.searchAptActivityRcv.layoutManager = LinearLayoutManager(context)
        binding.claimMyAptBtn.setOnClickListener {
            var fragment =AptModifyNewFragment()
            parentFragmentManager.beginTransaction().replace(R.id.container,fragment)
                .commitNow()
        }
        binding.aptSearchEdit.addTextChangedListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.updateSearchView()
            }
        }

        binding.back.setOnClickListener { myActivity.finish() }
        viewModel.AptList.observe(viewLifecycleOwner, Observer {
            if(it.size==0){
                binding.searchAptActivityRcv.visibility = View.GONE
                binding.aptModifyNotFoundTxt.visibility = View.VISIBLE
            }else{
                adapter.submitList(it)
                binding.searchAptActivityRcv.visibility = View.VISIBLE
                binding.aptModifyNotFoundTxt.visibility = View.GONE
            }
        })


        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
            myActivity.send(adapter.currentList[position])
            }
        })
    }


}