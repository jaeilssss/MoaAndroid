package com.moa.moakotlin.ui.signup

import android.app.Activity
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
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseScrollFragment
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.databinding.AptModifyNewFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AptModifyNewFragment : Fragment() {



    private lateinit var viewModel: AptModifyNewViewModel

    private lateinit var binding : AptModifyNewFragmentBinding

    private lateinit var myActivity : AptModifySearchActivity

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myActivity = activity as AptModifySearchActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.apt_modify_new_fragment , container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifyNewViewModel::class.java)
        binding.model = viewModel

        binding.back.setOnClickListener { myActivity.onBackPressed() }
        setChangeButton()
        viewModel.aptAddress.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
        viewModel.aptName.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
        viewModel.contact.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
        binding.AptModifyNewSubmit.setOnClickListener {
            if(viewModel.check()){
                CoroutineScope(Dispatchers.Main).launch {
                    if(viewModel.ClaimNewApt()){
                        context?.let { it1 ->
                            SinglePositiveButtonDialog(it1)
                                .setMessage(getString(R.string.ClaimNewAptDialog))
                                .setPositiveButton("예"){
                                    myActivity.finish()

                                }.show()
                        }
                    }
                }

            }else{
                Toast.makeText(context,"필요한 정보를 모두 입력해주세요!",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setChangeButton(){
        if(viewModel.check()){
            binding.AptModifyNewSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.AptModifyNewSubmit.setTextColor(Color.BLACK)
            binding.AptModifyNewSubmit.isClickable =true
        }else{
            binding.AptModifyNewSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.AptModifyNewSubmit.setTextColor(Color.WHITE)
            binding.AptModifyNewSubmit.isClickable= false
        }
    }

}