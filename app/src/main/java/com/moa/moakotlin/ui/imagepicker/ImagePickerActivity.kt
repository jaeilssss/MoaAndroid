package com.moa.moakotlin.ui.imagepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moa.moakotlin.R


class ImagePickerActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)


        initFragment()


    }

    fun initFragment(){
        var fragment = ImagePickerViewFragment()
        supportFragmentManager.beginTransaction().replace(R.id.ActivityImagePickerFragment,fragment).commit()
    }
}