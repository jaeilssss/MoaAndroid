package com.moa.moakotlin.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

var auth = FirebaseAuth.getInstance();
var TAG = "firebase sendMessage"




