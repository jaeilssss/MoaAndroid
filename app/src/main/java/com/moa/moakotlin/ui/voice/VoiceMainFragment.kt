package com.moa.moakotlin.ui.voice

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moa.moakotlin.R

class VoiceMainFragment : Fragment() {

    private lateinit var viewModel: VoiceMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.voice_main_fragment, container, false)

        when{
            ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.RECORD_AUDIO
            )==PackageManager.PERMISSION_GRANTED  &&
                    ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )==PackageManager.PERMISSION_GRANTED
            ->{
                var bundle = Bundle()

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)->{
                //교육용!!
                if(activity!=null){
                    showContextPopupPermission()
                }

            }
            else ->{
                println("여기 아냐??  ")
                var permissions = ArrayList<String>()
                permissions.add(android.Manifest.permission.RECORD_AUDIO)
                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                var reqPermissionArray: Array<String?>? =
                    arrayOfNulls(permissions.size)
                reqPermissionArray = permissions.toArray(reqPermissionArray)
                requestPermissions(reqPermissionArray,1000)
            }
        }
        return view
    }

    private fun showContextPopupPermission(){
        AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
            .setMessage("음성 컨탠츠를 이용하시려면 필요한 권한이 있습니다")
            .setPositiveButton("동의하기"){ _, _ ->
                var permissions = ArrayList<String>()
                permissions.add(android.Manifest.permission.RECORD_AUDIO)
                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                var reqPermissionArray: Array<String?>? =
                    arrayOfNulls(permissions.size)
                reqPermissionArray = permissions.toArray(reqPermissionArray)
                requestPermissions(reqPermissionArray,1000)
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .setNegativeButton("취소하기") { _, _ ->}
            .create()
            .show()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VoiceMainViewModel::class.java)

    }

}