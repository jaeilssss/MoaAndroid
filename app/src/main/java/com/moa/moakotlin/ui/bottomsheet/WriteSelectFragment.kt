package com.moa.moakotlin.ui.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentWriteSelectBinding


class WriteSelectFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentWriteSelectBinding

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_write_select,container , false)

        navController  = findNavController()

        binding.kidSelectWrite.setOnClickListener {
                navController.navigate(R.id.conciergeWriteFragment)
            dismiss()
        }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        println("여기 눌림~~~dismiss")
        super.onDismiss(dialog)
    }
}