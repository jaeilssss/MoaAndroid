package com.moa.moakotlin.ui.mypage

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.WebViewActivity
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.QuestionFragmentBinding
import com.moa.moakotlin.recyclerview.mypage.QuestionAdapter

class QuestionFragment : BaseFragment() {

    companion object {
        fun newInstance() = QuestionFragment()
    }

    private lateinit var viewModel: QuestionViewModel

    private lateinit var binding: QuestionFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapter : QuestionAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.question_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = QuestionAdapter()

        binding.questionRcv.adapter = adapter
        binding.questionRcv.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)

        navController = findNavController()


        viewModel.questionList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.questionBack.setOnClickListener{onBackPressed()}
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(context,WebViewActivity::class.java)

                intent.putExtra("url",adapter.currentList[position].url)

                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

}