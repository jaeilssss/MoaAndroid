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
     var code2 : String? =null
     var storedVerificationId : String ? =null
    var TAG = "firebase sendMessage"
    fun sendMessage(phoneNumber : String){
        auth.setLanguageCode("kr")
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

                code2 = credential.smsCode.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    signInWithPhoneAuthCredential(credential)
                }

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
            }
            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                // ...
            }
        }
   suspend fun signInWithPhoneAuthCredential(code: String) : Boolean{
       if(storedVerificationId==null){
           return false;
       }else{

        isChecked=false
       var credential : PhoneAuthCredential

       credential  = PhoneAuthProvider.getCredential(storedVerificationId!!,code)
       return try{

               auth.signInWithCredential(credential)
                       .addOnSuccessListener {
                           isChecked=true

                       }.
                       addOnFailureListener {
                           isChecked = code2!=null && code2.equals(code)
                       }.await()
            return   isChecked
           

       }catch (e :FirebaseException){
           isChecked
       }
       }
    }
    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }

}
private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    val user = task.result?.user

                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
}


