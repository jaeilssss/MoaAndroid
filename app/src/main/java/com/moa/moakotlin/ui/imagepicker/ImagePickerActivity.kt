package com.moa.moakotlin.ui.imagepicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ActivityImagePickerBinding


class ImagePickerActivity : AppCompatActivity() {


    var selectedPictures = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        var back = findViewById<ConstraintLayout>(R.id.imagePickerBack)
        back.setOnClickListener {
            finish()
        }
        var intent = intent
       selectedPictures = intent.getStringArrayListExtra("selectedPictureList")!!
        var check : String ?=null
        check = intent.getStringExtra("singleImage")
        var submit = findViewById<TextView>(R.id.ActivityImagePickerSubmit)

        submit.setOnClickListener {
            var intent = Intent()
            intent.putExtra("selectedPictures",selectedPictures)
            setResult(1000,intent)
            finish()
        }
        if(check!=null){

            initSingleImageFragment()
        }else{
            initFragment()
        }



    }



    private fun initFragment(){

        var fragment = ImagePickerSecondFragment(selectedPictures)
        supportFragmentManager.beginTransaction().replace(R.id.ActivityImagePickerFragment,fragment).commit()
    }

    private fun initSingleImageFragment(){
        var fragment = SingleImagePickerFragment(selectedPictures)
        supportFragmentManager.beginTransaction().replace(R.id.ActivityImagePickerFragment,fragment).commit()
    }
}