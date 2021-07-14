package com.moa.moakotlin.ui.voice

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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding
import com.moa.moakotlin.databinding.VoiceRoomMakeFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.FlexBoxAdapter
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceRoomMakeFragment : Fragment() {


    private lateinit var viewModel: VoiceRoomMakeViewModel

    private lateinit var binding: VoiceRoomMakeFragmentBinding

    private lateinit var navController: NavController

    private var selectedPictureList = ArrayList<String>()

    var list :ArrayList<String> ?=null

    companion object {
        val REQUEST_PICTURE_CODE = 1000
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.voice_room_make_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VoiceRoomMakeViewModel::class.java)
        binding.model = viewModel

        navController = findNavController()


        viewModel.theme.observe(viewLifecycleOwner, Observer { setSubmitBackGroundChange() })
        viewModel.title.observe(viewLifecycleOwner, Observer { setSubmitBackGroundChange() })
        viewModel.range.observe(viewLifecycleOwner, Observer { setSubmitBackGroundChange() })
        binding.VoiceRoomGoToAlbum.setOnClickListener { checkPermission() }

        val flexBoxAdapter = FlexBoxAdapter()

        binding.itemAptCertificationClose.setOnClickListener {
            list=null
            binding.itemAptCertificationClose.isVisible = false
            binding.VoiceRoomInsertImage.isVisible=false
            viewModel.image = ""
        }

        binding.VoiceRoomMakeSubmit.setOnClickListener { submit() }

        binding.VoiceRoomAroundMyApt.setOnClickListener {
            setBackGround(it,true)
            setBackGround(binding.VoiceRoomAll,false)
            setBackGround(binding.VoiceRoomAroundNb,false)

            viewModel.range.value = "우리아파트"
        }
        binding.VoiceRoomAroundNb.setOnClickListener {
            setBackGround(it,true)
            setBackGround(binding.VoiceRoomAll,false)
            setBackGround(binding.VoiceRoomAroundMyApt,false)
            viewModel.range.value = "인근"
        }
        binding.VoiceRoomAll.setOnClickListener {
            setBackGround(it,true)
            setBackGround(binding.VoiceRoomAroundNb,false)
            setBackGround(binding.VoiceRoomAroundMyApt,false)
            viewModel.range.value = "전국"
        }
        FlexboxLayoutManager(activity?.applicationContext)
                .apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.CENTER
                }.let {
                    binding.VoiceRoomMakeRcv.layoutManager = it
                    binding.VoiceRoomMakeRcv.adapter = flexBoxAdapter

                    var data = resources.getStringArray(R.array.voiceChatTheme)
                    flexBoxAdapter.submitList(data.asList())
                }
        flexBoxAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                flexBoxAdapter.checked = position
                flexBoxAdapter.notifyDataSetChanged()
                viewModel.theme.value = flexBoxAdapter.currentList[position]
            }
        })
    }

    private fun setSubmitBackGroundChange() {
        if (viewModel.check()) {
            binding.VoiceRoomMakeSubmit.isClickable = true
            binding.VoiceRoomMakeSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.VoiceRoomMakeSubmit.setTextColor(Color.BLACK)
        } else {
            binding.VoiceRoomMakeSubmit.isClickable = false
            binding.VoiceRoomMakeSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.VoiceRoomMakeSubmit.setTextColor(Color.WHITE)
        }
    }

    private fun checkPermission() {
        when {

            ContextCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {

                goToAlbum()

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                //교육용!!
                showContextPopupPermission()
            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }

    private fun showContextPopupPermission() {
        AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
                .setMessage("사진을 불러오기 위해 권한이 필요합니다")
                .setPositiveButton("동의하기") { _, _ ->
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
                .setNegativeButton("취소하기") { _, _ -> }
                .create()
                .show()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToAlbum()
                } else {
                    Toast.makeText(context, "권한이 거부되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }

    private fun goToAlbum() {
        var intent = Intent(activity, ImagePickerActivity::class.java)
        intent.putExtra("selectedPictureList", selectedPictureList)
        intent.putExtra("singleImage", "singleImage")
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_PICTURE_CODE && resultCode == REQUEST_PICTURE_CODE) {
            var list = data?.getStringArrayListExtra("selectedPictures")

            if (list != null) {
                binding.VoiceRoomInsertImage.isVisible = true
                binding.itemAptCertificationClose.isVisible = true
                Glide.with(activity?.applicationContext!!).load(list.get(0))
                        .into(binding.VoiceRoomInsertImage)
                viewModel.image = list.get(0)
            }
        }
    }


    fun setBackGround(view : View ,boolean : Boolean){
        if(boolean){
            view.setBackgroundResource(R.drawable.button_shape_main_color)
        }else{
            view.setBackgroundResource(R.drawable.shape_unable_radius_15)
        }
    }

    fun submit(){
        CoroutineScope(Dispatchers.Main).launch {
            binding.NeederWriteLoading.show()
            var token = viewModel.submit()
            var voiceChatRoom = viewModel.voiceChatRoom
            voiceChatRoom.documentID = viewModel.documentID
            if(viewModel.makeVoiceUser(voiceChatRoom.documentID)){
                var bundle = Bundle()
                bundle.putString("token",token)
                bundle.putParcelable("voiceChatRoom",voiceChatRoom)
                binding.NeederWriteLoading.hide()
                navController.navigate(R.id.voiceRoomFragment,bundle)
            }

        }
    }
}
