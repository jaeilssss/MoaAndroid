package com.moa.moakotlin.base

import android.view.Window
import android.widget.ScrollView

abstract class BaseScrollFragment : BaseFragment(){
    private  var keyboardVisibilityUtils: KeyboardVisibilityUtils ?=null
     fun keyboardVisibility(window:Window , scrollView: ScrollView) {
        keyboardVisibilityUtils =
            KeyboardVisibilityUtils(window,
                    onShowKeyboard = { keyboardHeight ->
                        scrollView.run {
                            smoothScrollTo(scrollX, scrollY + keyboardHeight)
                        }
                    })
    }
    fun closeKeyboardVisibility(){
        keyboardVisibilityUtils = null
    }
}