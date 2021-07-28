package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.PushMessage
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.push.FcmRepository

class NeederReviewWriteViewModel : ViewModel() {

    var content  = MutableLiveData<String>("")
  suspend  fun reviewWrite(uid : String) : Boolean{
        var review = Review(content.value!! , Timestamp.now(), User.getInstance().uid,uid)
         var repository = NeederRepository()

        return repository.writeReview(review,uid)
    }

    fun pushToken(user:User){
                var pushRepository = FcmRepository()
        var message = PushMessage("리뷰","${User.getInstance().nickName}님이 리뷰를 작성하셨습니다",user.pushToken)
        pushRepository.sendPushMessage(message)
    }

    fun hireCompletion(needer : Needer){
        var repository = NeederRepository()
        repository.hireCompletion(needer)
    }
}