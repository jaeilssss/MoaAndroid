package com.moa.moakotlin.ui.concierge.helper

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
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.CategoryMainFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.CategoryMainAdapter

class CategoryMainFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryMainFragment()
    }

    private lateinit var viewModel: CategoryMainViewModel

    private lateinit var binding : CategoryMainFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapter : CategoryMainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.category_main_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryMainViewModel::class.java)

        binding.CategoryMainRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!)

        adapter = CategoryMainAdapter()

        navController = findNavController()

        var list = ArrayList<Needer>()

        // 나중에 지울 것
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())
        list.add(Needer())

        binding.CategoryMainRcv.adapter = adapter
        adapter.submitList(list)




    }

}