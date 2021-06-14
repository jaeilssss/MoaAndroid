package com.moa.moakotlin.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.util.*

class PolicyViewModel : ViewModel() {

    var checkBoxFirst = ObservableField<Boolean>(false)
    var checkBoxSecond = ObservableField<Boolean>(false)
    var checkBoxThird = ObservableField<Boolean>(false)
    var checkBoxFourth = ObservableField<Boolean>(false)

    fun checkBoxFirst(){
        if(checkBoxFirst.get()==false){
            checkBoxFirst.set(true)
            checkBoxSecond.set(true)
            checkBoxThird.set(true)
            checkBoxFourth.set(true)
        }else{
            checkBoxFirst.set(false)
            checkBoxSecond.set(false)
            checkBoxThird.set(false)
            checkBoxFourth.set(false)
        }
    }

    fun checkBox() : Boolean{
        return checkBoxFirst.get()==true &&
                checkBoxSecond.get()==true &&
                checkBoxThird.get()==true &&
                checkBoxFourth.get()==true
    }
    // TODO: Implement the ViewModel
}