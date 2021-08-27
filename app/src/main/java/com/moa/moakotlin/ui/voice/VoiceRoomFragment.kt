package com.moa.moakotlin.ui.voice

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
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
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.custom.SinglePositiveButtonDialog
import com.moa.moakotlin.data.CurrentVoice
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding
import com.moa.moakotlin.recyclerview.voice.NewVoiceRoomAdapter
import com.moa.moakotlin.ui.bottomsheet.VoiceRoomRequestUserFragment
import com.moa.moakotlin.ui.bottomsheet.VoiceRoomUserProfileBottomSheet
import com.moa.moakotlin.ui.service.ForecdTerminationService
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VoiceRoomFragment : BaseFragment() {
    var lastTimeBackPressed : Long = 0
    private lateinit var viewModel: VoiceRoomViewModel

    private lateinit var binding: VoiceRoomFragmentBinding

    private lateinit var navController: NavController

    private lateinit var rtcEngine: RtcEngine

    private lateinit var mRtcEventHandler :IRtcEngineEventHandler

    private val ACTION_WORKER_CONFIG_ENGINE = 0X2012

    private var muteState = false;

    lateinit  var voiceChatRoom : VoiceChatRoom
    var speakerAdapter = NewVoiceRoomAdapter()

    var countDownTimer: CountDownTimer ?=null
    var isRequest = false
    var isClose = false



    var EVENT_TYPE_ON_USER_AUDIO_MUTED = 7

    var EVENT_TYPE_ON_SPEAKER_STATS = 8

    var EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9

    var EVENT_TYPE_ON_AUDIO_QUALITY = 10

    var EVENT_TYPE_ON_APP_ERROR = 13

    var EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED = 18

    var isInitData  = false
    var initCount = 0
    var audienceAdapter = NewVoiceRoomAdapter()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        if (voiceChatRoom != null) {
//            setVoiceRoomListener()
//
//        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        CurrentVoice.getInstance().boolean = true
        myActivity.isResume.value = false
        binding = DataBindingUtil.inflate(inflater, R.layout.voice_room_fragment, container, false)
        viewModel = ViewModelProvider(this).get(VoiceRoomViewModel::class.java)
        binding.model = viewModel
        var initCount = 0
        var token = arguments?.get("token") as String


         voiceChatRoom  = arguments?.getParcelable<VoiceChatRoom>("voiceChatRoom")!!
        if(viewModel.mlistener ==null ){
            setVoiceRoomListener()
        }
        setView(voiceChatRoom)

        createIRtcEnginHandler()
        initRtcEngine()
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)

        rtcEngine.setClientRole(1)

        rtcEngine.enableAudioVolumeIndication(0,100,true)

        rtcEngine.joinChannel(token, voiceChatRoom?.documentID, "Extra Optional Data", User.getInstance().phoneNumber.toInt())

        navController= findNavController()

        binding.VoiceRoomSpeakerRcv.layoutManager = GridLayoutManager(activity?.applicationContext!!, 3)

        binding.VoiceRoomAudienceRcv.layoutManager = GridLayoutManager(activity?.applicationContext!!, 3)

        binding.VoiceRoomSpeakerRcv.adapter = speakerAdapter

        binding.back.setOnClickListener {
////            voiceChatRoomExit()
//            navController.popBackStack()
            checkOwnerExit()
        }

        binding.VoiceRoomMicBtn.setOnClickListener {
            if(!muteState){
                rtcEngine.muteLocalAudioStream(true)
                muteState = true
                myMicOff()
                binding.VoiceRoomMicBtn.background = resources.getDrawable(R.drawable.shape_un_selected_hand)
                binding.VoiceRoomMicBtn.setImageResource(R.drawable.ic_mic_off)
            }else{
                rtcEngine.muteLocalAudioStream(false)
                muteState = false
                myMicOn()
                binding.VoiceRoomMicBtn.background = resources.getDrawable(R.drawable.shape_selected_hand)
                binding.VoiceRoomMicBtn.setImageResource(R.drawable.ic_mic_on)
            }
        }
        binding.VoiceRoomAudienceRcv.adapter =  audienceAdapter

        binding.VoiceRoomExitBtn.setOnClickListener { checkOwnerExit() }

        binding.VoiceRoomHandBtn.setOnClickListener { clickHandBtn() }

        viewModel.audiences.observe(viewLifecycleOwner, Observer {
            audienceAdapter.map = viewModel.audienceListMap

            audienceAdapter.list = it

            audienceAdapter.notifyDataSetChanged()

        })
        viewModel.speakers.observe(viewLifecycleOwner, Observer {
            speakerAdapter.map = viewModel.speakerListMap

            speakerAdapter.list= it
            speakerAdapter.notifyDataSetChanged()
        })
        viewModel.isDelete.observe(viewLifecycleOwner, Observer {
            if (it) {
                countDownTimer = createCountDownTimer(2000L)
                countDownTimer?.start()
                context?.let {
                    SinglePositiveButtonDialog(it)
                            .setMessage("해당 방송이 종료되었습니다!")
                            .setPositiveButton("나가기") {
                                if(countDownTimer!=null){
                                    (countDownTimer as CountDownTimer).cancel()
                                    ( countDownTimer as CountDownTimer)
                                    navController.popBackStack()
                                }
//                                navController.popBackStack()
//                                voiceChatRoomExit()
                            }
                            .show()
                }
            }
        })

        viewModel.myVoiceUser.observe(viewLifecycleOwner, Observer {
            setStartService()
            if (it.role.equals("owner")) {
                if(viewModel.requestSpeakerListener==null){
                    viewModel.setRequestSpeakerSnapShotListener(voiceChatRoom.documentID)
                }
                binding.VoiceRoomMicBtn.isClickable = true
                myMicOn()
            } else if (it.role.equals("audience")) {
                rtcEngine.muteLocalAudioStream(true)
                binding.VoiceRoomMicBtn.background = resources.getDrawable(R.drawable.shape_un_selected_hand)
                binding.VoiceRoomMicBtn.isClickable = false
                binding.VoiceRoomMicBtn.setImageResource(R.drawable.ic_mic_off)
                myMicOff()
                if(initCount!=0){
                    context?.let { it1 -> SinglePositiveButtonDialog(it1)
                            .setMessage("청중으로 역할이 변경되었습니다!")
                            .setPositiveButton("확인"){

                            }.show()
                    }
                }else{
                    initCount++
                }
            } else {
                rtcEngine.muteLocalAudioStream(false)
                binding.VoiceRoomMicBtn.background = resources.getDrawable(R.drawable.shape_selected_hand)
                binding.VoiceRoomMicBtn.isClickable = true
                binding.VoiceRoomMicBtn.setImageResource(R.drawable.ic_mic_on)
//                myMicOn()
                if(initCount!=0){

                    context?.let { it1 -> SinglePositiveButtonDialog(it1)
                            .setMessage("스피커로 역할이 변경되었습니다!")
                            .setPositiveButton("확인"){

                            }.show()
                    }
                }else{
                    initCount++
                }

            }


        })

        viewModel.requestUsers.observe(viewLifecycleOwner, Observer {
            if(it.size==0){
                binding.VoiceRoomHandCount.isVisible = false
            }else{
                binding.VoiceRoomHandCount.isVisible = true
                binding.VoiceRoomHandCount.text = it.size.toString()
            }

        })

        setAdapterClickListener(speakerAdapter)
        setAdapterClickListener(audienceAdapter)
        viewModel.checkVoiceRoom()

//        myActivity.isResume.observe(viewLifecycleOwner, Observer {
//            if(it){
//                if(initCount!=1){
//                    setVoiceRoomListener()
//                    myActivity.isResume.value = false
//                    initCount=2
//                }
//
//            }
//        })
        return binding.root
    }

    fun setVoiceRoomListener(){
        viewModel.setSnapShotListener(voiceChatRoom.documentID)

    }

    fun currentServiceList(){
        var am : ActivityManager = activity?.applicationContext?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var rs : List<ActivityManager.RunningServiceInfo> = am.getRunningServices(1000)

        for(i in 0..rs.size-1){
            var rsi : ActivityManager.RunningServiceInfo = rs.get(i)
            println(rsi.service.packageName)
            println(rsi.service.className)

        }
    }
    fun myMicOn(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.talking.add(User.getInstance().phoneNumber)
            speakerAdapter.talking.add(User.getInstance().phoneNumber)
            var index = speakerAdapter.list.indexOf(User.getInstance().phoneNumber)
            speakerAdapter.notifyItemChanged(index)

        }
    }
    fun myMicOff(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.talking.remove(User.getInstance().phoneNumber)
            speakerAdapter.talking.remove(User.getInstance().phoneNumber)
            var index = speakerAdapter.list.indexOf(User.getInstance().phoneNumber)
            speakerAdapter.notifyItemChanged(index)
        }
    }
    fun setAdapterClickListener(adapter: NewVoiceRoomAdapter){
       adapter.setOnItemClickListener(object : OnItemClickListener{
           override fun onItemClick(v: View, position: Int) {
               var voiceUser = adapter.map.get(adapter.list[position])!!
               val option : VoiceRoomUserProfileBottomSheet = VoiceRoomUserProfileBottomSheet(adapter.map.get(adapter.list[position])!!,voiceChatRoom,viewModel.myVoiceUser.value!!) {
                   when(it){
                       0 -> {

                       }
                       1 -> {
                                // 프로필 화면 이동
                           CoroutineScope(Dispatchers.Main).launch {
                               var user = viewModel.getUserInfo(voiceUser)
                               var bundle = Bundle()
                               bundle.putParcelable("user",user)
                               navController.navigate(R.id.action_voiceRoomFragment_to_userProfileFragment,bundle)
                           }


                       }
                   }

               }
               option.show(activity?.supportFragmentManager!!,"bottomsheet")
           }

       })
    }
    private fun setStartService(){
        var intent = Intent(activity?.baseContext,ForecdTerminationService::class.java)
        intent.putExtra("documentID",voiceChatRoom.documentID)
        intent.putExtra("voiceUser",viewModel.myVoiceUser.value!!)
        activity?.startService(intent)
    }

    private fun createCountDownTimer(initialMililis: Long) =
            object : CountDownTimer(initialMililis, 20 * 100L){
                override fun onFinish() {
                    countDownTimer=null
                    navController.popBackStack()
                }

                override fun onTick(remainTime: Long) {

                }

            }

    fun clickHandBtn(){
        if(viewModel.myVoiceUser.value?.role.equals("owner")){
            val option : VoiceRoomRequestUserFragment = VoiceRoomRequestUserFragment(voiceChatRoom.documentID) {
                when(it){
                    0 -> {

                    }
                    1 -> {

                    }
                }

            }
            option.show(activity?.supportFragmentManager!!,"bottomsheet")
        }else if(viewModel.myVoiceUser.value?.role.equals("audience")){
            viewModel.requestSpeakerUser(voiceChatRoom.documentID)
            Toast.makeText(activity?.applicationContext, "발언권을 요청하였습니다", Toast.LENGTH_SHORT).show()
            isRequest = true
        }
    }
    fun checkOwnerExit(){
        if(viewModel.myVoiceUser.value?.role.equals("owner")){
            context?.let {
                AptCertificationImageAlertDialog(it)
                        .setMessage("해당 방송을 종료하시겠습니까?")
                        .setPositiveButton("예"){
//                            voiceChatRoomExit()
                            navController.popBackStack()
                        }.setNegativeButton {  }.show()
            }
        }else{
//            voiceChatRoomExit()
            navController.popBackStack()
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

                    viewModel.deleteVoiceChatRoom(voiceChatRoom.documentID)
                }
                if(viewModel.myVoiceUser.value?.role.equals("speaker")){
                    viewModel.changeSpeakersCount(voiceChatRoom.documentID, -1)
                }
                if(isRequest==true){
                    viewModel.deleteRequestUser(voiceChatRoom.documentID, User.getInstance().uid)
                }
            isClose = true
                navController.popBackStack(R.id.voiceMainFragment, false)


            }

    }
    private fun setView(voiceChatRoom: VoiceChatRoom){
        binding.VoiceRoomTitle.text = voiceChatRoom.name

        binding.VoiceRoomTopic.text = voiceChatRoom.topic

        binding.VoiceRoomType.text = voiceChatRoom.range

                // 나중에 type 과 range 이름 변경하자..
    }

    private fun initRtcEngine(){
        try {
            rtcEngine = RtcEngine.create(
                    activity?.baseContext,
                    getString(R.string.agora_app_id),
                    mRtcEventHandler
            )

//            optional()
        } catch (e: Exception) {
            Log.e("TAG", Log.getStackTraceString(e))
            println("error 나오네 initRtcEngine")
            throw RuntimeException(
                    "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                            e
                    )
            )
        }
//        setChannelProfile()
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
            override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
//                super.onRemoteAudioStateChanged(uid, state, reason, elapsed)
                when(state){
                    1 ->{


                    }
                    2->{
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.talking.add("0${uid}")
                            speakerAdapter.talking.add("0${uid}")
                            var index = speakerAdapter.list.indexOf("0${uid}")
                            speakerAdapter.notifyItemChanged(index)
//                            speakerAdapter.notifyDataSetChanged()
                        }

                    }0->{
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.talking.remove("0${uid}")
                        speakerAdapter.talking.remove("0${uid}")
                        var index = speakerAdapter.list.indexOf("0${uid}")
                        speakerAdapter.notifyItemChanged(index)
                    }
                    }
                    3->{

                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.talking.remove("0${uid}")
                            speakerAdapter.talking.remove("0${uid}")
                            var index = speakerAdapter.list.indexOf("0${uid}")
                        speakerAdapter.notifyItemChanged(index)
//                            speakerAdapter.notifyDataSetChanged()
                        }
                    }
                    4->{

                    }

                }
            }
        }
    }


    override fun onStop() {
        CurrentVoice.getInstance().boolean = false
//        viewModel.deleteSnapShot()
//        viewModel.clearViewModel()
        super.onStop()
    }

    override fun onDestroy() {

        viewModel.deleteSnapShot()
        CoroutineScope(Dispatchers.Main).launch {

//            // 방 나갈때 방을 나가시겠습니까? 라고 물어봐야하지않을까??
//            viewModel.deleteVoiceUser(voiceChatRoom?.documentID!!, User.getInstance().uid)
//            viewModel.changeAudienceCount(voiceChatRoom?.documentID)
//            rtcEngine.leaveChannel()
//            viewModel.deleteSnapShot()
//            if(voiceChatRoom.owner.equals(User.getInstance().uid)){
//                println("voiceChatRoomExit -> owner ")
//                viewModel.deleteVoiceChatRoom(voiceChatRoom.documentID)
//            }
//            if(viewModel.myVoiceUser.value?.role.equals("speaker")){
//                println("여기 호출됨!!")
//                viewModel.changeSpeakersCount(voiceChatRoom.documentID, -1)
//            }
//            if(isRequest==true){
//                viewModel.deleteRequestUser(voiceChatRoom.documentID, User.getInstance().uid)
//            }
//            isClose = true
//            navController.popBackStack(R.id.voiceMainFragment, false)
            voiceChatRoomExit()


        }


        super.onDestroy()


    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
//            voiceChatRoomExit()
    navController.popBackStack()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
    }

    override fun onDetach() {


        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}

