package com.moa.moakotlin.ui.voice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.costumdialog.SinglePositiveButtonDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding
import com.moa.moakotlin.recyclerview.voice.VoiceRoomAdapter
import com.moa.moakotlin.repository.voice.VoiceRepository
import com.moa.moakotlin.ui.service.ForecdTerminationService
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class VoiceRoomFragment : BaseFragment() ,AGEventHandler{
    var lastTimeBackPressed : Long = 0
    private lateinit var viewModel: VoiceRoomViewModel

    private lateinit var binding: VoiceRoomFragmentBinding

    private lateinit var navController: NavController

    private lateinit var rtcEngine: RtcEngine

    private lateinit var mRtcEventHandler :IRtcEngineEventHandler

    private val ACTION_WORKER_CONFIG_ENGINE = 0X2012

    private var muteState = false;

    lateinit  var voiceChatRoom : VoiceChatRoom

    var countDownTimer: CountDownTimer ?=null
    var isRequest = false

    var EVENT_TYPE_ON_USER_AUDIO_MUTED = 7

    var EVENT_TYPE_ON_SPEAKER_STATS = 8

    var EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9

    var EVENT_TYPE_ON_AUDIO_QUALITY = 10

    var EVENT_TYPE_ON_APP_ERROR = 13

    var EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED = 18

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.voice_room_fragment, container, false)
        viewModel = ViewModelProvider(this).get(VoiceRoomViewModel::class.java)
        binding.model = viewModel



        var token = arguments?.get("token") as String
         voiceChatRoom  = arguments?.getParcelable<VoiceChatRoom>("voiceChatRoom")!!
        if (voiceChatRoom != null) {
            viewModel.setSnapShotListener(voiceChatRoom.documentID)
            setView(voiceChatRoom)
        }
        navController= findNavController()
       createIRtcEnginHandler()
        initRtcEngine()
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)

        rtcEngine.setEnableSpeakerphone(true)

        rtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                .toString() + File.separator + activity?.applicationContext?.getPackageName() + "/log/agora-rtc.log")
        rtcEngine.setClientRole(1)
//        rtcEngine.enableAudioVolumeIndication(200, 3, false)
        rtcEngine.joinChannel(token, voiceChatRoom?.documentID, "Extra Optional Data", User.getInstance().phoneNumber.toInt())
        binding.VoiceRoomSpeakerRcv.layoutManager = GridLayoutManager(activity?.applicationContext!!, 4)
        binding.VoiceRoomAudienceRcv.layoutManager = GridLayoutManager(activity?.applicationContext!!, 4)

        var speakerAdapter = VoiceRoomAdapter()

        binding.VoiceRoomSpeakerRcv.adapter = speakerAdapter

        var audienceAdapter = VoiceRoomAdapter()

        binding.VoiceRoomAudienceRcv.adapter =  audienceAdapter

        binding.VoiceRoomExitBtn.setOnClickListener { voiceChatRoomExit() }

        binding.VoiceRoomHandBtn.setOnClickListener { requestSpeaker() }



//        requestUser도 지워야함 지그므

        viewModel.audiences.observe(viewLifecycleOwner, Observer {
            audienceAdapter.submitList(it)
            audienceAdapter.map = viewModel.audienceListMap
            audienceAdapter.notifyDataSetChanged()
        })
        viewModel.speakers.observe(viewLifecycleOwner, Observer {
            speakerAdapter.map = viewModel.speakerListMap
            speakerAdapter.submitList(it)
            speakerAdapter.notifyDataSetChanged()
        })
        viewModel.isDelete.observe(viewLifecycleOwner, Observer {
            if (it) {
                countDownTimer = createCountDownTimer(1000L)
                countDownTimer?.start()
                context?.let {
                    SinglePositiveButtonDialog(it)
                            .setMessage("해당 방송이 종료되었습니다!")
                            .setPositiveButton("나가기") {
                                voiceChatRoomExit()
                            }
                            .show()
                }
            }
        })

        viewModel.myVoiceUser.observe(viewLifecycleOwner, Observer {
            setStartService()
            if (it.role.equals("owner")) {
                viewModel.setRequestSpeakerSnapShotListener(voiceChatRoom.documentID)

            } else if (it.role.equals("audience")) {
                rtcEngine.muteLocalAudioStream(true)
                binding.VoiceRoomMicBtn.background = resources.getDrawable(R.drawable.shape_un_selected_hand)
                binding.VoiceRoomMicBtn.isClickable = false

            } else {
                rtcEngine.muteLocalAudioStream(false)
            }

        })

        viewModel.requestUsers.observe(viewLifecycleOwner, Observer {
            binding.VoiceRoomHandCount.isVisible = true
            binding.VoiceRoomHandCount.text = it.size.toString()
        })

        return binding.root
    }



    private fun setStartService(){
        var intent = Intent(activity?.applicationContext,ForecdTerminationService::class.java)
        intent.putExtra("documentID",voiceChatRoom.documentID)
        intent.putExtra("voiceUser",viewModel.myVoiceUser.value!!)
        activity?.startService(intent)
    }


    fun requestSpeaker(){

        viewModel.requestSpeakerUser(voiceChatRoom.documentID)
        Toast.makeText(activity?.applicationContext, "발언권을 요청하였습니다", Toast.LENGTH_SHORT).show()
        isRequest = true
    }
    fun setRole(){

    }
    private fun createCountDownTimer(initialMililis: Long) =
            object : CountDownTimer(initialMililis, 10 * 100L){
                override fun onFinish() {
                    countDownTimer = null

                    voiceChatRoomExit()
                }

                override fun onTick(remainTime: Long) {

                }

            }
    private fun voiceChatRoomExit(){
        CoroutineScope(Dispatchers.Main).launch {

            // 방 나갈때 방을 나가시겠습니까? 라고 물어봐야하지않을까??
            viewModel.deleteVoiceUser(voiceChatRoom?.documentID!!, User.getInstance().uid)
                viewModel.changeAudienceCount(voiceChatRoom?.documentID)
                rtcEngine.leaveChannel()
                viewModel.deleteSnapShot()
                if(voiceChatRoom.owner.equals(User.getInstance().uid)){
                    println("voiceChatRoomExit -> owner ")
                    viewModel.deleteVoiceChatRoom(voiceChatRoom.documentID)
                }
                if(viewModel.speakerList.contains("${User.getInstance().phoneNumber}")){
                    viewModel.changeSpeakersCount(voiceChatRoom.documentID, -1)
                }
                if(isRequest==true){
                    viewModel.deleteRequestUser(voiceChatRoom.documentID, User.getInstance().uid)
                }
                navController.popBackStack(R.id.voiceMainFragment, false)
            }

    }
    private fun setView(voiceChatRoom: VoiceChatRoom){
        binding.VoiceRoomTitle.text = voiceChatRoom.name

        binding.VoiceRoomTopic.text = voiceChatRoom.topic

        binding.VoiceRoomType.text = voiceChatRoom.range

                // 나중에 type 과 range 이름 변경하자..
    }
    private fun setToast(msg: String){
        Toast.makeText(activity?.baseContext!!, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        TODO("Not yet implemented")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        TODO("Not yet implemented")
    }

    override fun onExtraCallback(type: Int, vararg data: Any?) {
        TODO("Not yet implemented")
    }

    private fun initRtcEngine(){
        try {
            rtcEngine = RtcEngine.create(
                    activity?.baseContext,
                    getString(R.string.agora_app_id),
                    mRtcEventHandler
            )

            optional()
        } catch (e: Exception) {
            Log.e("TAG", Log.getStackTraceString(e))
            println("error 나오네 initRtcEngine")
            throw RuntimeException(
                    "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                            e
                    )
            )
        }
        setChannelProfile()
    }
    // Listen for the onJoinChannelSuccess callback.
    // This callback occurs when the local user successfully joins the channel.
    private fun createIRtcEnginHandler() {
        mRtcEventHandler =  object : IRtcEngineEventHandler() {
            override fun onLeaveChannel(stats: RtcStats?) {

                super.onLeaveChannel(stats)
            }

            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
              println("성공!!!@@@@@@")
          }
          override fun onUserJoined(uid: Int, elapsed: Int) {
              super.onUserJoined(uid, elapsed)
              println("join~~~~~%%%%%%")
          }

          override fun onUserOffline(uid: Int, reason: Int) {
              super.onUserOffline(uid, reason)
              println("offline")
          }

          override fun onError(err: Int) {
              super.onError(err)
              println("error handler rtc engin")
          }

            override fun onActiveSpeaker(uid: Int) {
                super.onActiveSpeaker(uid)
            }

            override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
//                super.onRemoteAudioStateChanged(uid, state, reason, elapsed)
                when(reason){
                    1 ->{
                        println("이야기중!! ${uid}")
                    }
                    2->{
                        println("이야기 멈춤")
                    }


                }
            }
        }
    }
    private fun setChannelProfile() {
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)


    }

    private fun optional() {
//       activity?.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
//        activity?.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL)
    }

    private fun HandlerSetting(){
        val envelop = Message()
        envelop.what = ACTION_WORKER_CONFIG_ENGINE
        envelop.obj = arrayOf<Any>(Constants.CLIENT_ROLE_BROADCASTER)
    }

    override fun onDestroy() {
        rtcEngine.leaveChannel()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            voiceChatRoomExit()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(context, "종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show()
    }



    override fun onDetach() {

        super.onDetach()
    }



    override fun onDestroyView() {
//        println("프레그먼트 강제종료 destroyview")
//        if(voiceChatRoom.owner.equals(User.getInstance().uid)){
//            println("voiceChatRoomExit2323232323232 -> owner ")
//            viewModel.deleteVoiceChatRoom(voiceChatRoom.documentID)
//        }else{
//            if(viewModel.speakerList.contains("${User.getInstance().phoneNumber}")){
//                viewModel.changeSpeakersCount(voiceChatRoom.documentID, -1)
//            }
//            if(isRequest==true){
//                viewModel.deleteRequestUser(voiceChatRoom.documentID, User.getInstance().uid)
//            }
//        }
//        var db = FirebaseFirestore.getInstance()
//        db.collection("VoiceChatRoom")
//                .document(voiceChatRoom.documentID)
//                .delete()

//        rtcEngine.leaveChannel()
//        viewModel.deleteSnapShot()






        super.onDestroyView()
    }
}

