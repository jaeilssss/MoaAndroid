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
import com.moa.moakotlin.databinding.FragmentNeederMainBinding
import com.moa.moakotlin.recyclerview.concierge.NeederMainAdapter
import com.moa.moakotlin.recyclerview.kid.KidAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederMainFragment : BaseFragment() {

        lateinit var binding : FragmentNeederMainBinding

        lateinit var navController: NavController
        lateinit var model: NeederMainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_needer_main,container, false)

        navController = findNavController()

        model = context?.let {
            NeederMainViewModel(navController,it)
        }!!

        binding.model = model

        var manager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        var manager2= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        var manager3= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        var manager4= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        var manager5= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.kidRcv.layoutManager = manager
        binding.interiorRcv.layoutManager = manager2
        binding.besidesRcv.layoutManager = manager3
        binding.laborRcv.layoutManager = manager4
        binding.petRcv.layoutManager = manager5

        CoroutineScope(Dispatchers.Main).launch {
//            var kidDataList = model.getData("육아,교육")
            var interiorDataList = model.getData("인테리어")
//            var petDataList = model.getData("반려동물")
                    // 품앗이 가 labor 인대 좀 어색하지 않나? 바꿔야할거같다는 생각..
//            var laborDataList = model.getData("품앗이")
//            var besidesDataList = model.getData("기타")
//            var kidAdapter = NeederMainAdapter(kidDataList, requireContext())
            var interiorAdapter = context?.let { NeederMainAdapter(interiorDataList, it) }
            binding.kidRcv.adapter = interiorAdapter

        }
        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}