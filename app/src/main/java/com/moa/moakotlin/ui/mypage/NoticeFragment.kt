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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.WebViewActivity
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.BottomNavController
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.NoticeFragmentBinding
import com.moa.moakotlin.recyclerview.mypage.NoticeAdapter
import com.moa.moakotlin.recyclerview.mypage.QuestionAdapter

class NoticeFragment : BaseFragment() {

    companion object {
        fun newInstance() = NoticeFragment()
    }

    private lateinit var viewModel: NoticeViewModel

    private lateinit var binding : NoticeFragmentBinding

    private lateinit var adapter : NoticeAdapter

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.notice_fragment, container , false)
        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel
        adapter= NoticeAdapter()
        binding.noticeRcv.adapter = adapter
        binding.noticeRcv.layoutManager = LinearLayoutManager(context)

        viewModel.getNoticeList()

        viewModel.noticeList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.noticeBack.setOnClickListener { navController.popBackStack() }
        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(context, WebViewActivity::class.java)

                intent.putExtra("url",adapter.currentList[position].url)

                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }
}