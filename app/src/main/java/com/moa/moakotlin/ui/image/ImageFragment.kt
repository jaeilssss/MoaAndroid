package com.moa.moakotlin.ui.image
import android.os.Bundle
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ImageFragmentBinding


class ImageFragment : Fragment() {
    enum class TOUCH_MODE {
        NONE,   // 터치 안했을 때
        SINGLE, // 한손가락 터치
        MULTI   //두손가락 터치
    }


    // https://elliot-kim.github.io/boostcourse/boostcourse-6-pinch_zoom/


    private lateinit var mScaleGestureDetector : ScaleGestureDetector

     var mScaleFactor  = 1.0f


    companion object {
        fun newInstance() = ImageFragment()
    }

    private lateinit var viewModel : ImageViewModel

    private lateinit var binding : ImageFragmentBinding

    private lateinit var navController : NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.image_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        navController = findNavController()

       mScaleGestureDetector = ScaleGestureDetector(requireContext(),ScaleListener())

    }

    
     fun onTouchEvent(motionEvent: MotionEvent) : Boolean{

        mScaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            // ScaleGestureDetector에서 factor를 받아 변수로 선언한 factor에 넣고
            mScaleFactor *= scaleGestureDetector.scaleFactor

            // 최대 10배, 최소 10배 줌 한계 설정
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f))

            // 이미지뷰 스케일에 적용
            binding.image.setScaleX(mScaleFactor)
            binding.image.setScaleY(mScaleFactor)
            return true
        }
    }
}