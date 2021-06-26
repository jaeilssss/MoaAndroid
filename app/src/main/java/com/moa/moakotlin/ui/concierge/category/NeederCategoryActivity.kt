package com.moa.moakotlin.ui.concierge.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ActivityNeederCategoryBinding

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
}