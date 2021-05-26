package com.moa.moakotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.push.FcmService
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
            var intent = Intent(this,FcmService::class.java)
            startService(intent)
            Picture.deleteInstance()
            if(FirebaseAuth.getInstance().currentUser==null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                var result = model.initApp(FirebaseAuth.getInstance().currentUser.uid,this)
                if(result){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

            }


        }catch (e :Exception) {

        }
    }// startLoading Method..
}// MainActiv