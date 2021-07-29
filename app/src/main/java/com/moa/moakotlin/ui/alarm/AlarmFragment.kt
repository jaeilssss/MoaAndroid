package com.moa.moakotlin.ui.alarm


import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Notification
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.AlarmFragmentBinding
import com.moa.moakotlin.recyclerview.alarm.AlarmAdapter

class AlarmFragment : Fragment() {


    private lateinit var viewModel: AlarmViewModel

    private lateinit var binding : AlarmFragmentBinding

    private lateinit var navController: NavController

    var notificationList = ArrayList<Notification>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.alarm_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("알람 프레그먼트")
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
//        val data = activity?.getSharedPreferences("AlarmSetting",Context.MODE_PRIVATE)
//
//        if(data?.getBoolean("isEventAlarm",false) == true){
//
//        }
        binding.model = viewModel

        navController = findNavController()

        var adapter = AlarmAdapter()

        binding.AlarmFragmentRcv.adapter = adapter
        binding.AlarmFragmentRcv.layoutManager = LinearLayoutManager(context)
//


        viewModel.SnapShot()

        viewModel.notificationList.observe(viewLifecycleOwner, Observer {
            activity?.getSharedPreferences("MyLatestNotification", Context.MODE_PRIVATE)!!
                    .edit {
                        putString("documentID",it[0].documentID)
                        commit()
                    }
                    adapter.submitList(it)
        })
    }

    fun getMyNotification(){

    }

    override fun onStop() {

        viewModel.removeSnapShot()

        super.onStop()
    }
}