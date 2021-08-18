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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.MyApp
import com.moa.moakotlin.MyPhone
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding
import com.moa.moakotlin.databinding.VoiceRoomMakeFragmentBinding
import com.moa.moakotlin.recyclerview.concierge.FlexBoxAdapter
import com.moa.moakotlin.ui.imagepicker.ImagePickerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceRoomMakeFragment : BaseFragment() {


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
        (context as MainActivity).backListener = this
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
        binding.back.setOnClickListener { navController.popBackStack() }
        val flexBoxAdapter = FlexBoxAdapter()

        binding.voiceRoomPictureDelete.setOnClickListener {
            list=null
            binding.voiceRoomPictureDelete.isVisible = false
            binding.VoiceRoomInsertImage.isVisible=false
            viewModel.image = ""
            binding.VoiceRoomImageCount.text = "0/1"
        }

        binding.VoiceRoomMakeSubmit.setOnClickListener { submit() }

        binding.VoiceRoomAroundMyApt.setOnClickListener {
            setBackGround(it,true,binding.VoiceRoomMyAptImg,"우리아파트만")
            setBackGround(binding.VoiceRoomAll,false,binding.VoiceRoomAllImg,"전국")
            setBackGround(binding.VoiceRoomAroundNb,false,binding.VoiceRoomAroundAptImg,"인근아파트까지")

            viewModel.range.value = "우리아파트"
        }
        binding.VoiceRoomAroundNb.setOnClickListener {
            setBackGround(it,true,binding.VoiceRoomAroundAptImg,"인근아파트까지")
            setBackGround(binding.VoiceRoomAll,false,binding.VoiceRoomAllImg,"전국")
            setBackGround(binding.VoiceRoomAroundMyApt,false,binding.VoiceRoomMyAptImg,"우리아파트만")
            viewModel.range.value = "인근"
        }
        binding.VoiceRoomAll.setOnClickListener {
            setBackGround(it,true,binding.VoiceRoomAllImg,"전국")
            setBackGround(binding.VoiceRoomAroundNb,false,binding.VoiceRoomAroundAptImg,"인근아파트까지")
            setBackGround(binding.VoiceRoomAroundMyApt,false,binding.VoiceRoomMyAptImg,"우리아파트만")
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

    override fun onBackPressed() {
        navController.popBackStack()
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
                if(list.size>0){
                    binding.VoiceRoomInsertImage.isVisible = true
                    binding.voiceRoomPictureDelete.isVisible = true
                    Glide.with(activity?.applicationContext!!).load(list.get(0))
                        .into(binding.VoiceRoomInsertImage)
                    viewModel.image = list.get(0)

                    binding.VoiceRoomImageCount.text = "1/1"
                }

            }
        }
    }


    fun setBackGround(view : View ,boolean : Boolean,img : View,type : String){
        if(boolean){
            view.setBackgroundResource(R.drawable.button_shape_main_color)
            if(type.equals("우리아파트만")) img.setBackgroundResource(R.drawable.ic_voice_my_apt_active)
            else if(type.equals("인근아파트까지")) img.setBackgroundResource(R.drawable.ic_voice_around_apt_active)
            else if(type.equals("전국")) img.setBackgroundResource(R.drawable.ic_voice_all_active)
        }else{
            view.setBackgroundResource(R.drawable.shape_grey_radius_15)
            if(type.equals("우리아파트만")) img.setBackgroundResource(R.drawable.ic_voice_my_apt_default)
            else if(type.equals("인근아파트까지")) img.setBackgroundResource(R.drawable.ic_voice_my_around_apt_default)
            else if(type.equals("전국")) img.setBackgroundResource(R.drawable.ic_voice_all_default)
        }
    }

    fun submit(){
        if(context?.let { MyPhone().getBatteryRemain(it) }!! <20 ){
            context?.let {
                SinglePositiveButtonDialog(it)
                        .setMessage("모아라디오를 이용하시려면\n배터리 잔량이 20%이상 있어야 가능합니다!")
                        .setPositiveButton("예"){

                        }.show()
            }

        }else{
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
                    if(User.getInstance().aptCode.equals("MOA")){
                        viewModel.sendPushMoa()
                    }else{
                        viewModel.sendPushMessage()
                    }
                    navController.navigate(R.id.voiceRoomFragment,bundle)
                }

            }
        }

    }
}
