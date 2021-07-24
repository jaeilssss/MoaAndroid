package com.moa.moakotlin.ui.mypage

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.collection.LLRBNode
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Apt
import com.moa.moakotlin.databinding.AptModifyFragmentBinding
import com.moa.moakotlin.repository.user.UserRepository
import com.moa.moakotlin.ui.signup.AptSearchActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AptModifyFragment : BaseFragment() {


    companion object{
        var REQUEST_APT_SEARCH = 7000
    }
    private lateinit var viewModel: AptModifyViewModel


    private lateinit var binding : AptModifyFragmentBinding

    private lateinit var navController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.apt_modify_fragment , container , false)
        myActivity.bottomNavigationGone()
        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifyViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel
        binding.AptModifyAptEdit.setOnClickListener { goToSearchView()  }
        setChangeBackGround()

        viewModel.aptName.observe(viewLifecycleOwner, Observer { setChangeBackGround() })
        viewModel.dong.observe(viewLifecycleOwner, Observer { setChangeBackGround() })
        viewModel.hosoo.observe(viewLifecycleOwner, Observer { setChangeBackGround()})

        binding.AptmodifySubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(viewModel.modifyApt()){
                    Toast.makeText(context,"아파트 정보 수정이 완료되었습니다",Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.action_aptModifyFragment_to_aptModifyCertificationNoticeFragment)
                }
            }
        }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun goToSearchView(){
    var intent = Intent(context,AptSearchActivity::class.java)

        startActivityForResult(intent, REQUEST_APT_SEARCH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_APT_SEARCH && resultCode == REQUEST_APT_SEARCH){
            var apt : Apt = data?.getParcelableExtra<Apt>("apt")!!
            viewModel.aptName.value = apt.aptName
            viewModel.aptCode.value = apt.aptCode
            viewModel.address = apt.address
            binding.AptModifyAptEdit.text = apt.aptName
        }
    }

    fun setChangeBackGround(){
        if(viewModel.check()){
            binding.AptmodifySubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.AptmodifySubmit.setTextColor(Color.BLACK)
            binding.AptmodifySubmit.isClickable = true
        }else{
            binding.AptmodifySubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.AptmodifySubmit.setTextColor(Color.WHITE)
            binding.AptmodifySubmit.isClickable = false
        }
    }

}