package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
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
import com.moa.moakotlin.databinding.NeederMainFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.HelperMainAdapter
import com.moa.moakotlin.recyclerview.concierge.NeederMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederMainFragment : Fragment() {

    private lateinit var viewModel: NeederMainViewModel

    private lateinit var binding : NeederMainFragmentBinding

    private lateinit var navController: NavController

    lateinit var myActivity : MainActivity

    var kidAdapter = NeederMainAdapter()
    var interiorAdapter = NeederMainAdapter()
    var etcAdapter= NeederMainAdapter()
    var educationAdapter = NeederMainAdapter()
    var petAdapter = NeederMainAdapter()
    var sharingAdapter = NeederMainAdapter()
    var borrowAdapter = NeederMainAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.needer_main_fragment,container,false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederMainViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        myActivity.bottomNavigationVisible()

        binding.NeederMainKidRcv.adapter = kidAdapter
        binding.NeederMainpetRcv.adapter = petAdapter
        binding.NeederMainEducationRcv.adapter =educationAdapter
        binding.NeederMainInteriorRcv.adapter =interiorAdapter
        binding.NeederMainEtcRcv.adapter = etcAdapter

        binding.NeederMainKidRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainpetRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEducationRcv.layoutManager  = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainInteriorRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEtcRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainBorrowRcv.layoutManager =  LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainSharingRcv.layoutManager =  LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)


        initGetData(kidAdapter,"육아")
        initGetData(interiorAdapter,"인테리어")
        initGetData(petAdapter,"반려동물케어")
        initGetData(educationAdapter,"교육")
        initGetData(etcAdapter,"기타")
        initGetData(borrowAdapter,"빌려주세요")
        initGetData(sharingAdapter,"품앗이")
    }
    // 이 부분 수정해야함.. 어댑터를 하나 더 만들어야 함
    fun initGetData(adapter: NeederMainAdapter,mainCategory : String){
        CoroutineScope(Dispatchers.Main).launch {
            var list = viewModel.getData(mainCategory)
            adapter.submitList(list)
        }
    }
}