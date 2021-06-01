package com.moa.moakotlin.repository.login

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.base.Repository
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.firebase.auth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class LoginRepository(var activity: FragmentActivity){
    var isChecked = false
    lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    lateinit var code2 : String
     var storedVerificationId : String ? =null
    var TAG = "firebase sendMessage"
    fun sendMessage(phoneNumber : String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    var callbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                println("여기 들어왔니???")
                code2 = credential.smsCode.toString()
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
                println(resendToken)


                // ...
            }
        }
   suspend fun signInWithPhoneAuthCredential(code: String) : Boolean{
       if(storedVerificationId==null){
           return false;
       }else{


        isChecked=false
       var credential : PhoneAuthCredential

       credential  = PhoneAuthProvider.getCredential(storedVerificationId,code)
       return try{
           CoroutineScope(Dispatchers.Default).async {

           }
           auth.signInWithCredential(credential)
                   .addOnSuccessListener {
                       isChecked=true

                   }.
                   addOnFailureListener {
                       isChecked=false
                   }.await()
                        println(isChecked)
                    isChecked
       }catch (e :FirebaseException){
           isChecked
       }
       }
    }
    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }


    }


