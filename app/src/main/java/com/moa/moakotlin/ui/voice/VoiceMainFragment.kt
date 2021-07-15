package com.moa.moakotlin.ui.voice

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.VoiceMainFragmentBinding
import com.moa.moakotlin.recyclerview.voice.VoiceMainAdapter
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceMainFragment : BaseFragment() {

    private lateinit var viewModel: VoiceMainViewModel

    private lateinit var binding: VoiceMainFragmentBinding

    private lateinit var navController: NavController

    private lateinit var adapter : VoiceMainAdapter

    private lateinit var myActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)

        myActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.voice_main_fragment,container,false)
        viewModel = ViewModelProvider(this).get(VoiceMainViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()

        myActivity.bottomNavigationGone()

        adapter = VoiceMainAdapter()

        binding.VoiceMainRcv.adapter = adapter

        binding.VoiceMainRcv.layoutManager = LinearLayoutManager(activity?.applicationContext)

        permission()
        initView()

        binding.VoiceMainCreageRoomBtn.setOnClickListener {checkAptCertification()}
        viewModel.voiceChatRoomList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })


        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.VoiceMainLoadingProgressBar.show()
                    if(viewModel.goToVoiceRoom(adapter.currentList[position].documentID)){

                        var token = adapter.currentList[position].documentID?.let { viewModel.generateToken(it) }

                        var bundle = Bundle()

                        bundle.putString("token",token)
                        bundle.putParcelable("voiceChatRoom",adapter.currentList[position])

                        viewModel.increasePeopleCount(position)
                        binding.VoiceMainLoadingProgressBar.hide()
                        navController.navigate(R.id.voiceRoomFragment,bundle)
                    }else{
                        binding.VoiceMainLoadingProgressBar.hide()
                        showToast(activity?.applicationContext!!,"접속이 불가능합니다")
                    }
                }

            }

        })
        return binding.root
    }


    fun initView(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.initGetVoiceChatRoomList()
        }
    }
    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun checkAptCertification(){
        if(User.getInstance().certificationStatus.equals("인증").not()){
            Toast.makeText(context,"아파트 인증된 회원만 방을 개설 할 수 있습니다",Toast.LENGTH_SHORT).show()
        }else{
            navController.navigate(R.id.voiceRoomMakeFragment)
        }
    }
    private fun showContextPopupPermission(){
        AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
            .setMessage("음성 컨탠츠를 이용하시려면 필요한 권한이 있습니다")
            .setPositiveButton("동의하기"){ _, _ ->
                var permissions = ArrayList<String>()
                permissions.add(android.Manifest.permission.RECORD_AUDIO)
                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                var reqPermissionArray: Array<String?>? =
                    arrayOfNulls(permissions.size)
                reqPermissionArray = permissions.toArray(reqPermissionArray)
                requestPermissions(reqPermissionArray,1000)
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .setNegativeButton("취소하기") { _, _ ->}
            .create()
            .show()
    }
    private fun permission(){
        when{
            ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.RECORD_AUDIO
            )==PackageManager.PERMISSION_GRANTED  &&
                    ContextCompat.checkSelfPermission(
                        activity?.applicationContext!!,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )==PackageManager.PERMISSION_GRANTED
            ->{
                var bundle = Bundle()

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)->{
                //교육용!!
                if(activity!=null){
                    showContextPopupPermission()
                }

            }
            else ->{
                var permissions = ArrayList<String>()
                permissions.add(android.Manifest.permission.RECORD_AUDIO)
                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                var reqPermissionArray: Array<String?>? =
                    arrayOfNulls(permissions.size)
                reqPermissionArray = permissions.toArray(reqPermissionArray)
                requestPermissions(reqPermissionArray,1000)
            }
        }
    }

}