package com.moa.moakotlin.ui.voice

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.VoiceMainFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceMainFragment : Fragment() {

    private lateinit var viewModel: VoiceMainViewModel

    private lateinit var binding: VoiceMainFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.voice_main_fragment,container,false)
        viewModel = ViewModelProvider(this).get(VoiceMainViewModel::class.java)
        binding.model = viewModel

        navController = findNavController()

        permission()

        binding.join.setOnClickListener {

            if(viewModel.checkChannelName()){
                CoroutineScope(Dispatchers.Main).launch {
                 var result = viewModel.generateToken()
                    var bundle = Bundle()
                    bundle.putString("token",result)
                     navController.navigate(R.id.voiceRoomFragment,bundle)
                }

            }
        }
        return binding.root
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
    private fun permission(){
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
                var permissions = ArrayList<String>()
                permissions.add(android.Manifest.permission.RECORD_AUDIO)
                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                var reqPermissionArray: Array<String?>? =
                    arrayOfNulls(permissions.size)
                reqPermissionArray = permissions.toArray(reqPermissionArray)
                requestPermissions(reqPermissionArray,1000)
            }
        }
    }

}