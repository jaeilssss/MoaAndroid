package com.moa.moakotlin.data

import com.google.firebase.Timestamp


class Chat {
    lateinit var talk : String

    lateinit var uid : String

    lateinit var timeStamp: Timestamp


     var images : ArrayList<String>? = null



    companion object{
        // 자기변수 선언하기
        @Volatile private var instance : HashMap<String,ArrayList<Chat>> ? = null

        fun getInstance() : HashMap<String,ArrayList<Chat>> = instance ?: synchronized(this){
            instance ?: HashMap<String,ArrayList<Chat>>().also {
                instance = it
            }
        }

        fun size(roomId: String) : Int? {
            return instance?.get(roomId)?.size
        }
        fun setInstance(roomId : String , list : ArrayList<Chat>){
            instance?.put(roomId,list)
        }

        fun addInstance(roomId : String , list : ArrayList<Chat>){
            var temp = instance?.get(roomId)
            temp?.addAll(list)
            if (temp != null) {
                instance?.put(roomId,temp)
            }
        }

    }


}