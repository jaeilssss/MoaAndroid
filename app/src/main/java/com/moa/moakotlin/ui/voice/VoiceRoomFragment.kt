package com.moa.moakotlin.ui.voice

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.VoiceRoomFragmentBinding

class VoiceRoomFragment : Fragment() {

    private lateinit var viewModel: VoiceRoomViewModel


    private lateinit var binding: VoiceRoomFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.voice_room_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VoiceRoomViewModel::class.java)
        // TODO: Use the ViewModel
    }

}