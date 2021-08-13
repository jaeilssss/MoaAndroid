package com.moa.moakotlin.ui.concierge.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.moa.moakotlin.MyApp
import com.moa.moakotlin.R

class NeederCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_needer_category)

        initFragment()
    }

    private fun initFragment(){
        var fragment = NeederMainCategoryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.NeederCategoryFrgment,fragment).commit()
    }

     fun complete(mainCategory : String , subCategory : String){
        var intent = Intent()
        println(subCategory)
        intent.putExtra("mainCategory",mainCategory)
        intent.putExtra("subCategory",subCategory)

        setResult(2000,intent)

        finish()


    }
}