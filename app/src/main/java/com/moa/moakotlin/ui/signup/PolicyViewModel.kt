package com.moa.moakotlin.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class PolicyViewModel : ViewModel() {

    var checkBoxFirst = MutableLiveData<Boolean>(false)
    var checkBoxSecond = MutableLiveData<Boolean>(false)
    var checkBoxThird = MutableLiveData<Boolean>(false)
    var checkBoxFourth = MutableLiveData<Boolean>(false)
    var list = ArrayList<Boolean>()

    fun checkBoxFirst(){
        if(checkBoxFirst.value==true){
            checkBoxFirst.value = true
            checkBoxSecond.value = true
            checkBoxThird.value = true
            checkBoxFourth.value = true
        }else if(checkBoxFirst.value==false){
            checkBoxFirst.value = false
            checkBoxSecond.value = false
            checkBoxThird.value = false
            checkBoxFourth.value = false
        }
    }

    fun checkBoxCheck() {
        println("${checkBoxSecond.value}")
       if(checkBoxSecond.value==false){
            checkBoxFirst.value = false
        }else if(checkBoxThird.value==false){
            checkBoxFirst.value= false
        }else if(checkBoxFourth.value==false){
            checkBoxFirst.value =false
        }
            if(checkBoxSecond.value==true &&
                    checkBoxThird.value==true&&
                    checkBoxFourth.value==true){
                checkBoxFirst.value =true
            }
    }

    fun checkBox() : Boolean{
        return checkBoxSecond.value==true && checkBoxThird.value==true

    }
    // TODO: Implement the ViewModel
}