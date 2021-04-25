package com.moa.moakotlin.data

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import kotlin.properties.Delegates

class Picture (){
     var path =""

    var selectCount by Delegates.notNull<Int>()

    var position by Delegates.notNull<Int>()


    companion object{


        @Volatile private var instance : ArrayList<String>? = null

        fun getInstance() : ArrayList<String> = Picture.instance ?: synchronized(this){
            Picture.instance ?: ArrayList<String>().also {
                Picture.instance = it
            }
        }
        fun deleteInstance(){
            instance = null
        }
        fun isNull() : Boolean{
            return instance != null
        }

        fun setInstance(list: ArrayList<String>){
            instance = list
        }

        fun getGalleryPhotos( context : Context) : ArrayList<String>{
            var pictures = ArrayList<String>()

            var uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            var colums =
                arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
            val orderBy = MediaStore.Images.Media._ID

            var cursor = context.contentResolver.query(uri,colums,null,null,orderBy)

            if(cursor !=null && cursor.count>0){
                while(cursor.moveToNext()){
                    var picture = Picture()

                    var indexPath = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)

                    picture.path = cursor.getString(indexPath)

                    pictures.add(picture.path)
                }
            }else{
                Log.e("getGalleryPhotos","error")

            }
            pictures.reverse()
            return pictures
        }
    }

}