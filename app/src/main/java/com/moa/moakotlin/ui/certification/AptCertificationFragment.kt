package com.moa.moakotlin.ui.certification

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.AptCertificationFragmentBinding


class AptCertificationFragment : BaseFragment() {

    companion object {
        fun newInstance() = AptCertificationFragment()
    }

    private lateinit var binding : AptCertificationFragmentBinding

    private lateinit var viewModel: AptCertificationViewModel

    private lateinit var navController: NavController

    private val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.apt_certification_fragment, container, false)

        viewModel = ViewModelProvider(this).get(AptCertificationViewModel::class.java)

        navController = findNavController()

        binding.model = viewModel

        binding.itemGoToAlbum.setOnClickListener {
            when {
                activity?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                            it1,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } ==PackageManager.PERMISSION_GRANTED  &&
                        ContextCompat.checkSelfPermission(
                                activity?.applicationContext!!,
                                android.Manifest.permission.CAMERA
                        )==PackageManager.PERMISSION_GRANTED -> {
                    navController.navigate(R.id.imagePickerViewFragment)
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    //교육용 팝업
                    showContextPopUpPermission()
                }
                else -> {
                    requestPermissions(requiredPermissions, 1000)
                }

            }
        }
            return binding.root
        }

    override fun onBackPressed() {
        navController.popBackStack()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1000){
            navController.navigate(R.id.imagePickerViewFragment)
        }
    }

    fun showContextPopUpPermission(){
        AlertDialog.Builder(context).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기"){ _, _->
                    requestPermissions(requiredPermissions, 1000)
                }
                .setNegativeButton("취소하기"){ _, _->

                }
                .create()
                .show()

    }


}