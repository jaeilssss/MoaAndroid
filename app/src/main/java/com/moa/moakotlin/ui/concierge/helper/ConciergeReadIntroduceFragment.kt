package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.rpc.Help
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.ConciergeReadIntroduceFragmentBinding

class ConciergeReadIntroduceFragment(var helper : Helper ? ,var needer : Needer ?) : Fragment() {


    private lateinit var viewModel: ConciergeReadIntroduceViewModel

    private lateinit var binding : ConciergeReadIntroduceFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.concierge_read_introduce_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConciergeReadIntroduceViewModel::class.java)
        binding.model = viewModel

        settingData()
    }

    private fun settingData(){
        if(helper!=null){
            viewModel.content.set(helper!!.content)
            viewModel.hopeWage.set("${helper!!.hopeWage} 원 (협의가능)")
            viewModel.aptName.set(helper!!.aptName)
            if(helper!!.isNego.not()){
                viewModel.hopeWage.set("${helper!!.hopeWage} 원")
            }
        }else if(needer!=null){
            viewModel.content.set(needer!!.content)
            viewModel.hopeWage.set("${needer!!.hopeWage} 원 (협의가능)")
            viewModel.aptName.set(needer!!.aptName)
            if(needer!!.isNego.not()){
                viewModel.hopeWage.set("${needer!!.hopeWage} 원")
            }
        }

    }

}