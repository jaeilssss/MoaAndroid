package com.moa.moakotlin.ui.voice

import android.app.Application
import com.moa.moakotlin.vmodel.CurrentUserSettings
import com.moa.moakotlin.vmodel.WorkerThread

class AGApplication : Application() {

    private var mWorkerThread: WorkerThread? = null

    @Synchronized
    fun initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = WorkerThread(applicationContext)
            mWorkerThread!!.start()
            mWorkerThread!!.waitForReady()
        }
    }

    @Synchronized
    fun getWorkerThread(): WorkerThread? {
        return mWorkerThread
    }

    @Synchronized
    fun deInitWorkerThread() {
        mWorkerThread?.exit()
        try {
            mWorkerThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        mWorkerThread = null
    }

    val mAudioSettings: CurrentUserSettings = CurrentUserSettings()
}
