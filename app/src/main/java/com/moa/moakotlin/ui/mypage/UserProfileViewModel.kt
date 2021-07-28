package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.review.ReviewRepository

class UserProfileViewModel : ViewModel() {


    var nickName = MutableLiveData<String>(User.getInstance().nickName)
    var aptName = MutableLiveData<String>(User.getInstance().aptName)
    var introduction = MutableLiveData<String>("")

    var reviewList = MutableLiveData<ArrayList<Review>>(ArrayList())


    suspend fun getReviewList(userUid : String){

        var repository = ReviewRepository()

        var list = repository.getReviewList(userUid)

        reviewList.value = list
    }
}