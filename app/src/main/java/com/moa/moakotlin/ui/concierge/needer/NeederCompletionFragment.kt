package com.moa.moakotlin.ui.concierge.needer

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
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.NeederCompletionFragmentBinding
import com.moa.moakotlin.recyclerview.chat.ChattingRoomAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederCompletionFragment : Fragment() {



    private lateinit var viewModel: NeederCompletionViewModel

    private lateinit var binding : NeederCompletionFragmentBinding

    private lateinit var navController: NavController

    private lateinit var needer : Needer

    private lateinit var adapter : ChattingRoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater , R.layout.needer_completion_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederCompletionViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()

        binding.back.setOnClickListener { navController.popBackStack() }



        arguments?.let {
            needer  = it.getParcelable<Needer>("Needer")!!
            setData()
        }

        getChatData()
    }

   private fun setData(){
        if(needer.images!!.size>0){
            Glide.with(activity?.applicationContext!!).load(needer.images!!.get(0)).into(binding.NeederCompletionContentImage)
        }
       binding.NeederCompletionContentTitle.text = needer.title
    }


    private fun getChatData(){
        CoroutineScope(Dispatchers.Main).launch {
            var list = viewModel.getChattingRoomList()
            list.add(ChattingRoom())
            list.add(ChattingRoom())
            list.add(ChattingRoom())
            list.add(ChattingRoom())
            list.add(ChattingRoom())
            list.add(ChattingRoom())
            adapter = ChattingRoomAdapter(activity?.applicationContext!!,list)

            binding.NeederCompletionRcv.adapter = adapter

            binding.NeederCompletionRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!)


        }
    }

}