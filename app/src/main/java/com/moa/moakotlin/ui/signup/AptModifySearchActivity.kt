package com.moa.moakotlin.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Apt

class AptModifySearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.apt_modify_search_activity)

            var fragment = AptModifySearchFragment()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container,fragment)
                    .commitNow()

    }


    fun send(apt : Apt){

                var intent = Intent()

                intent.putExtra("apt",apt)

                setResult(7000,intent)

                finish()
    }
}