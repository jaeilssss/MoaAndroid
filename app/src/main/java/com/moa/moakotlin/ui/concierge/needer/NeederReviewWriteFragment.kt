package com.moa.moakotlin.ui.concierge.needer

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
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.NeederReviewWriteFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederReviewWriteFragment : BaseFragment {


    private lateinit var viewModel: NeederReviewWriteViewModel

    private lateinit var binding : NeederReviewWriteFragmentBinding

    private lateinit var navController: NavController

    private lateinit var opponentUser : User

    private lateinit var needer : Needer

    constructor() : super()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.needer_review_write_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederReviewWriteViewModel::class.java)

        binding.model = viewModel
        navController = findNavController()

        arguments.let {
            opponentUser = it?.getParcelable<User>("opponentUser")!!
            needer = it.getParcelable<Needer>("Needer")!!
            binding.NeederReviewWriteMessage.text = "${opponentUser.nickName}님의 재능공유는 어떘나요?"
        }

        binding.NeederREviewWriteSubmit.setOnClickListener {
         completeNeeder()
        }

        binding.back.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun completeNeeder(){
        CoroutineScope(Dispatchers.Main).launch {
            if(viewModel.reviewWrite(opponentUser.uid)){
                needer.hireStatus = "모집완료"
                viewModel.pushToken(opponentUser)
                viewModel.hireCompletion(needer)
                Toast.makeText(context , "모집완료 되었습니다",Toast.LENGTH_SHORT).show()
                navController.popBackStack(R.id.neederReadFragment,false)
            }
        }

    }
}