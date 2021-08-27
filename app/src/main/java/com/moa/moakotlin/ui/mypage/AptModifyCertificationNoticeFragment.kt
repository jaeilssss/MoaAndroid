package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.AptModifyCertificationNoticeFragmentBinding

class  AptModifyCertificationNoticeFragment : BaseFragment() {

    companion object {
        fun newInstance() = AptModifyCertificationNoticeFragment()
    }

    private lateinit var viewModel: AptModifyCertificationNoticeViewModel

    private lateinit var binding : AptModifyCertificationNoticeFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.apt_modify_certification_notice_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifyCertificationNoticeViewModel::class.java)
        (context as MainActivity).backListener = this
        navController = findNavController()
        myActivity.bottomNavigationGone()

        binding.CertificationBtn.setOnClickListener { navController.navigate(R.id.action_aptModifyCertificationNoticeFragment_to_aptModifyCertificationFragment) }
        binding.skipCertificationBtn.setOnClickListener {
            Toast.makeText(context,"아파트 인증을 미루었습니다 ",Toast.LENGTH_SHORT).show()
            navController.popBackStack(R.id.MyPageFragment,false)
        }
    }

    override fun onBackPressed() {
        navController.popBackStack(R.id.MyPageFragment,false)
    }

}