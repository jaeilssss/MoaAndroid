package com.moa.moakotlin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class MyApp : Application(), LifecycleObserver {

    companion object{
    var isForeground =false
    }
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this) }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() { isForeground = false }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() { isForeground = true}
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onAppCreated() { System.out.println("check app ON_CREATE!!!!!!!!!!!!!!!!!") }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppResumed() { System.out.println("check app ON_RESUME!!!!!!!!!!!!!!!!!") }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroyed() { System.out.println("check app ON_DESTROY!!!!!!!!!!!!!!!!!") }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPaused() { System.out.println("check app ON_PAUSE!!!!!!!!!!!!!!!!!") }
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAppAny() { System.out.println("check app ON_ANY!!!!!!!!!!!!!!!!!") } }

