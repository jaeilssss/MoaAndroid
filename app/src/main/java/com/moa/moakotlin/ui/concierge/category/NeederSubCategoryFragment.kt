package com.moa.moakotlin.ui.concierge.category

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.NeederSubCategoryFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.FlexBoxAdapter

class NeederSubCategoryFragment(var mainCategory : String) : Fragment() {

    private lateinit var viewModel: NeederSubCategoryViewModel

    private lateinit var binding : NeederSubCategoryFragmentBinding

    private lateinit var myActivity : NeederCategoryActivity

    var kidSubCategory = arrayListOf<String>("등하원 도우미","실내놀이","야외활동","아이돌봄","기타육아")
    var interiorCategory = arrayListOf<String>("못박기","가구 재배치","DIY 가구조립","조명 교체/설치","문고리 교체/설치","도어락 교체/설치","기타 인테리어")
    var sharingCategory = arrayListOf<String>("장 함께보고 나누기","김장 같이 하기","공동 육아","기타 품앗이")
    var etcCategory = arrayListOf<String>("가구/가전 버리기","용달","기타")
    var eduCategory = arrayListOf<String>("학습/과외","미술","음악","체육","기타 교육")
    var petCategory = arrayListOf<String>("배식","산책","실내놀이","맡기기","반려동물 기타")
    var borrowCategory = arrayListOf<String>("육아용품","공구/가전","식료품","도서/사무용품","기타")

    override fun onAttach(context: Context) {
        super.onAttach(context)

        myActivity = activity as NeederCategoryActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.needer_sub_category_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(NeederSubCategoryViewModel::class.java)

        binding.model = viewModel


        val flexBoxAdapter = FlexBoxAdapter()

        FlexboxLayoutManager(activity?.applicationContext)
            .apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.CENTER
            }.let {
                binding.NeederSubCategoryRcv.layoutManager = it
                binding.NeederSubCategoryRcv.adapter = flexBoxAdapter
                if(mainCategory.equals("육아")){flexBoxAdapter.submitList(kidSubCategory)}
                else if(mainCategory.equals("교육")){flexBoxAdapter.submitList(eduCategory)}
                else if(mainCategory.equals("인테리어")){flexBoxAdapter.submitList(interiorCategory)}
                else if(mainCategory.equals("품앗이")){flexBoxAdapter.submitList(sharingCategory)}
                else if(mainCategory.equals("기타")){flexBoxAdapter.submitList(etcCategory)}
                else if(mainCategory.equals("반려동물케어")){flexBoxAdapter.submitList(petCategory)}
                else if(mainCategory.equals("빌려주세요")){flexBoxAdapter.submitList(borrowCategory)}
            }
        flexBoxAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {

                myActivity.complete(mainCategory,flexBoxAdapter.currentList[position])
            }

        })

    }

}