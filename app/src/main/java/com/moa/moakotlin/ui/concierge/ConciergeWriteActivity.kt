package com.moa.moakotlin.ui.concierge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.moa.moakotlin.R

class ConciergeWriteActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concierge_write)

            initFragment()

    }

    private fun initFragment() {
        var fragment = ConciergeWriteFragment()
        supportFragmentManager.beginTransaction().replace(R.id.ConciergeWriteFrameLayout, fragment).commit()
    }


}