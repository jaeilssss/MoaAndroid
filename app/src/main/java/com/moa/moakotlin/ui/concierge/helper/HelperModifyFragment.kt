package com.moa.moakotlin.ui.concierge.helper

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.HelperModifyFragmentBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity

class HelperModifyFragment : Fragment() {

    companion object {
        fun newInstance() = HelperModifyFragment()
    }

    private lateinit var viewModel: HelperModifyViewModel

    private lateinit var binding : HelperModifyFragmentBinding

    lateinit var adapter : CertificationImageAdapter

    var selectedPictureList = ArrayList<String>()

    private  lateinit var helper : Helper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.helper_modify_fragment,container,false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelperModifyViewModel::class.java)
        binding.model = viewModel
        initAdapter()

        arguments.let {
            helper = it?.getParcelable<Helper>("helper")!!
            setData(helper)
        }

        binding.HelperModifyAlbum.setOnClickListener {
            checkPermission()
        }

    }
    private fun setData(helper : Helper){
        viewModel.title.value = helper.title
        viewModel.category.value = helper.mainCategory
        viewModel.content.value = helper.content
        viewModel.hopeWage.value = helper.hopeWage
        viewModel.selectedPictureList.value = helper.images
    }
    private fun setSubmitBtnChange(){
        if(viewModel.checkEdit()){
            binding.HelperModifySubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.HelperModifySubmit.setTextColor(Color.BLACK)
        }else{
            binding.HelperModifySubmit.setBackgroundResource(R.drawable.shape_grey_top_radius_15)
            binding.HelperModifySubmit.setTextColor(Color.WHITE)
        }
    }
    private fun initAdapter(){
        adapter  = CertificationImageAdapter()
        binding.HelperModifyRcv.adapter = adapter

        binding.HelperModifyRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==2000 && requestCode==2000){

            binding.HelperModifyCategory.text = data?.getStringExtra("selectedMainCategory")
            viewModel.category.value = data?.getStringExtra("selectedMainCategory")

        }else if(resultCode==1000 && requestCode==1000){
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                selectedPictureList.addAll(list)
                adapter.submitList(selectedPictureList)
                adapter.notifyDataSetChanged()
                binding.HelperModifyCountPicture.text = selectedPictureList.size.toString()
                viewModel.selectedPictureList.value = selectedPictureList
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
        AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
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
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
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

        var intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra("selectedPictureList",selectedPictureList)
        startActivityForResult(intent,1000)
    }
}