package com.moa.moakotlin.ui.claim

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ClaimReadFragmentBinding

class ClaimReadFragment : Fragment() {

    companion object {
        fun newInstance() = ClaimReadFragment()
    }

    private lateinit var viewModel: ClaimReadViewModel

    private lateinit var binding : ClaimReadFragmentBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.claim_read_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClaimReadViewModel::class.java)
        // TODO: Use the ViewModel
    }

}