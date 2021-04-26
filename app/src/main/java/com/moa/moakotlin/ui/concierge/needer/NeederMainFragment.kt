package com.moa.moakotlin.ui.concierge.needer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.FragmentKidMainBinding
import com.moa.moakotlin.recyclerview.kid.KidAdapter

class NeederMainFragment : BaseFragment() {

        lateinit var binding : FragmentKidMainBinding

        lateinit var navController: NavController
        lateinit var model: NeederMainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_kid_main,container, false)

        navController = findNavController()

        model = context?.let {
            NeederMainViewModel(navController,it)
        }!!

        var rcv = binding.kidRecyclerview
        var manager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rcv.layoutManager = manager

        var db = FirebaseFirestore.getInstance()
        db.collection("Kid").whereIn("aptCode",aptList.getInstance().aroundApt)
                .get().addOnSuccessListener {
                    var list = ArrayList<Kid>()
                    for( document in it.documents){
                        var kid = document.toObject(Kid::class.java)
                        if (kid != null){
                           kid.documentID = document.id
                            list.add(kid)
                        }
                    }
                    context?.let {
                        var adapter = KidAdapter(list, it,navController)
                        rcv.adapter = adapter
                    }
                }
        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack(R.id.HomeFragment,false)
    }


}