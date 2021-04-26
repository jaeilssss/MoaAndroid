package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.databinding.FragmentKidReviewWriteBinding
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory

class NeederReviewWriteFragment : Fragment() {


    lateinit var binding : FragmentKidReviewWriteBinding

    lateinit var navController: NavController

    lateinit var model : NeederReviewWriteViewModel
    lateinit var transfer: Transfer

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null  ){
            transfer = activity as Transfer
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var bundle = arguments as Bundle

        var opponent = bundle.getString("opponentUid")
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_kid_review_write,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,KidViewModelFactory(navController))
            .get(NeederReviewWriteViewModel::class.java)

        binding.model = model
        model.transfer = transfer

        binding.kidReviewSubmit.setOnClickListener {

            if(model.content.get()?.length==0){
                Toast.makeText(context,"리뷰를 작성해주세요!",Toast.LENGTH_SHORT).show()
            }else{
                if (opponent != null) {
                    model.submit(opponent)
                }
            }

        }
        return binding.root
    }

}