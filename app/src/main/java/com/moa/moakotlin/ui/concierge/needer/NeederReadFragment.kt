package com.moa.moakotlin.ui.concierge.needer

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
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentNeederReadBinding
import com.moa.moakotlin.viewpageradapter.KidReadViewPagerAdapter

class NeederReadFragment : Fragment() {

    lateinit var binding : FragmentNeederReadBinding

    lateinit var navController: NavController

    lateinit var model: NeederReadViewModel

    lateinit var kid : Kid
    lateinit var  bundle : Bundle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        arguments?.let {
            bundle = arguments as Bundle
             kid = bundle.getParcelable("kid")!!
        }

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_needer_read , container , false)

        if(kid.hireStatus.equals("채용완료")){
            binding.kidReadHireState.text = "채용완료"
            binding.kidReadHireState.isEnabled = false
        }
        if(kid.uid.equals(User.getInstance().uid)){
                binding.kidReadChatLayout.
                isVisible = false
        }
        navController = findNavController()

        var adapter : KidReadViewPagerAdapter
        if(kid.images==null){
           adapter = activity?.applicationContext?.let { KidReadViewPagerAdapter(null, it) }!!
        }else{
            adapter = kid.images?.let { KidReadViewPagerAdapter(it, activity?.applicationContext!!) }!!
        }

        binding.viewPager.adapter = adapter

        if(kid.images !=null && kid.images!!.size>1){
            binding.tabLayout.setupWithViewPager(binding.viewPager,true)
        }
        binding.mainCategory.text = kid.lifeCycle

        binding.kidReadContent.text = kid.content

        binding.kidHopeDate.text = kid.hopeDate

        model = ViewModelProvider(this).get(NeederReadViewModel::class.java)

        binding.model = model

        binding.kidReadHireState.setOnClickListener {

            if(kid.uid.equals(User.getInstance().uid)){

                var popupMenu = PopupMenu(context,it)

                activity?.menuInflater?.inflate(R.menu.kid_hire_status_menu,popupMenu.menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.kid_hire_status_hired ->{
                            setHired()
                            var bundle = Bundle()
                            bundle.putParcelable("kid",kid)
                            return@setOnMenuItemClickListener true
                        }
                        else ->  return@setOnMenuItemClickListener false
                    }
                }
            }
        }
    binding.option.setOnClickListener {

        var popupMenu = PopupMenu(context,it)
        if(kid.uid.equals(User.getInstance().uid)){
            activity?.menuInflater?.inflate(R.menu.kid_read_option,popupMenu.menu)

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.kid_read_modify ->{
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
                        return@setOnMenuItemClickListener true
                    }else -> {
                    return@setOnMenuItemClickListener false
                }
                }
            }
        }
        }

        binding.kidReadChattingBtn.setOnClickListener {
            kid.uid?.let {
                    it1 -> model.goToChatting(it1)
            }
        }
        return binding.root
    }

    fun setHired(){
        var db = FirebaseFirestore.getInstance()
        db.collection("Kid")
            .document(kid.documentID)
            .update(
                "hireStatus","채용완료"
            )
    }
}