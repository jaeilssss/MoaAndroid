package com.moa.moakotlin.ui.claim

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.databinding.ClaimModifyFragmentBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ClaimModifyFragment : BaseFragment() {

    private lateinit var viewModel: ClaimModifyViewModel

    private lateinit var binding : ClaimModifyFragmentBinding

    private lateinit var navController: NavController

    private lateinit var complaint: Complaint

    lateinit var adapter : CertificationImageAdapter

    var uploadedPosition =-1

    var selectedPictureList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (context as MainActivity).backListener = this
        binding = DataBindingUtil.inflate(inflater , R.layout.claim_modify_fragment , container , false)
        adapter = CertificationImageAdapter()
        binding.back.setOnClickListener { navController.popBackStack() }
        binding.ClaimModifySubmit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                binding.ClaimModifyLoading.show()
                viewModel.submit(selectedPictureList,complaint)

            }
        }
        binding.ClaimModifyRcv.adapter = adapter
        adapter.setOnItemCLickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                when(v.id){
                    R.id.itemAptCertificationClose->{
                        if(position<=uploadedPosition ){

                            viewModel.uploadedPosition--
                            uploadedPosition--

                            complaint.images.removeAt(position)
//                            selectedPictureList.removeAt(position)
                            var list = ArrayList<String>()
                            list.addAll(complaint.images)
                            list.addAll(selectedPictureList)
                            viewModel.selectedPictureList.value = list
//                            adapter.submitList(viewModel.selectedPictureList.value)
//                            adapter.notifyDataSetChanged()

                        }else{
                            selectedPictureList.removeAt(position)
                            viewModel.selectedPictureList.value = selectedPictureList
//                            adapter.submitList(viewModel.selectedPictureList.value)
//                            adapter.notifyDataSetChanged()
                        }

//                        binding.ClaimModifyCountPicture.text = adapter.currentList.size.toString()
                    }
                }
            }
        })

        binding.ClaimModifyRcv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        binding.ClaimModifyAlbum.setOnClickListener { checkPermission() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ClaimModifyViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()

        arguments?.let{
            complaint = it.getParcelable<Complaint>("complaint")!!
            viewModel.setViewData(complaint)
            binding.ClaimModifyCategory.text = complaint.category
            if(complaint.images.size>0){
                uploadedPosition = complaint.images.size-1
            }
        }

        viewModel.title.observe(viewLifecycleOwner, Observer {
            setChangeBackgroundBtn()
        })

        viewModel.content.observe(viewLifecycleOwner, Observer {
            setChangeBackgroundBtn()
        })

        viewModel.category.observe(viewLifecycleOwner, Observer {
            setChangeBackgroundBtn()
        })

        viewModel.selectedPictureList.observe(viewLifecycleOwner, Observer {

            binding.ClaimModifyCountPicture.text = it.size.toString()

            adapter.submitList(it)

            adapter.notifyDataSetChanged()
        })
        viewModel.newData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,"글이 수정되었습니다",Toast.LENGTH_SHORT).show()
            var bundle = Bundle()
            bundle.putParcelable("complaint",it)
            navController.navigate(R.id.action_claimModifyFragment_to_claimReadFragment,bundle)
            binding.ClaimModifyLoading.hide()
        })
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


    fun setChangeBackgroundBtn(){

        if(viewModel.check()){
            binding.ClaimModifySubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.ClaimModifySubmit.setTextColor(Color.BLACK)
            binding.ClaimModifySubmit.isClickable = true

        }else{
            binding.ClaimModifySubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.ClaimModifySubmit.isClickable = false
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode== ClaimWriteFragment.REQUEST_CATEGORY_SELECTION
                && resultCode== ClaimWriteFragment.REQUEST_CATEGORY_SELECTION){

            viewModel.category.value= data?.getStringExtra("category")
            binding.ClaimModifyCategory.text = viewModel.category.value

        }else if(requestCode== ClaimWriteFragment.REQUEST_PHOTO_CODE && resultCode == ClaimWriteFragment.REQUEST_PHOTO_CODE){
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                selectedPictureList.addAll(list)
                var temp = ArrayList<String>()
                temp.addAll(complaint.images)
                temp.addAll(selectedPictureList)
                viewModel.selectedPictureList.value = temp

            }
        }
    }

}