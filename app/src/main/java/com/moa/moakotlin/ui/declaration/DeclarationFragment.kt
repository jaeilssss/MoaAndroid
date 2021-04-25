package com.moa.moakotlin.ui.declaration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentReference
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentDeclarationBinding
import com.moa.moakotlin.viewmodelfactory.DeclarationViewFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumesAll

class DeclarationFragment : Fragment() {

    lateinit var binding : FragmentDeclarationBinding

    lateinit var model : DeclarationViewModel

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_declaration, container,false)


        navController = findNavController()


        model = ViewModelProvider(this,DeclarationViewFactory(navController)).get(DeclarationViewModel::class.java)

        model.initCheckBoxList()
        binding.model = model
        var result =false
        binding.declarationSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                  result= model.submit()
                if(result==true){
                    showToast("신고가 완료되었습니다")
                    navController.popBackStack(R.id.sitterMainFragment,false)
                }else{

                }
            }


        }

        return binding.root
    }
    fun showToast(msg : String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}