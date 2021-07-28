package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.DropOutQuestionFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DropOutQuestionFragment : BaseFragment() {

    companion object {
        fun newInstance() = DropOutQuestionFragment()
    }

    private lateinit var binding : DropOutQuestionFragmentBinding

    private lateinit var viewModel: DropOutQuestionViewModel

    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.drop_out_question_fragment,container, false)
        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DropOutQuestionViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()

        viewModel.alreadUseCheck.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.checked.value = "유사한 다른 타 어플을 사용 중이라서"
                viewModel.errorCheck.value = false
                viewModel.emptyDataCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }


        })
        viewModel.emptyDataCheck.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.checked.value = "나에게 필요한 정보가 부족해서"
                viewModel.errorCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }

        })
        viewModel.errorCheck.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.checked.value = "시스템 오류가 많아서"
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.etcCheck.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.checked.value = "기타"
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.modifyCheck.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.checked.value="아이디 변경 또는 재가입을 위해서"
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.noMannerCheck.observe(viewLifecycleOwner, Observer {

            if(it){
                viewModel.checked.value = "비매너 이웃을 만나서"
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.privateCheck.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.checked.value="개인정보 유출 방지 등 보안 상의 문제로"
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
            }
        })
        viewModel.checked.observe(viewLifecycleOwner, Observer {
            if(it.length>0){
                binding.DropOutSubmit.isClickable  = true
                binding.DropOutSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            }
        })

        binding.DropOutSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(viewModel.dropOut()){
                    navController.navigate(R.id.FirstFragment)
                }
            }
             }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}