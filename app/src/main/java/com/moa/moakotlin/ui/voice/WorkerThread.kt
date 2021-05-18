package com.moa.moakotlin.ui.voice

import android.os.Handler
import android.os.Message


class WorkerThread : Thread() {

    companion object{
        private val ACTION_WORKER_THREAD_QUIT = 0X1010 // quit this thread


        private val ACTION_WORKER_JOIN_CHANNEL = 0X2010

        private val ACTION_WORKER_LEAVE_CHANNEL = 0X2011

        private val ACTION_WORKER_CONFIG_ENGINE = 0X2012
    }


      class WorkerThreadHandler(var mWorkerThread: WorkerThread) : Handler(){
        override fun handleMessage(msg: Message) {
            if(this.mWorkerThread ==null){
                return;
            }
            when(msg.what){
                ACTION_WORKER_THREAD_QUIT ->{

                }
                ACTION_WORKER_JOIN_CHANNEL->{
                    var data  = arrayOf(msg.obj)

                }
                ACTION_WORKER_LEAVE_CHANNEL->{

                }
                ACTION_WORKER_CONFIG_ENGINE ->{
                    val configData = msg.obj as Array<Any>
                    mWorkerThread!!.configEngine(configData[0] as Int)
                }
            }
        }
    }

    private val mWorkerHandler: WorkerThreadHandler? = null
    private fun configEngine(cRole : Int){
        if(Thread.currentThread() !=this){
            var message = Message()
            message.what = ACTION_WORKER_CONFIG_ENGINE
            message.obj = arrayOf<Any>(cRole)
            mWorkerHandler?.sendMessage(message)

        }
    }
}