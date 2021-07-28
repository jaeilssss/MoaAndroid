package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.user.UserRepository


class DropOutQuestionViewModel : ViewModel() {


    var modifyCheck = MutableLiveData<Boolean>(false)
    var emptyDataCheck = MutableLiveData<Boolean>(false)
    var errorCheck = MutableLiveData<Boolean>(false)
    var privateCheck = MutableLiveData<Boolean>(false)
    var alreadUseCheck  = MutableLiveData<Boolean>(false)
    var noMannerCheck = MutableLiveData<Boolean>(false)
    var etcCheck = MutableLiveData<Boolean>(false)

    var checked=MutableLiveData<String>("")

  suspend fun dropOut(): Boolean{
        var userRepository = UserRepository()
        var helperRepository = HelperRepository()
        var neederRepository = NeederRepository()
        if(userRepository.dropOutUser()){

            return true
        }else{
            return false
        }
    }

}