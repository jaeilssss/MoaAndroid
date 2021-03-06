package com.moa.moakotlin.ui.signup

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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.MyNeighborhoodFragmentBinding
import com.moa.moakotlin.recyclerview.apt.MyNeighborhoodListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyNeighborhoodFragment : BaseFragment() {

    private lateinit var viewModel: MyNeighborhoodViewModel

    private lateinit var binding : MyNeighborhoodFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.my_neighborhood_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNeighborhoodViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel
        binding.back.setOnClickListener { navController.popBackStack() }
        binding.myAptText.text = User.getInstance().aptName
        (context as MainActivity).backListener = this
        arguments?.let {
            var neighborhood = it.getStringArrayList("neighborhood")
            if (neighborhood != null) {
                neighborhood.remove(User.getInstance().aptName)
                settingMyNeighborhood(neighborhood)
            }
        }

        binding.neighborhoodNextBtn.setOnClickListener {
            goToAptCertificationNotice()
        }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun goToAptCertificationNotice(){
        navController.navigate(R.id.certificationNoticeFragment)
    }
    private fun settingMyNeighborhood( neighborhood : ArrayList<String>){
            var adapter = MyNeighborhoodListAdapter()

            adapter.submitList(neighborhood)

            binding.myNeighborhoodRcv.adapter = adapter

            binding.myNeighborhoodRcv.layoutManager = LinearLayoutManager(context)

    }

}