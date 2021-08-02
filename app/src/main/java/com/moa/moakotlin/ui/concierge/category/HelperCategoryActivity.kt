package com.moa.moakotlin.ui.concierge.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.moa.moakotlin.R

class HelperCategoryActivity : AppCompatActivity() {



    var selectMainCategory = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helper_category)
        var back : ConstraintLayout = findViewById(R.id.back)
        back.setOnClickListener { finish() }
        initFragment()

        var submit = findViewById<TextView>(R.id.HelperCategorySubmit)
        submit.setOnClickListener {
                if(selectMainCategory.equals("").not()){
                    var intent = Intent()

                    intent.putExtra("selectedMainCategory",selectMainCategory)

                    setResult(2000,intent)

                    finish()
                }
        }
    }

    fun setMainCategory(mainCategory : String){
        selectMainCategory = mainCategory
    }
    private fun initFragment(){
        var fragment = HelperMainCategoryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.HelperCategoryFrgment,fragment).commit()
    }
}