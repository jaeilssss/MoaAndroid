package com.moa.moakotlin.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class PolicyViewModel : ViewModel() {

    var checkBoxFirst = ObservableField<Boolean>(false)
    var checkBoxSecond = ObservableField<Boolean>(false)
    var checkBoxThird = ObservableField<Boolean>(false)
    var checkBoxFourth = ObservableField<Boolean>(false)
    var list = ArrayList<Boolean>()

    fun checkBoxFirst(){


    }

    fun checkBoxCheck() {


    }

    fun checkBox() : Boolean{
        return checkBoxFirst.get()==true &&
                checkBoxSecond.get()==true &&
                checkBoxThird.get()==true &&
                checkBoxFourth.get()==true
    }
    // TODO: Implement the ViewModel
}