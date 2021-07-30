
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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.MoaOperationPolicyFragmentBinding

class MoaOperationPolicy : BaseFragment() {

    companion object {
        fun newInstance() = MoaOperationPolicy()
    }

    private lateinit var viewModel: MoaOperationPolicyViewModel

    private lateinit var navController: NavController

    private lateinit var binding : MoaOperationPolicyFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater , R.layout.moa_operation_policy_fragment,container,false)
        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoaOperationPolicyViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel
        var url = ""
        var title =""
        arguments?.let {
            url = it.getString("url")!!
            title = it.getString("title")!!
        }
        binding.MoaOperationWebView.loadUrl(url)
        binding.MoaOperationWebViewTitle.text = title
        binding.MoaOperationWebView.settings.javaScriptEnabled = true

        binding.MoaOperationWebView.settings.domStorageEnabled= true
        binding.moaPolicyBack.setOnClickListener { navController.popBackStack() }



    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

}