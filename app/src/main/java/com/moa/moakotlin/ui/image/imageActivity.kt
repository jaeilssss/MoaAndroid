package com.moa.moakotlin.ui.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import uk.co.senab.photoview.PhotoViewAttacher

class imageActivity : AppCompatActivity() {
    enum class TOUCH_MODE {
        NONE,   // 터치 안했을 때
        SINGLE, // 한손가락 터치
        MULTI   //두손가락 터치
    }

    lateinit var mAttacher : PhotoViewAttacher
    var url : String = ""
    private lateinit var image : ImageView
    private lateinit var back : ConstraintLayout


    private lateinit var mScaleGestureDetector : ScaleGestureDetector

    var mScaleFactor  = 1.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        url = intent.getStringExtra("url")!!
        image = findViewById(R.id.image)
        back = findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }
        Glide.with(this).load(url).into(image)
        mAttacher = PhotoViewAttacher(image)
//        mScaleGestureDetector = ScaleGestureDetector(this,ScaleListener())
    }


    override fun onTouchEvent(motionEvent: MotionEvent) : Boolean{

//
//
//        mScaleGestureDetector.onTouchEvent(motionEvent)

        return true
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            // ScaleGestureDetector에서 factor를 받아 변수로 선언한 factor에 넣고
            mScaleFactor *= scaleGestureDetector.scaleFactor

            // 최대 10배, 최소 10배 줌 한계 설정
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f))

            // 이미지뷰 스케일에 적용
            image.setScaleX(mScaleFactor)
            image.setScaleY(mScaleFactor)


            return true
        }
    }
}