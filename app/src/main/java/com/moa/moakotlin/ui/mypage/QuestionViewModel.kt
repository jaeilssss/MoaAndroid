package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.AppQuestion
import com.moa.moakotlin.repository.mypage.MyPageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuestionViewModel : ViewModel() {

//
    var questionList = MutableLiveData<ArrayList<AppQuestion>>()

    init {
        var repository = MyPageRepository()

        CoroutineScope(Dispatchers.Main).launch {
            var list = repository.getAppQuestion()

            questionList.value = list
        }


    }
}