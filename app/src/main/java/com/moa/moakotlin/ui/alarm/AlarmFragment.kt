package com.moa.moakotlin.ui.alarm


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
import com.moa.moakotlin.data.Notification
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

        binding.model = viewModel

        navController = findNavController()

        arguments?.let {
            notificationList = it.getParcelableArrayList<Notification>("notificationList")!!
            println(notificationList.size)
        }

        var adapter = AlarmAdapter()

        binding.AlarmFragmentRcv.adapter = adapter
        binding.AlarmFragmentRcv.layoutManager = LinearLayoutManager(context)

        adapter.submitList(notificationList)


    }

}