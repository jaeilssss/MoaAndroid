package com.moa.moakotlin.ui.signup

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User

class SignUpInfoViewModel() : ViewModel(){
    var name = ObservableField<String>("")
    var birthDay = ObservableField<String>("")
    var gender = ObservableField<String>("")
    lateinit var bundle : Bundle
    var nickName = ObservableField<String>("")
    lateinit var db : FirebaseFirestore
    var nickCheck = false
    fun next(){
        if(name.get()?.length!! >0 && birthDay.get()?.length!! >0 && nickName.get()?.length!! >0&&
            gender.get()?.length!!>0){
            if(nickCheck==false){
//                Toast.makeText(context,"별칭 중복 확인을 해주세요",Toast.LENGTH_SHORT).show()
                return
            }else {
                var user = User.getInstance()
                user.name = name.get()!!
                user.nickName = nickName.get()!!

                user.birthday = birthDay.get()!!
                if (gender.get().equals("1") || gender.get().equals("3")) {
                    user.isMan = true
                } else if (gender.get().equals("2") || gender.get().equals("4")) {
                    user.isMan = false
                } else {
//                    Toast.makeText(context, "주민등록 뒤 첫자리 재대로 입력해주세요", Toast.LENGTH_SHORT).show()
                }
//                navController.navigate(R.id.action_signUpInfoFragment_to_aptInfoFragment)
            }
        }else{
//            Toast.makeText(context,"정보를 입력해주세요!",Toast.LENGTH_SHORT).show()
        }
    }
    fun nickNameCheck(){
        db = FirebaseFirestore.getInstance()
        db.collection("User").whereEqualTo("nickName",nickName.get())
            .get()
            .addOnSuccessListener {
            if(it.isEmpty){
//                Toast.makeText(context,"가능한 별칭 입니다!",Toast.LENGTH_SHORT).show()
                nickCheck = true
            }else{
//                Toast.makeText(context,"이미 사용중인 별칭 입니다!",Toast.LENGTH_SHORT).show()
                nickCheck = false
            }
            }.addOnFailureListener{

            }
    }
}