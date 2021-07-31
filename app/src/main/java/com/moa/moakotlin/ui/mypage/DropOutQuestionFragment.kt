package com.moa.moakotlin.ui.mypage

import android.content.Intent
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
import com.moa.moakotlin.LoadingActivity
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
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
            binding.DropOutCheckBOxalreadUses.isChecked = it
            viewModel.setCheckedMessage()
            if(it){
                viewModel.errorCheck.value = false
                viewModel.emptyDataCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.emptyDataCheck.observe(viewLifecycleOwner, Observer {
            binding.DropOutCheckBoxEmptyData.isChecked = it
            viewModel.setCheckedMessage()
            if(it){
                viewModel.errorCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }

        })
        viewModel.errorCheck.observe(viewLifecycleOwner, Observer {
            binding.DropOutCheckBoxError.isChecked = it
            viewModel.setCheckedMessage()
            if(it){
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.etcCheck.observe(viewLifecycleOwner, Observer {
            binding.DropOutCheckBoxEtc.isChecked  = it
            viewModel.setCheckedMessage()
            if(it){
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.modifyCheck.observe(viewLifecycleOwner, Observer {

            binding.DropOutCheckBoxIDModify.isChecked = it
            viewModel.setCheckedMessage()
            if(it){
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.noMannerCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.noMannerCheck.observe(viewLifecycleOwner, Observer {
            binding.DropOutCheckBoxNoManner.isChecked = it
            viewModel.setCheckedMessage()

            if(it){
                viewModel.emptyDataCheck.value = false
                viewModel.alreadUseCheck.value = false
                viewModel.errorCheck.value = false
                viewModel.etcCheck.value = false
                viewModel.modifyCheck.value = false
                viewModel.privateCheck.value = false
            }
        })
        viewModel.privateCheck.observe(viewLifecycleOwner, Observer {
            binding.DropOutCheckBoxPrivate. isChecked = it
            viewModel.setCheckedMessage()
            if(it){
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
            }else{
                binding.DropOutSubmit.isClickable  = false
                binding.DropOutSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            }
        })

        viewModel.delete.observe(viewLifecycleOwner, Observer {
            if(it){
                println("아니 왜 ㅡㅡ ")
                var intent = Intent(context, LoadingActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        })

        binding.DropOutSubmit.setOnClickListener {

            context?.let { it1 -> AptCertificationImageAlertDialog(it1)
                    .setMessage("회원탈퇴를 진행하시겠습니까?\n 탈퇴를 진행 할 경우 그동안 작성하신 글은 제거됩니다!")
                    .setPositiveButton("네"){
                        CoroutineScope(Dispatchers.Main).launch {
                            if(viewModel.dropOut()){

                            }
                        }
                    }
                    .setNegativeButton {

                    }.show()
            }

             }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}