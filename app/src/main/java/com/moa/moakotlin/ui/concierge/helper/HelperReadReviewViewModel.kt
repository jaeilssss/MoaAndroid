package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.repository.review.ReviewRepository

class HelperReadReviewViewModel : ViewModel() {

    var reviewList = MutableLiveData<ArrayList<Review>>()
    suspend fun getReviewList(helperUid : String){

        var repository = ReviewRepository()

        var list = repository.getReviewList(helperUid)

        reviewList.value = list
    }


}