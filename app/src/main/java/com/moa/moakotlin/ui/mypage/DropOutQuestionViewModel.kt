package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.data.DropOut
import com.moa.moakotlin.data.User
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
    var delete = MutableLiveData<Boolean>(false)
  suspend fun dropOut(): Boolean{
        var userRepository = UserRepository()
        var helperRepository = HelperRepository()
        var neederRepository = NeederRepository()
      helperRepository.deleteGetMyHelper()
      neederRepository.deleteGetMyNeeder()
      userRepository.deleteAtPath("User/${User.getInstance().uid}/ChattingRoom")
      userRepository.deleteAtPath("User/${User.getInstance().uid}/Notification")
      userRepository.deleteAtPath("User/${User.getInstance().uid}")
      var dropOut = DropOut(User.getInstance().address,User.getInstance().birthday,User.getInstance().isMan,
      checked.value!!, Timestamp.now())
      userRepository.writeDropOutReason(dropOut)
//      delete.value =userRepository.dropOutUser()
      delete.value = true
      return delete.value!!
    }


    fun setCheckedMessage(){
        if(alreadUseCheck.value!!){
            checked.value = "유사한 다른 타 어플을 사용 중이라서"
        }else if(emptyDataCheck.value!!){
            checked.value ="나에게 필요한 정보가 부족해서"
        }else if(errorCheck.value!!){
            checked.value = "시스템 오류가 많아서"
        }else if(etcCheck.value!!){
            checked.value = "기타"
        }else if(modifyCheck.value!!){
            checked.value="아이디 변경 또는 재가입을 위해서"
        }else if(noMannerCheck.value!!){
            checked.value = "비매너 이웃을 만나서"
        }else if(privateCheck.value!!){
            checked.value="개인정보 유출 방지 등 보안 상의 문제로"
        }else {
            checked.value=""
        }
    }


}