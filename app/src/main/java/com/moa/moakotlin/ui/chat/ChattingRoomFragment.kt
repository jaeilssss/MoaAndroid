package com.moa.moakotlin.ui.chat

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.databinding.FragmentChattingRoomBinding
import com.moa.moakotlin.recyclerview.chat.ChattingRoomAdapter


class ChattingRoomFragment : BaseFragment() {

    var lastTimeBackPressed : Long = 0
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
        (context as MainActivity).backListener = this
        navController = findNavController()

        model = ViewModelProvider(this).get(ChattingRoomViewModel::class.java)
        myActivity.bottomNavigationVisible()
        binding.model = model

        var rcv = binding.ChattingRoomRcv

        adapter = context?.let { ChattingRoomAdapter(it,ArrayList<ChattingRoom>()) }!!

        rcv.layoutManager=  LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rcv.setHasFixedSize(true)
        rcv.setItemViewCacheSize(100)
        rcv.adapter = adapter

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        model.setSnapShot()

        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var bundle = Bundle()
                bundle.putString("roomId",adapter.roomList[position])
                bundle.putParcelable("opponentUser",adapter.userList.get(position))
                navController.navigate(R.id.action_chattingRoomFragment_to_ChatFragment,bundle)
            }
        })

        model.chattingRoomData.observe(viewLifecycleOwner, Observer {
            if(it.size!=0) {
                activity?.getSharedPreferences("MyLatestChattingRoomTimeStamp", Context.MODE_PRIVATE)!!
                        .edit {

                            putString("timeStamp", it[0].timeStamp.toString())

                            commit()
                        }
            }
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
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            activity?.finish()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(context,"종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show()
    }
}