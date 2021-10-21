package com.moa.moakotlin.ui.claim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ActivityClaimCategorySelectBinding
import android.view.View
class ClaimCategorySelectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityClaimCategorySelectBinding

    private  var selection  : String =""

    private var oldCategory : View ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimCategorySelectBinding.inflate(getLayoutInflater())
        binding.back.setOnClickListener { finish() }
        binding.success.setOnClickListener { completSelectCategory() }
        var view = binding.root
        setContentView(view)

        binding.ClaimMainCategoryelevatorLayout.setOnClickListener {
            checkCategory("승강기",it)
        }
        binding.ClaimMainCategoryParkLayout.setOnClickListener {
            checkCategory("주차장",it)
        }
        binding.ClaimMainCategorySecurityLayout.setOnClickListener {
            checkCategory("보안/경비",it)
        }
        binding.ClaimMainCategoryLandscapeLayout.setOnClickListener {
            checkCategory("조경시설",it)
        }
        binding.ClaimMainCategoryFacilityLayout.setOnClickListener {
            checkCategory("공동시설",it)
        }
        binding.ClaimMainCategoryEntranceLayout.setOnClickListener {
            checkCategory("공동현관/복도",it)
        }
        binding.ClaimMainCategoryFlawLayout.setOnClickListener {
            checkCategory("세대하자/민원",it)
        }
        binding.ClaimMainCategoryEtcLayout.setOnClickListener {
           checkCategory("기타",it)
        }
    }

    private fun checkCategory(categoryStr : String , layout: View){
        if(selection.length==0  || !selection.equals(categoryStr)){
            selection = categoryStr
            if(oldCategory!=layout){
                if(oldCategory==null){
                    changeCategory(null,layout)
                }else{
                    changeCategory(oldCategory!!,layout)
                }
            }
        }
    }

    private fun completSelectCategory(){
        if(selection.isNotEmpty()){
            var intent = Intent()

            intent.putExtra("category",selection)

            setResult(2000,intent)

            finish()
        }
    }
    private fun changeCategory(oldCategoryParam : View?, newCategoryParam : View){
        if(oldCategory==null){
            newCategoryParam.setBackgroundResource(R.drawable.button_shape_main_color)
            oldCategory = newCategoryParam

        }else{
            newCategoryParam.setBackgroundResource(R.drawable.button_shape_main_color)
            oldCategoryParam?.setBackgroundResource(R.drawable.shape_grey_radius_15)
            oldCategory = newCategoryParam
        }
    }


}