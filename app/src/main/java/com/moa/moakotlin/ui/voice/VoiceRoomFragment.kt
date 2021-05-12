package com.moa.moakotlin.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding
import io.agora.rtc.RtcEngine
import io.agora.rtc.IRtcEngineEventHandler;

class VoiceRoomFragment : Fragment() ,AGEventHandler{

    private lateinit var viewModel: VoiceRoomViewModel

    private lateinit var binding: VoiceRoomFragmentBinding

    private lateinit var navController: NavController

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

        binding = DataBindingUtil.inflate(inflater,R.layout.voice_room_fragment,container,false)
        viewModel = ViewModelProvider(this).get(VoiceRoomViewModel::class.java)
        binding.model = viewModel

        var token = arguments?.get("token") as String

        navController= findNavController()
        worker().configEngine(cRole)
        val rtc: RtcEngine = worker().getRtcEngine()
        rtc.joinChannel(token, roomName, "good", config().mUid)
        setToast(token)

        return binding.root
    }


    private fun setToast(msg : String){
        Toast.makeText(activity?.applicationContext!!,msg , Toast.LENGTH_SHORT).show()
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
    protected fun worker(): WorkerThread {
        return (activity?.application as AGApplication).getWorkerThread()
    }
}