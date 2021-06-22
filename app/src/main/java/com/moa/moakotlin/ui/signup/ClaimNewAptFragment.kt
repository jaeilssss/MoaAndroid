package com.moa.moakotlin.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R
import com.moa.moakotlin.customdialog.SinglePositiveButtonDialog
import com.moa.moakotlin.databinding.ClaimNewAptFragmentBinding

class ClaimNewAptFragment : Fragment() {

    companion object {
        fun newInstance() = ClaimNewAptFragment()
    }

    private lateinit var viewModel: ClaimNewAptViewModel

    private lateinit var binding: ClaimNewAptFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.claim_new_apt_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimNewAptViewModel::class.java)

        binding.claimNewAptSubmit.setOnClickListener {

            context?.let { it1 ->
                SinglePositiveButtonDialog(it1)
                        .setMessage(getString(R.string.ClaimNewAptDialog))
                        .setPositiveButton("예") {

                        }
                        .show()
            }
        }

    }
}