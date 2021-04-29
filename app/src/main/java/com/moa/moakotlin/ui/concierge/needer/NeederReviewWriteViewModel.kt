package com.moa.moakotlin.ui.concierge.needer

import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.data.User
import com.google.firebase.Timestamp
import com.moa.moakotlin.R
import com.moa.moakotlin.base.Transfer


class NeederReviewWriteViewModel(navController: NavController) : BaseViewModel(navController) {
    var content = ObservableField<String>("")

    // 굉장히 비효율적인 방식인거같다 .... 나중에 효율적인 방식으록 고치자
    lateinit var transfer : Transfer

    fun submit(opponentUid :String){
        var db = FirebaseFirestore.getInstance()
        var review = content.get()?.let { Review(it,Timestamp.now(),User.getInstance().uid) }

        if (review != null) {
            db.collection("User").document(opponentUid)
                .collection("Review")
                .add(review)
                .addOnSuccessListener {
                    transfer.showToast("리뷰작성이 완료되었습니댜")
                    navController.popBackStack(R.id.kidReadFragment,false)
                }
        }
    }
}