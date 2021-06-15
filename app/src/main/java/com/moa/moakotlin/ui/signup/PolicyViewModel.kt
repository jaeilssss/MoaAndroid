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
        if(checkBoxFirst.get()==true){
            checkBoxFirst.set(true)
            checkBoxSecond.set(true)
            checkBoxThird.set(true)
            checkBoxFourth.set(true)
        }else if(checkBoxFirst.get()==false){
            checkBoxFirst.set(false)
            checkBoxSecond.set(false)
            checkBoxThird.set(false)
            checkBoxFourth.set(false)
        }
    }

    fun checkBoxCheck() {
        if(checkBoxSecond.get()==false){
            checkBoxFirst.set(false)
        }else if(checkBoxThird.get()==false){
            checkBoxFirst.set(false)
        }else if(checkBoxFourth.get()==false){
            checkBoxFirst.set(false)
        }
            if(checkBoxSecond.get()==true &&
                    checkBoxThird.get()==true&&
                    checkBoxFourth.get()==true){
                checkBoxFirst.set(true)
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