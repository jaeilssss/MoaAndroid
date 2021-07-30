package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.MoaOperationPolicyFragmentBinding

class MoaOperationPolicy : Fragment() {

    companion object {
        fun newInstance() = MoaOperationPolicy()
    }

    private lateinit var viewModel: MoaOperationPolicyViewModel

    private lateinit var navController: NavController

    private lateinit var binding : MoaOperationPolicyFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater , R.layout.moa_operation_policy_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoaOperationPolicyViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel

        binding.MoaOperationWebView.loadUrl("https://moduapt.com/terms/use")
        binding.MoaOperationWebView.settings.javaScriptEnabled = true

        binding.MoaOperationWebView.settings.domStorageEnabled= true



    }

}