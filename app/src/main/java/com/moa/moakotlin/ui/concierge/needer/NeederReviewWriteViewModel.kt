package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.NeederRepository

class NeederReviewWriteViewModel : ViewModel() {

    var content  = MutableLiveData<String>("")
  suspend  fun reviewWrite(uid : String) : Boolean{
        var review = Review(content.value!! , Timestamp.now(), User.getInstance().uid)
         var repository = NeederRepository()

        return repository.writeReview(review,uid)
    }

    fun hireCompletion(needer : Needer){
        var repository = NeederRepository()
        repository.hireCompletion(needer)
    }
}