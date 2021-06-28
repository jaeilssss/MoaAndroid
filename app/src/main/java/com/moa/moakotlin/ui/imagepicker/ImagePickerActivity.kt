package com.moa.moakotlin.ui.imagepicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.moa.moakotlin.R


class ImagePickerActivity : AppCompatActivity() {


    var selectedPictures = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)


        initFragment()


        var submit = findViewById<TextView>(R.id.ActivityImagePickerSubmit)

        submit.setOnClickListener {
            var intent = Intent()
            intent.putExtra("selectedPictures",selectedPictures)
            Toast.makeText(applicationContext,selectedPictures.size.toString(),Toast.LENGTH_SHORT).show()
            setResult(1000,intent)
            finish()
        }

    }



    fun initFragment(){
        var fragment = ImagePickerViewFragment()
        supportFragmentManager.beginTransaction().replace(R.id.ActivityImagePickerFragment,fragment).commit()
    }
}