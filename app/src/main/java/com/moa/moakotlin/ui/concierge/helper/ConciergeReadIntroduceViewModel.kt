package com.moa.moakotlin.ui.concierge.helper

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class ConciergeReadIntroduceViewModel : ViewModel() {
   var content = ObservableField<String>("ㅇㅇ")
    var hopeWage = ObservableField<String>("")
    var aptName = ObservableField<String>("")
}