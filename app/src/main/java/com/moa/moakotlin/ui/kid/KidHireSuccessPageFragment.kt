package com.moa.moakotlin.ui.kid

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
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.R
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentKidHireSuccessPageBinding
import com.moa.moakotlin.recyclerview.kid.KidHireSuccessAdapter
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory

class KidHireSuccessPageFragment : Fragment() {

lateinit var binding: FragmentKidHireSuccessPageBinding

    lateinit var navController: NavController

    lateinit var model : KidHireSuccessViewModel

    lateinit var list : ArrayList<ChattingRoom>

    lateinit var adapter : KidHireSuccessAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_kid_hire_success_page , container ,false)

        navController = findNavController()

        model = ViewModelProvider(this, KidViewModelFactory(navController))
            .get(KidHireSuccessViewModel::class.java)

        var kid = arguments?.getParcelable<Kid>("kid")


        if(kid?.images!=null){
            context?.let { Glide.with(it).load(kid.images!!.get(0)).into(binding.kidSuccessImage) }
        }else{
            binding.kidSuccessImage.setImageResource(R.drawable.moa_kid_default)
        }

        binding.kidSuccessTitle.text = kid?.title

        binding.model = model

        getChattingRoomList()
        return binding.root
    }


    fun getChattingRoomList(){
        list = ArrayList()

        var db = FirebaseFirestore.getInstance()

        db.collection("User").document(User.getInstance().uid)
            .collection("ChattingRoom")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for(dc in it.documents){
                    var data = dc.toObject(ChattingRoom::class.java)
                    if (data != null) {
                        list.add(data)
                    }
                }
                adapter = KidHireSuccessAdapter(navController,list)
                binding.kidHireSuccessRcv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                binding.kidHireSuccessRcv.adapter =adapter



            }
    }
}