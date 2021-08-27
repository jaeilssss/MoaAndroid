package com.moa.moakotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.moa.moakotlin.base.ConnectionStateMonitor
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.push.FcmService
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity(){
lateinit var model : LoadingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        model = ViewModelProvider(this).get(LoadingViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
        }
    }
  suspend fun startLoading() {
        try{
            Picture.deleteInstance()
            var userRepository = UserRepository()
            if(FirebaseAuth.getInstance().currentUser==null){

                startActivity(Intent(this, MainActivity::class.java))

                finish()

            }else{
                var result = FirebaseAuth.getInstance().currentUser?.let { model.initApp(it.uid,this) }
                if(result == true){

                    FirebaseMessaging.getInstance().subscribeToTopic("MOA")
                    FirebaseMessaging.getInstance().subscribeToTopic(User.getInstance().aptCode)

                    userRepository.registerPushToken()

                    if(intent.getStringExtra("request").equals("채팅")){
                        var intent = Intent(this,MainActivity::class.java)
                        intent.putExtra("request","채팅")
                        startActivity(intent)
                    }else if(intent.getStringExtra("request").equals("알림")){
                        var intent = Intent(this,MainActivity::class.java)
                        intent.putExtra("request","알림")
                        startActivity(intent)
                    }else if(intent.getStringExtra("request").equals("모아 라디오")){
                        var intent = Intent(this,MainActivity::class.java)
                        intent.putExtra("request","모아 라디오")
                        startActivity(intent)
                    } else{
                        var intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }

                    finish()

                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

        }catch (e :Exception) {
           FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    // startLoading Method..


    override fun onDestroy() {

        super.onDestroy()


    }
}// MainActiv