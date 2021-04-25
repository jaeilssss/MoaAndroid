package com.moa.moakotlin.ui.imagepicker

import android.Manifest
import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.moa.moakotlin.BuildConfig

class Libraries {
    //permission

    companion object{
        fun requestPermissionStorage(context: Activity?) {
            //lay hinh tu dien thoai
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                //the second show request permission
                val builderExplain =
                    AlertDialog.Builder(context)
                builderExplain.setCancelable(false)
                builderExplain.setMessage("This function requires access to storage")
                builderExplain.setPositiveButton(
                    R.string.yes
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE
                    )
                }
                builderExplain.show()
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE
                )
            }
        }

        fun requestPermissionStorageDeny(context: Activity) {
            //deny
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                //press not allow
                Toast.makeText(
                    context,
                    "You must allow usage of storage access to use this function",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //press never show again
                val builderExplain =
                    AlertDialog.Builder(context)
                builderExplain.setCancelable(false)
                builderExplain.setMessage("This function requires access to storage\n\nDo you want to enable storage access?")
                builderExplain.setPositiveButton(
                    R.string.yes
                ) { dialog, which ->
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
                builderExplain.setNegativeButton(
                    R.string.no
                ) { dialog, which -> dialog.dismiss() }
                builderExplain.show()
            }
        }
    }



}
