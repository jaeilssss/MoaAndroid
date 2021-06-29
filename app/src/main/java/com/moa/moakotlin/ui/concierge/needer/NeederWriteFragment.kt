package com.moa.moakotlin.ui.concierge.needer

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentHelperWriteBinding

class NeederWriteFragment : Fragment() {

    lateinit var binding : FragmentHelperWriteBinding

    lateinit var navController: NavController

    lateinit var model : NeederWriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_helper_write,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(NeederWriteViewModel::class.java)

        binding.model = model


        return binding.root
    }

    fun selectType(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val items = resources.getStringArray(R.array.mainCategory)
        builder.setItems(R.array.mainCategory, DialogInterface.OnClickListener { dialog, pos ->
            val items = resources.getStringArray(R.array.mainCategory)
            model.mainCategory = items.get(pos)

        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setTitle("당신의 유형을 선택해주세요")
        alertDialog.show()
    }

}