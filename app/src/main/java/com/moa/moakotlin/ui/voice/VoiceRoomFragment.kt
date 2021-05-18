package com.moa.moakotlin.ui.voice

import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine


class VoiceRoomFragment : Fragment() ,AGEventHandler{

    private lateinit var viewModel: VoiceRoomViewModel

    private lateinit var binding: VoiceRoomFragmentBinding

    private lateinit var navController: NavController

    private lateinit var rtcEngine: RtcEngine

    private lateinit var mRtcEventHandler :IRtcEngineEventHandler

    private val ACTION_WORKER_CONFIG_ENGINE = 0X2012

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

        binding = DataBindingUtil.inflate(inflater, R.layout.voice_room_fragment,container,false)
        viewModel = ViewModelProvider(this).get(VoiceRoomViewModel::class.java)
        binding.model = viewModel

        var token = arguments?.get("token") as String

        navController= findNavController()
       createIRtcEnginHandler()
        initRtcEngine()
        // Sets the channel profile of the Agora RtcEngine.
        // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        rtcEngine.setEnableSpeakerphone(true)
        rtcEngine.muteLocalVideoStream(true)
        rtcEngine.joinChannel(token, "test","Extra Optional Data", User.getInstance().phoneNumber.toInt())
        rtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)

        rtcEngine.muteLocalVideoStream(true)
        var handler = Handler()
        var message = Message()

        handler.sendMessage(message)
        return binding.root
    }


    private fun setToast(msg : String){
        Toast.makeText(activity?.baseContext!!,msg , Toast.LENGTH_SHORT).show()
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
            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
              println("성공!!!")
          }
          override fun onUserJoined(uid: Int, elapsed: Int) {
              super.onUserJoined(uid, elapsed)
              println("join~~~~~")
          }

          override fun onUserOffline(uid: Int, reason: Int) {
              super.onUserOffline(uid, reason)
              println("offline")
          }

          override fun onError(err: Int) {
              super.onError(err)
              println("error")
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
}

