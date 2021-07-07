package com.moa.moakotlin.ui.concierge.needer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentNeederWriteBinding
import com.moa.moakotlin.ui.concierge.category.NeederCategoryActivity

class NeederWriteFragment : Fragment() {

    lateinit var binding : FragmentNeederWriteBinding

    lateinit var navController: NavController

    lateinit var model : NeederWriteViewModel

    companion object{
        var REQUEST_CATEGORY_SELECTION = 2000
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_needer_write,container,false)


        model = ViewModelProvider(this).get(NeederWriteViewModel::class.java)

        binding.model = model

        binding.NeederWriteCategoryLayout.setOnClickListener { selectCategory() }
        return binding.root
    }

    fun selectCategory(){
        var intent = Intent(activity,NeederCategoryActivity::class.java)

        startActivityForResult(intent, REQUEST_CATEGORY_SELECTION)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== REQUEST_CATEGORY_SELECTION && resultCode == REQUEST_CATEGORY_SELECTION){

            model.mainCategory.value = data?.getStringExtra("mainCategory")
            model.subCategory.value = data?.getStringExtra("subCategory")

            binding.NeederWriteCategory.text = "${data?.getStringExtra("mainCategory")}  /  ${data?.getStringExtra("subCategory")}"

        }
    }
}