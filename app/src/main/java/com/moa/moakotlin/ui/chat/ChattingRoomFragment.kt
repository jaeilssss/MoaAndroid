package com.moa.moakotlin.ui.chat

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.databinding.FragmentChattingRoomBinding
import com.moa.moakotlin.recyclerview.chat.ChattingRoomAdapter


class ChattingRoomFragment : BaseFragment() {


    lateinit var binding : FragmentChattingRoomBinding
    var TAG = "ChattingRoom"
    lateinit var navController: NavController

    lateinit var db : ListenerRegistration
    lateinit var model: ChattingRoomViewModel
   lateinit var adapter :ChattingRoomAdapter
     var firebase  = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chatting_room,container , false)

        navController = findNavController()

        model = ViewModelProvider(this).get(ChattingRoomViewModel::class.java)

        binding.model = model

//        var rcv = binding.chattingRoomRecyclerview

        adapter = context?.let { ChattingRoomAdapter(it,ArrayList<ChattingRoom>()) }!!

//        rcv.layoutManager=  LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//
//        rcv.adapter = adapter

        model.setSnapShot()

        model.chattingRoomData.observe(viewLifecycleOwner, Observer {
            adapter.list = model.chattingRoomData.value!!
            adapter.roomList = model.roomList
            adapter.resetting()
        })
        return binding.root
    }
    override fun onStop() {
        super.onStop()
        adapter.list.clear()
        model.deleteSnapShot()
    }

    override fun onBackPressed() {
        navController.popBackStack(R.id.HomeFragment,true)
    }
}