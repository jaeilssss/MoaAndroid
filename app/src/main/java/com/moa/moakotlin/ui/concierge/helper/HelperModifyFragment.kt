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
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.WebViewActivity
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.HelperModifyFragmentBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.ui.concierge.category.HelperCategoryActivity
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelperModifyFragment : Fragment() {

    companion object {
        fun newInstance() = HelperModifyFragment()
    }

    private lateinit var viewModel: HelperModifyViewModel

    private lateinit var binding : HelperModifyFragmentBinding

    lateinit var adapter : CertificationImageAdapter
    var url = "https://moaapt.notion.site/6d1c2fe592af434692501480dfe92d23"
    var selectedPictureList = ArrayList<String>()

    private  lateinit var helper : Helper
    var uploadedPosition =-1
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

        viewModel.selectedPictureList.observe(viewLifecycleOwner, Observer {

            binding.HelperModifyCountPicture.text = it.size.toString()
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
        binding.HelperModifyCategoryLayout.setOnClickListener {

            var intent = Intent(activity, HelperCategoryActivity::class.java)
            startActivityForResult(intent,2000)
        }
        viewModel.title.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        viewModel.hopeWage.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        viewModel.content.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })
        viewModel.category.observe(viewLifecycleOwner, Observer {
            setSubmitBtnChange()
        })

        viewModel.newHelper.observe(viewLifecycleOwner, Observer {

            Toast.makeText(activity?.applicationContext,"수정이 완료되었습니다",Toast.LENGTH_SHORT).show()

            var intent = Intent()

            intent.putExtra("newHelper",it)

           activity?.setResult(4000,intent)

                activity?.finish()
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                binding.HelperModifyLoading.hide()

        })
        binding.HelperModifySubmit.setOnClickListener {
            activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            binding.HelperModifyLoading.show()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.submit(uploadedPosition,helper)
            }
        }

        binding.NeederModifyGoToGuide.setOnClickListener { goToWebView() }
    }

    fun goToWebView(){
        var intent = Intent(activity, WebViewActivity::class.java)

        intent.putExtra("url",url)

        startActivity(intent)
    }
    private fun setData(helper : Helper){
        viewModel.title.value = helper.title
        viewModel.category.value = helper.mainCategory
        viewModel.content.value = helper.content
        viewModel.hopeWage.value = helper.hopeWage
        viewModel.selectedPictureList.value = helper.images
        selectedPictureList.addAll(viewModel.selectedPictureList.value!!)
        if(helper.images!!.size>0){
            uploadedPosition = helper.images!!.size-1
        }
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

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.HelperModifyRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)

        adapter.setOnItemCLickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                if(position<=uploadedPosition && uploadedPosition!=-1){
                    uploadedPosition--
                    selectedPictureList.removeAt(position)
                    helper.images?.removeAt(position)
                    viewModel.selectedPictureList.value = selectedPictureList

                }else{
                    selectedPictureList.removeAt(position)
                    viewModel.selectedPictureList.value = selectedPictureList
                }


            }

        })
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