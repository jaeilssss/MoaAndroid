package com.moa.moakotlin.ui.concierge.needer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.FragmentNeederWriteBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.ui.concierge.category.NeederCategoryActivity
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederWriteFragment : Fragment() {

    lateinit var binding : FragmentNeederWriteBinding

    lateinit var navController: NavController

    lateinit var model : NeederWriteViewModel

    var selectedPictureList = ArrayList<String>()

    lateinit var adapter : CertificationImageAdapter

    companion object{
        var REQUEST_CATEGORY_SELECTION = 2000
        var REQUEST_HOPE_DATE_SELECT_CODE = 6000
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_needer_write,container,false)


        model = ViewModelProvider(this).get(NeederWriteViewModel::class.java)

        binding.model = model

        binding.NeederWriteCategoryLayout.setOnClickListener { selectCategory() }
        binding.NeederWriteHopeDateLayout.setOnClickListener { selectHopeDate() }
        binding.NeederWriteAlbum.setOnClickListener { checkPermission() }
        binding.NeederWriteSubmit.setOnClickListener {
            binding.NeederWriteLoading.show()
            activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            CoroutineScope(Dispatchers.Main).launch {
                model.submit()
            }

        }
        model.title.observe(viewLifecycleOwner, Observer {setButtonBackgroundChange()})
        model.content.observe(viewLifecycleOwner, Observer { setButtonBackgroundChange() })
        model.wage.observe(viewLifecycleOwner, Observer { setButtonBackgroundChange() })
        model.mainCategory.observe(viewLifecycleOwner, Observer { setButtonBackgroundChange() })
        model.subCategory.observe(viewLifecycleOwner, Observer { setButtonBackgroundChange() })
        model.hopeDate.observe(viewLifecycleOwner, Observer { setButtonBackgroundChange() })

        model.selectedPictureList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.NeederWriteCountPicture.text = it.size.toString()
        })
        model.newNeeder.observe(viewLifecycleOwner, Observer {

            Toast.makeText(context,"작성이 완료되었습니다",Toast.LENGTH_SHORT).show()
            binding.NeederWriteLoading.hide()
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            activity?.finish()
        })
        initAdapter()
        return binding.root


    }




    fun setButtonBackgroundChange(){
        if(model.check()){
            binding.NeederWriteSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.NeederWriteSubmit.isClickable = true
        }else{
            binding.NeederWriteSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.NeederWriteSubmit.isClickable =false
        }
    }
    fun initAdapter(){
        adapter  = CertificationImageAdapter()
        binding.NeederWriteRcv.adapter = adapter

        binding.NeederWriteRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)

        adapter.setOnItemCLickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                    selectedPictureList.removeAt(position)
                    model.selectedPictureList.value = selectedPictureList
            }

        })
    }
    fun selectCategory(){
        var intent = Intent(activity,NeederCategoryActivity::class.java)

        startActivityForResult(intent, REQUEST_CATEGORY_SELECTION)
    }

    fun selectHopeDate(){
        var intent = Intent(activity,CustomCalendarActivity::class.java)

        startActivityForResult(intent, REQUEST_HOPE_DATE_SELECT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== REQUEST_CATEGORY_SELECTION && resultCode == REQUEST_CATEGORY_SELECTION){

            model.mainCategory.value = data?.getStringExtra("mainCategory")
            model.subCategory.value = data?.getStringExtra("subCategory")

            binding.NeederWriteCategory.text = "${data?.getStringExtra("mainCategory")}  /  ${data?.getStringExtra("subCategory")}"

        }
        if(requestCode == REQUEST_HOPE_DATE_SELECT_CODE && resultCode == REQUEST_HOPE_DATE_SELECT_CODE){
            binding.NeederWriteHopeDateText.text = "${data?.getStringExtra("hopeYear")}년 ${data?.getStringExtra("hopeMonth")}월 ${data?.getStringExtra("hopeDay")}일"
            model.hopeDate.value= "${data?.getStringExtra("hopeYear")}년 ${data?.getStringExtra("hopeMonth")}월 ${data?.getStringExtra("hopeDay")}일"
        }
        if(resultCode==1000 && requestCode==1000){
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                selectedPictureList.addAll(list)
                adapter.submitList(selectedPictureList)
                adapter.notifyDataSetChanged()
                model.selectedPictureList.value = selectedPictureList
            }
        }

    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )== PackageManager.PERMISSION_GRANTED ->{

                goToAlbum()

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                //교육용!!
                showContextPopupPermission()
            }
            else ->{
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
        }
    }

    private fun goToAlbum(){

        var intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra("selectedPictureList",selectedPictureList)
        startActivityForResult(intent,1000)
    }
    private fun showContextPopupPermission(){
        AlertDialog.Builder(context).setTitle("권한이 필요합니다")
            .setMessage("사진을 불러오기 위해 권한이 필요합니다")
            .setPositiveButton("동의하기"){ _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .setNegativeButton("취소하기") { _, _ ->}
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            1000->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    goToAlbum()
                }else{
                    Toast.makeText(context,"권한이 거부되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else->{

            }
        }
    }
}