package com.moa.moakotlin.ui.concierge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.ui.concierge.helper.HelperModifyFragment
import com.moa.moakotlin.ui.concierge.helper.HelperModifyViewModel
import com.moa.moakotlin.ui.concierge.helper.HelperWritePageFragment

class ConciergeWriteActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concierge_write)


            if(intent?.getParcelableExtra<Helper>("helper")!=null){
                var data = intent?.getParcelableExtra<Helper>("helper")
                if (data != null) {
                    initHelperModify(data)
                }
            }else if(intent?.getParcelableExtra<Helper>("needer")!=null){

            }else{
                initFragment(ConciergeWriteFragment())
            }

    }

    private fun initFragment( fragment : ConciergeWriteFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.ConciergeWriteFrameLayout, fragment).commit()
    }
    private fun initHelperModify(data : Helper){
        var fragment = HelperModifyFragment()
        var bundle =  Bundle()
        bundle.putParcelable("helper",data)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.ConciergeWriteFrameLayout, fragment).commit()
    }


}