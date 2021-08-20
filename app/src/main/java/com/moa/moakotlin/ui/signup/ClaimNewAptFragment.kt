package com.moa.moakotlin.ui.signup

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.BaseScrollFragment
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.databinding.ClaimNewAptFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClaimNewAptFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClaimNewAptFragment()
    }

    private lateinit var viewModel: ClaimNewAptViewModel

    private lateinit var binding: ClaimNewAptFragmentBinding

    private lateinit var navController: NavController
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.claim_new_apt_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimNewAptViewModel::class.java)

        navController = findNavController()
        (context as MainActivity).backListener = this
        binding.back.setOnClickListener { navController.popBackStack() }
        binding.model = viewModel
        setChangeButton()
        binding.claimNewAptSubmit.setOnClickListener {
            context?.let { it1 ->
                SinglePositiveButtonDialog(it1)
                        .setMessage(getString(R.string.ClaimNewAptDialog))
                        .setPositiveButton("예"){
                            CoroutineScope(Dispatchers.Main).launch {
                                if(viewModel.ClaimNewApt()){
                                    navController.navigate(R.id.firstViewFragment)
                                }else{

                                }
                            }
                        }
                        .show()
            }
        }

        viewModel.aptAddress.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
        viewModel.aptName.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
        viewModel.contact.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
    }
    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun setChangeButton(){
        if(viewModel.check()){
            binding.claimNewAptSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.claimNewAptSubmit.setTextColor(Color.BLACK)
            binding.claimNewAptSubmit.isClickable =true
        }else{
            binding.claimNewAptSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.claimNewAptSubmit.setTextColor(Color.WHITE)
            binding.claimNewAptSubmit.isClickable= false
        }
    }
}