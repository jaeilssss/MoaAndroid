package com.moa.moakotlin.ui.concierge.needer

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
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.NeederModifyFragmentBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.ui.concierge.category.NeederCategoryActivity
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederModifyFragment : Fragment() {



    private lateinit var viewModel: NeederModifyViewModel

    private lateinit var binding : NeederModifyFragmentBinding

    private lateinit var needer : Needer

    var uploadedPosition =-1

    var selectedPictureList = ArrayList<String>()

    lateinit var adapter : CertificationImageAdapter

    companion object{
        var REQUEST_CATEGORY_SELECTION = 2000
        var REQUEST_HOPE_DATE_SELECT_CODE = 6000
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.needer_modify_fragment,container,false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederModifyViewModel::class.java)
        binding.model = viewModel
        arguments?.let{
            needer = it.getParcelable<Needer>("Needer")!!
            initSetData()
        }
        initAdapter()

        binding.NeederModifyCategoryLayout.setOnClickListener { Toast.makeText(context,"카테고리는 변경이 불가능 합니다",Toast.LENGTH_SHORT).show() }

        binding.NeederModifyHopeDateLayout.setOnClickListener { selectHopeDate() }

        binding.NeederModifyAlbum.setOnClickListener { checkPermission() }

        viewModel.title.observe(viewLifecycleOwner, Observer {setSubmitBtnChange()})
        viewModel.mainCategory.observe(viewLifecycleOwner, Observer {setSubmitBtnChange()})
        viewModel.hopeDate.observe(viewLifecycleOwner, Observer { setSubmitBtnChange() })
        viewModel.wage.observe(viewLifecycleOwner, Observer { setSubmitBtnChange() })
        viewModel.content.observe(viewLifecycleOwner, Observer { setSubmitBtnChange() })
        binding.NeederModifyHopeDateText.text = needer.hopeDate
        binding.NeederModifyCategory.text = "${needer.mainCategory}  /  ${needer.subCategory}"
        binding.NeederModifySubmit.setOnClickListener {
            binding.NeederModifySubmit.isClickable = false
            binding.NeederModifyLoading.show()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.submit(uploadedPosition,needer)
            }
        }
        viewModel.selectedPictureList.observe(viewLifecycleOwner, Observer {

            binding.NeederModifyCountPicture.text = it.size.toString()
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
        viewModel.newNeeder.observe(viewLifecycleOwner, Observer {

            Toast.makeText(activity?.applicationContext,"수정이 완료되었습니다",Toast.LENGTH_SHORT).show()

            var intent = Intent()

            intent.putExtra("newHelper",it)

            activity?.setResult(4000,intent)

            activity?.finish()
            binding.NeederModifySubmit.isClickable = true
            binding.NeederModifyLoading.hide()

        })

    }

    fun selectCategory(){
        var intent = Intent(activity, NeederCategoryActivity::class.java)

        startActivityForResult(intent, NeederWriteFragment.REQUEST_CATEGORY_SELECTION)
    }

    fun selectHopeDate(){
        var intent = Intent(activity,CustomCalendarActivity::class.java)

        startActivityForResult(intent, NeederWriteFragment.REQUEST_HOPE_DATE_SELECT_CODE)
    }
    fun initSetData(){
        viewModel.title.value = needer.title
        viewModel.content.value = needer.content
        viewModel.isNego.value = needer.isNego
        viewModel.mainCategory.value = needer.mainCategory
        viewModel.subCategory.value = needer.subCategory
        viewModel.hopeDate.value = needer.hopeDate
        viewModel.wage.value = needer.hopeWage
        viewModel.selectedPictureList.value = needer.images

        viewModel.selectedPictureList.value = needer.images
        selectedPictureList.addAll(viewModel.selectedPictureList.value!!)
        if(needer.images!!.size>0){
            uploadedPosition = needer.images!!.size-1
        }
    }

    private fun setSubmitBtnChange(){
        if(viewModel.checkEdit()){
            binding.NeederModifySubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.NeederModifySubmit.setTextColor(Color.BLACK)
        }else{
            binding.NeederModifySubmit.setBackgroundResource(R.drawable.shape_grey_top_radius_15)
            binding.NeederModifySubmit.setTextColor(Color.WHITE)
        }
    }
    private fun initAdapter(){
        adapter  = CertificationImageAdapter()
        binding.NeederModifyRcv.adapter = adapter

        binding.NeederModifyRcv.layoutManager = LinearLayoutManager(activity?.applicationContext!!,
            LinearLayoutManager.HORIZONTAL,false)

        adapter.setOnItemCLickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                if(position<=uploadedPosition && uploadedPosition!=-1){
                    uploadedPosition--
                    selectedPictureList.removeAt(position)
                    needer.images?.removeAt(position)
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
        if(resultCode== REQUEST_CATEGORY_SELECTION && requestCode==REQUEST_CATEGORY_SELECTION){

            viewModel.mainCategory.value = data?.getStringExtra("mainCategory")
            viewModel.subCategory.value = data?.getStringExtra("subCategory")

            binding.NeederModifyCategory.text = "${data?.getStringExtra("mainCategory")}  /  ${data?.getStringExtra("subCategory")}"

        }else if(resultCode== 1000 && requestCode==1000){
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                selectedPictureList.addAll(list)
                adapter.submitList(selectedPictureList)
                adapter.notifyDataSetChanged()
                binding.NeederModifyCountPicture.text = selectedPictureList.size.toString()
                viewModel.selectedPictureList.value = selectedPictureList
            }
        }else if(requestCode == REQUEST_HOPE_DATE_SELECT_CODE && resultCode ==REQUEST_HOPE_DATE_SELECT_CODE){
            binding.NeederModifyHopeDateText.text = "${data?.getStringExtra("hopeYear")}년 ${data?.getStringExtra("hopeMonth")}월 ${data?.getStringExtra("hopeDay")}일"
            viewModel.hopeDate.value= "${data?.getStringExtra("hopeYear")}년 ${data?.getStringExtra("hopeMonth")}월 ${data?.getStringExtra("hopeDay")}일"
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
