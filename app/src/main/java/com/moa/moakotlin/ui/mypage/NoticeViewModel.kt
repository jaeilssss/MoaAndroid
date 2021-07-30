package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Notice
import com.moa.moakotlin.repository.mypage.MyPageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoticeViewModel : ViewModel() {

    var noticeList  = MutableLiveData<ArrayList<Notice>>()


    fun getNoticeList(){
        CoroutineScope(Dispatchers.Main).launch {
            var repository = MyPageRepository()

            var list = repository.getAppNotice()

            noticeList.value = list
        }

    }
}