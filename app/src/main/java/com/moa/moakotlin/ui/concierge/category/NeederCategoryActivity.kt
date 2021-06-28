package com.moa.moakotlin.ui.concierge.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ActivityNeederCategoryBinding

class NeederCategoryActivity : AppCompatActivity() {



    var selectMainCategory = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_needer_category)

        initFragment()

        var submit = findViewById<TextView>(R.id.NeederCategorySubmit)
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
        var fragment = NeederMainCategoryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.NeederCategoryFrgment,fragment).commit()
    }
}