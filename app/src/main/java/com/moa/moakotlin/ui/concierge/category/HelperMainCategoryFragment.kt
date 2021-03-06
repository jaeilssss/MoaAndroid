package com.moa.moakotlin.ui.concierge.category

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.HelperMainCategoryFragmentBinding


class HelperMainCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = HelperMainCategoryFragment()
    }

    private lateinit var viewModel: HelperMainCategoryViewModel

    private lateinit var binding : HelperMainCategoryFragmentBinding

        var selectMainCategory  = ""

     var myActivity : HelperCategoryActivity ?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        myActivity =  activity as HelperCategoryActivity

    }

    override fun onDetach() {
        super.onDetach()
        myActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.helper_main_category_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelperMainCategoryViewModel::class.java)

        binding.model = viewModel
        binding.HelperMainCategoryKidLayout.setOnClickListener {
            if (selectMainCategory == "육아") {
                setBackgroundKid(R.drawable.shape_grey_radius_10)
                selectMainCategory = ""
                myActivity?.setMainCategory("")
            } else {
                setBackgroundKid(R.drawable.drawable_view_focused_main_color)
                setBackgroundEdu(R.drawable.shape_grey_radius_10)
                setBackgroundPet(R.drawable.shape_grey_radius_10)
                setBackgroundInterior(R.drawable.shape_grey_radius_10)
                setBackgroundEtc(R.drawable.shape_grey_radius_10)
                selectMainCategory = "육아"
                myActivity?.setMainCategory("육아")
            }
        }
        binding.HelperMainCategoryEduLayout.setOnClickListener {

            if (selectMainCategory == "교육") {
                setBackgroundEdu(R.drawable.shape_grey_radius_10)
                selectMainCategory = ""
                myActivity?.setMainCategory("")
            } else {
                setBackgroundPet(R.drawable.shape_grey_radius_10)
                setBackgroundInterior(R.drawable.shape_grey_radius_10)
                setBackgroundEtc(R.drawable.shape_grey_radius_10)
                setBackgroundKid(R.drawable.shape_grey_radius_10)
                setBackgroundEdu(R.drawable.drawable_view_focused_main_color)
                selectMainCategory = "교육"
                myActivity?.setMainCategory("교육")
            }
        }

        binding.HelperMainCategoryPetLayout.setOnClickListener {
            if(selectMainCategory =="반려동물케어"){
                setBackgroundPet(R.drawable.shape_grey_radius_10)
                selectMainCategory=""
                myActivity?.setMainCategory("")
            }else{
                setBackgroundInterior(R.drawable.shape_grey_radius_10)
                setBackgroundEtc(R.drawable.shape_grey_radius_10)
                setBackgroundKid(R.drawable.shape_grey_radius_10)
                setBackgroundEdu(R.drawable.shape_grey_radius_10)
                setBackgroundPet(R.drawable.drawable_view_focused_main_color)
                selectMainCategory = "반려동물케어"
                myActivity?.setMainCategory("반려동물케어")
            }
        }
        binding.HelperMainCategoryInteriorLayout.setOnClickListener {
            if(selectMainCategory=="인테리어"){
                setBackgroundInterior(R.drawable.shape_grey_radius_10)
                selectMainCategory =""
                myActivity?.setMainCategory("")
            }else{
                setBackgroundEtc(R.drawable.shape_grey_radius_10)
                setBackgroundKid(R.drawable.shape_grey_radius_10)
                setBackgroundEdu(R.drawable.shape_grey_radius_10)
                setBackgroundPet(R.drawable.shape_grey_radius_10)
                setBackgroundInterior(R.drawable.drawable_view_focused_main_color)
                selectMainCategory = "인테리어"
                        myActivity?.setMainCategory("인테리어")
            }
        }

        binding.HelperMainCategoryEtcLayout.setOnClickListener {
            if(selectMainCategory=="기타"){
                setBackgroundEtc(R.drawable.shape_grey_radius_10)
                selectMainCategory= ""
                myActivity?.setMainCategory("")
            }else{
                setBackgroundKid(R.drawable.shape_grey_radius_10)
                setBackgroundEdu(R.drawable.shape_grey_radius_10)
                setBackgroundPet(R.drawable.shape_grey_radius_10)
                setBackgroundInterior(R.drawable.shape_grey_radius_10)
                setBackgroundEtc(R.drawable.drawable_view_focused_main_color)
                selectMainCategory="기타"
                myActivity?.setMainCategory("기타")
            }
        }
    }

    private fun initBackground(selected : String){
        if(selected=="육아"){
            selectMainCategory="육아"
            setBackgroundKid(R.drawable.drawable_view_focused_main_color)
        }else if(selected=="교육"){
            selectMainCategory="교육"
            setBackgroundEdu(R.drawable.drawable_view_focused_main_color)
        }else if(selected=="반려동물케어"){
            setBackgroundPet(R.drawable.drawable_view_focused_main_color)
            selectMainCategory ="반려동물케어"
        }else if(selected=="인테리어"){
            selectMainCategory="인테리어"
            setBackgroundInterior(R.drawable.drawable_view_focused_main_color)
        }else if(selected=="기타"){
            setBackgroundEtc(R.drawable.drawable_view_focused_main_color)
            selectMainCategory="기타"
        }

    }
    private fun setBackgroundKid(resInt : Int){
        binding.HelperMainCategoryKidLayout.setBackgroundResource(resInt)
    }
    private fun setBackgroundEdu(resInt : Int){
        binding.HelperMainCategoryEduLayout.setBackgroundResource(resInt)
    }
    private fun setBackgroundPet(resInt : Int){
        binding.HelperMainCategoryPetLayout.setBackgroundResource(resInt)
    }
    private fun setBackgroundInterior(resInt: Int){
        binding.HelperMainCategoryInteriorLayout.setBackgroundResource(resInt)
    }
    private fun setBackgroundEtc(resInt: Int){
        binding.HelperMainCategoryEtcLayout.setBackgroundResource(resInt)
    }

}