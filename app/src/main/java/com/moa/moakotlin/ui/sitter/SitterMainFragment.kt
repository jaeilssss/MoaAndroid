package com.moa.moakotlin.ui.sitter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.FragmentSitterMainBinding
import com.moa.moakotlin.recyclerview.sitter.SitterMainAdapter
import com.moa.moakotlin.viewmodelfactory.SitterViewModelFactory
import com.moa.moakotlin.viewpageradapter.SitterMainViewPager

class SitterMainFragment : Fragment() {


    lateinit var binding: FragmentSitterMainBinding

    lateinit var model : SitterMainViewModel

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sitter_main,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,SitterViewModelFactory(navController))
                .get(SitterMainViewModel::class.java)

        binding.model=model

        var adapter = context?.let { SitterMainViewPager(ArrayList<Sitter>(), it,navController) }
        binding.sitterMainViewpager.adapter = adapter

        binding.sitterMainTab.setupWithViewPager(binding.sitterMainViewpager,true)

        binding.sitterRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        var db = FirebaseFirestore.getInstance()
        db.collection("Sitter").whereIn("aptCode", aptList.getInstance().aroundApt)
                .get().addOnSuccessListener {
                    var list = ArrayList<Sitter>()

                    for( document in it.documents){
                        var sitter = document.toObject(Sitter::class.java)
                        if (sitter != null){
                            sitter.documentID = document.id
                            list.add(sitter)
                        }
                    }
                    context?.let {
                        var rcvAdapter = SitterMainAdapter(list, it,navController)
                        rcvAdapter.setOnItemClickListener(object : OnItemClickListener{
                            override fun onItemClick(v: View, position: Int) {
                                when(v.id){
                                    R.id.sitter_item_layout ->{
                                        var bundle  = Bundle()
                                        bundle.putParcelable("sitter",rcvAdapter.list.get(position))
                                        navController.navigate(R.id.action_sitterMainFragment_to_sitterReadFragment,bundle)
                                    }

                                }
                            }

                        })
                        binding.sitterRecyclerview.adapter = rcvAdapter
                        return@addOnSuccessListener
                    }
                }

        return binding.root
    }

}