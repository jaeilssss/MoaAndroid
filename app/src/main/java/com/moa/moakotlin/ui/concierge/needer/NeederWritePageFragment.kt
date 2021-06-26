package com.moa.moakotlin.ui.concierge.needer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentNeederWritePageBinding
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter
import com.moa.moakotlin.ui.concierge.category.NeederCategoryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class NeederWritePageFragment : Fragment() {

    lateinit var binding: FragmentNeederWritePageBinding

    lateinit var navController: NavController

    lateinit var model: NeederWritePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_needer_write_page, container, false)
        navController = findNavController()

        model = ViewModelProvider(this).get(NeederWritePageViewModel::class.java)
        binding.model = model

        var now = LocalDateTime.now()

        model.year.set(now.year)
        model.month.set(now.monthValue)
        model.day.set(now.dayOfMonth)


        binding.NeederWriteCategoryLayout.setOnClickListener {

            var intent = Intent(activity,NeederCategoryActivity::class.java)
            startActivityForResult(intent,1000)
        }
        binding.NeederWriteAlbum.setOnClickListener {
            checkPermission()
        }

        return binding.root
    }

    private fun checkPermission(){
        when{
                ContextCompat.checkSelfPermission(
                        activity?.applicationContext!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED ->{


                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                    //교육용!!
                    showContextPopupPermission()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }
    }
    private fun showContextPopupPermission(){
        AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기"){ _, _ ->
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
                .setNegativeButton("취소하기") { _, _ ->}
                .create()
                .show()
    }
    fun selectFirstType(){
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var items = resources.getStringArray(R.array.mainCategory)
        builder.setItems(items,DialogInterface.OnClickListener { dialogInterface, i ->
            model.mainCategory = items.get(i)
            detailType(items.get(i))
        })
        var alertDialog : AlertDialog =builder.create()
        alertDialog.setTitle("원하는 영역을 선택해주세요!")
        alertDialog.show()
    }
    fun detailType(firstType : String){
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var items : Array<String>
        if(firstType.equals("인테리어")){
            items = resources.getStringArray(R.array.interiorDetail)
        }else if(firstType.equals("육아,교육")){
            items = resources.getStringArray(R.array.kidDetail)
        }else if(firstType.equals("품앗이")){
            items = resources.getStringArray(R.array.laborDetail)
        }else if(firstType.equals("반려동물")){
            items = resources.getStringArray(R.array.petDetail)
        }else{
            return
        }
        builder.setItems(items,DialogInterface.OnClickListener { dialogInterface, i ->
            model.subCategory = items.get(i)
        })
        var alertDialog : AlertDialog =builder.create()
        alertDialog.setTitle("세부 영역을 선택해주세요")
        alertDialog.show()
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            1000->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }else{
                    Toast.makeText(context,"권한이 거부되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else->{

            }
        }
    }
    override fun onStop() {
        super.onStop()
    }
}




