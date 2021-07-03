package com.moa.moakotlin.ui.voice

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class VoiceRoomMakeFragment : Fragment() {

    companion object {
        fun newInstance() = VoiceRoomMakeFragment()
    }

    private lateinit var viewModel: VoiceRoomMakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.voice_room_make_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VoiceRoomMakeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}