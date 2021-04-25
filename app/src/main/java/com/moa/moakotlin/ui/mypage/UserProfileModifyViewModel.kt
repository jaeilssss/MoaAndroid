package com.moa.moakotlin.ui.mypage

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.mypage.MyPageRepository
import kotlinx.coroutines.*

class UserProfileModifyViewModel : ViewModel() {
 var introduction =ObservableField<String>("")
    var nickname =ObservableField<String>("")

    suspend fun submit() : Boolean {
        var result = false
        var repository = MyPageRepository()

            if(Picture.getInstance().size>0){
                CoroutineScope(Dispatchers.Default).async {
                 var path =repository.uploadImage(Picture.getInstance().get(0))
                    User.getInstance().profileImage = path
                }.await()
            }
            result = CoroutineScope(Dispatchers.Default).async {
                introduction.get()?.let { User.getInstance().profileImage?.let { it1 ->
                    repository.modifyProfile(it,nickname.get()!!,
                        it1
                    )
                } }
            }.await()!!
            return result
    }
}