package com.moa.moakotlin.ui.concierge.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentSitterReadBinding
import com.moa.moakotlin.viewmodelfactory.SitterViewModelFactory
import com.moa.moakotlin.viewpageradapter.SitterReadViewPager

class HelperReadFragment : Fragment() {

    lateinit var binding : FragmentSitterReadBinding

    lateinit var model : HelperReadViewModel

    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sitter_read,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,SitterViewModelFactory(navController)).get(HelperReadViewModel::class.java)
        var sitter : Sitter
     arguments.let {
        sitter = (arguments as Bundle).getParcelable("sitter")!!
    }
        binding.model = model

       model.initViewModel(sitter)
        // 이 부분 나중에 수정!!!
        binding.sitterReadStatus.text =  sitter.status

        binding.sitterReadInfo.text  = sitter.aptName

        binding.sitterReadContent.text = sitter.content

        binding.sitterReadType.text = sitter.type

        var adapter = sitter?.images?.let { context?.let { it1 -> SitterReadViewPager(it, it1,navController) } }

        binding.sitterReadViewpager.adapter = adapter
        if(sitter.images!!.size >2){
            binding.sitterReadTab.setupWithViewPager(binding.sitterReadViewpager)
        }
        if(sitter.uid.equals(User.getInstance().uid)){
            binding.sitterReadChatLayout.isVisible = false
        }else{
            binding.sitterReadWage.text = sitter.wage
        }

        binding.sitterReadChattingBtn.setOnClickListener {
//                var opponentUser  =
        }
        binding.sitterReadOption.setOnClickListener {
            var popupMenu = PopupMenu(context,it)
            if(sitter.uid.equals(User.getInstance().uid)){
                activity?.menuInflater?.inflate(R.menu.kid_read_option,popupMenu.menu)

                popupMenu.show()

                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.kid_read_modify ->{
                            navController.navigate(R.id.action_sitterReadFragment_to_sitterModifyFragment,(arguments as Bundle))
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
            }else{
                activity?.menuInflater?.inflate(R.menu.single_declaration_menu,popupMenu.menu)

                popupMenu.show()

                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.declaration->{
                            var bundle = Bundle()

                            bundle.putParcelable("sitter",sitter)
                            navController.navigate(R.id.action_sitterReadFragment_to_declarationFragment,bundle)
                            return@setOnMenuItemClickListener true
                        }else -> {
                        return@setOnMenuItemClickListener false
                    }
                    }
                }
            }
        }



        return binding.root
    }



}