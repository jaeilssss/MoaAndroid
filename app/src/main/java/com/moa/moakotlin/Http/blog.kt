package com.moa.moakotlin

import kotlinx.coroutines.*





    fun main(){

      coroutine()
    }

fun coroutine(){
    var testHttp = testHttp()
    CoroutineScope(Dispatchers.Main).launch {



        var result =CoroutineScope(Dispatchers.Default).async {

            testHttp.send()

        }.await()

        println("결과 : ${result}")
    }
}
