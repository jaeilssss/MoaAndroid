package com.moa.moakotlin.firebase

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class phoneCertificationApi(var context : Context,var activity: FragmentActivity,var phoneNumber : String) {
    lateinit var storedVerificationId : String
    var auth = FirebaseAuth.getInstance();
 var result = "fail"
    lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    var TAG = "firebase sendMessage"
    lateinit var code : String
   var callbacks =
    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            System.out.println("onVerificationCompleted")
             code = credential.smsCode
            
//            if(code!=null){
//                Log.d(TAG, "onVerificationCompleted:$credential")
//                signInWithPhoneAuthCredential(credential)
//            }
        }
        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }
            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
            // ...
        }
    }
    private fun signInWithPhoneAuthCredential2(credential: PhoneAuthCredential) {
       auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(com.moa.moakotlin.firebase.TAG, "signInWithCredential:success")

                        val user = task.result?.user
                        result = "ok"
                        // ...
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(com.moa.moakotlin.firebase.TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                }
    }

    fun sendMessage(){
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    fun verify(code : String) :  String{
//        CoroutineScope(Dispatchers.Main).launch {
//            CoroutineScope(Dispatchers.Default).async {
//                var credential : PhoneAuthCredential
//                credential  = PhoneAuthProvider.getCredential(storedVerificationId,code)
//                signInWithPhoneAuthCredential(credential)
//            }.await()
//        }
        CoroutineScope(Dispatchers.Main).launch {
//            runBlocking {
//                var credential  = PhoneAuthProvider.getCredential(storedVerificationId,code)
//                 result = signInWithPhoneAuthCredential(credential)
//            }
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                var credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
                result = signInWithPhoneAuthCredential(credential)
            }
        }
        return result


    }

   suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential):String{
            return try{
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(com.moa.moakotlin.firebase.TAG, "signInWithCredential:success")
                            println("success!!!!!!@@@")
                            val user = task.result?.user

                            result = "ok"
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(com.moa.moakotlin.firebase.TAG, "signInWithCredential:failure", task.exception)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            result = "false"
                        }
                    }.await()
                        "ok"
            }catch (e: FirebaseException){
                    "error"
            }
    }
}
