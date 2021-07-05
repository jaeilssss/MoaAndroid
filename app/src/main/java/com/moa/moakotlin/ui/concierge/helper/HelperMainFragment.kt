package com.moa.moakotlin.ui.concierge.helper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.FragmentNeederMainBinding
import com.moa.moakotlin.recyclerview.concierge.HelperMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelperMainFragment : BaseFragment() {

        lateinit var binding : FragmentNeederMainBinding

        lateinit var navController: NavController
        lateinit var model: HelperMainViewModel
    var kidAdapter = HelperMainAdapter()
    var interiorAdapter = HelperMainAdapter()
    var etcAdapter= HelperMainAdapter()
    var educationAdapter = HelperMainAdapter()
    var petAdapter = HelperMainAdapter()
    lateinit var myActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_needer_main,container, false)

        navController = findNavController()

        model = ViewModelProvider(this).get(HelperMainViewModel::class.java)
        myActivity.bottomNavigationVisible()


        var list = ArrayList<Helper>()
        list.add(Helper())
        list.add(Helper())
        list.add(Helper())
        list.add(Helper())
        list.add(Helper())

        binding.NeederMainKidAllBtn.setOnClickListener {

        }
        binding.NeederMainKidRcv.adapter = kidAdapter
        binding.NeederMainpetRcv.adapter = petAdapter
        binding.NeederMainEducationRcv.adapter =educationAdapter
        binding.NeederMainInteriorRcv.adapter =interiorAdapter
        binding.NeederMainEtcRcv.adapter = etcAdapter

        binding.NeederMainKidRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainpetRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEducationRcv.layoutManager  = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainInteriorRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)
        binding.NeederMainEtcRcv.layoutManager= LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)


        binding.model = model

        binding.NeederMainKidAllBtn.setOnClickListener {
//             키드 데이터를 가지고 오는 viewmodel 메소드 호출 필요
            navController.navigate(R.id.categoryMainFragment)

        }
        binding.NeederMaininteriorAllBtn.setOnClickListener {

        }
        binding.NeederMainEduAllBtn.setOnClickListener {

        }
        binding.NeederMainEtcAllBtn.setOnClickListener {

        }
        binding.NeederMainPetAllBtn.setOnClickListener {

        }
//        initGetData(kidAdapter,"육아")
//        initGetData(interiorAdapter,"인테리어")
        initGetData()
        setClickListener(kidAdapter)
        setClickListener(interiorAdapter)
        setClickListener(petAdapter)
        setClickListener(educationAdapter)
        setClickListener(etcAdapter)
        return binding.root
    }


//    fun initGetData(adapter: HelperMainAdapter,mainCategory : String){
//        CoroutineScope(Dispatchers.Main).launch {
//            var list = model.getData(mainCategory)
//            adapter.submitList(list)
//        }
//    }
    fun initGetData(){

        arguments?.let {
            var map  = it.getSerializable("HelperData")!! as HashMap<String, ArrayList<Helper>>
            kidAdapter.submitList(map.get("육아"))
            interiorAdapter.submitList(map.get("인테리어"))
            petAdapter.submitList(map.get("반려동물케어"))
            etcAdapter.submitList(map.get("기타"))
            educationAdapter.submitList(map.get("교육"))

        }

    }
    fun initGetData2(adapter: HelperMainAdapter){

        arguments?.let {
            var map  = it.getSerializable("HelperData")!! as HashMap<String, ArrayList<Helper>>
            adapter.submitList(map.get("인테리어"))

        }

    }
    override fun onBackPressed( ) {
        navController.popBackStack()
    }


    fun setClickListener(adapter: HelperMainAdapter){
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    var writer = model.getWriterData(adapter.currentList[position].uid)
                    if(writer!=null){
                        var bundle = Bundle()
                        bundle.putParcelable("data",adapter.currentList[position])
                        bundle.putParcelable("writer",writer)
                        navController.navigate(R.id.HelperReadFragment,bundle)
                    }


                }

            }

        })
    }

}