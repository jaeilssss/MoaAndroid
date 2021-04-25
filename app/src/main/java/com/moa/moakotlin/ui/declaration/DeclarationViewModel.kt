package com.moa.moakotlin.ui.declaration

import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Report
import com.moa.moakotlin.data.User
import kotlinx.coroutines.tasks.await

class DeclarationViewModel(navController: NavController) :BaseViewModel(navController){

    var checkBoxList = arrayOfNulls<ObservableField<Boolean>>(4)

    var reason = ObservableField<String>("")

    fun initCheckBoxList(){
        for(i in 0..3){
            checkBoxList[i] = ObservableField(false)
        }
    }
    suspend fun submit() : Boolean{

        return try{
            var report = Report()
            report.detailReason = reason.get()!!

            report.uid = User.getInstance().uid
            var result =false
            var db = FirebaseFirestore.getInstance()
            db.collection("Report")
                .add(report).addOnSuccessListener {
                    result = true
                }.addOnFailureListener{
                   result =  false
                }.await()
            result
        }catch(e: FirebaseException){
            false
        }

    }


    fun check(index : Int){
        for(i in 0..3){
            if(index ==i){
                checkBoxList[i]?.set(true)
            }else{
                checkBoxList[i]?.set(false)
            }
        }
    }

}