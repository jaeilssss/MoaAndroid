package com.moa.moakotlin.ui.concierge.helper

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.FragmentHelperWritePageBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.ui.concierge.category.HelperCategoryActivity
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HelperWritePageFragment : Fragment() {

    lateinit var binding: FragmentHelperWritePageBinding

    lateinit var model: HelperWritePageViewModel

    lateinit var adapter : CertificationImageAdapter

    var selectedPictureList = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_helper_write_page, container, false)


        model = ViewModelProvider(this).get(HelperWritePageViewModel::class.java)
        binding.model = model

        binding.back.setOnClickListener { activity?.finish() }
        binding.HelperWriteCategoryLayout.setOnClickListener {
            var intent = Intent(activity,HelperCategoryActivity::class.java)
            startActivityForResult(intent,2000)
        }
        binding.HelperWriteAlbum.setOnClickListener {
            checkPermission()
        }

        binding.HelperWriteSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                binding.HelperWriteLoading.show()
                activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

               model.submit()
               model.newHelper.observe(viewLifecycleOwner, Observer {
                   var bundle = Bundle()
                   bundle.putParcelable("helper",it)
                   Toast.makeText(activity?.applicationContext,"작성이 완료되었습니다",Toast.LENGTH_SHORT).show()
                   activity?.finish()
                   activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                   binding.HelperWriteLoading.hide()
               })
            }
        }


        initAdapter()
        binding.HelperWriteCountPicture.text = "0"

        model.title.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        model.category.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        model.content.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        model.hopeWage.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        setSubmitBtnChange()

        return binding.root
    }

    fun setSubmitBtnChange(){
        if(model.checkEdit()){
            binding.HelperWriteSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.HelperWriteSubmit.setTextColor(Color.BLACK)
        }else{
            binding.HelperWriteSubmit.setBackgroundResource(R.drawable.shape_grey_top_radius_15)
            binding.HelperWriteSubmit.setTextColor(Color.WHITE)
        }
    }
    fun initAdapter(){
        adapter  = CertificationImageAdapter()
        binding.HelperWriteRcv.adapter = adapter

        binding.HelperWriteRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,LinearLayoutManager.HORIZONTAL,false)

        adapter.setOnItemCLickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                selectedPictureList.removeAt(position)
                model.selectedPictureList.value = selectedPictureList
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==2000 && requestCode==2000){

            binding.HelperWriteCategory.text = data?.getStringExtra("selectedMainCategory")
            model.category.value = data?.getStringExtra("selectedMainCategory")

        }else if(resultCode==1000 && requestCode==1000){
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                 selectedPictureList.addAll(list)
                adapter.submitList(selectedPictureList)
                adapter.notifyDataSetChanged()
                binding.HelperWriteCountPicture.text = selectedPictureList.size.toString()
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

    private fun goToAlbum(){

        var intent = Intent(activity,ImagePickerActivity::class.java)
        intent.putExtra("selectedPictureList",selectedPictureList)
        startActivityForResult(intent,1000)
    }
    override fun onStop() {
        super.onStop()
    }


}




