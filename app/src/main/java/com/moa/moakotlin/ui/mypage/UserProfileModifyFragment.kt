package com.moa.moakotlin.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.UserProfileModifyFragmentBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class UserProfileModifyFragment : Fragment() {

    private lateinit var viewModel: UserProfileModifyViewModel

    private lateinit var binding: UserProfileModifyFragmentBinding


    private lateinit var navController: NavController

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(UserProfileModifyViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_modify_fragment, container, false)

        return binding.root
    }

}