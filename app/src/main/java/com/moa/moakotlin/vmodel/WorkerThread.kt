package com.moa.moakotlin.vmodel

import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import java.io.File
import java.util.logging.Logger

class WorkerThread(private val mContext: Context) : Thread() {

    private class WorkerThreadHandler internal constructor(private var mWorkerThread: WorkerThread?) :
        Handler() {
        fun release() {
            mWorkerThread = null
        }

        override fun handleMessage(msg: Message) {
            if (mWorkerThread == null) {

                return
            }
            when (msg.what) {
                ACTION_WORKER_THREAD_QUIT -> mWorkerThread!!.exit()
                ACTION_WORKER_JOIN_CHANNEL -> {
                    val data = msg.obj as Array<String>
                    mWorkerThread!!.joinChannel(data[0], msg.arg1)
                }
                ACTION_WORKER_LEAVE_CHANNEL -> {
                    val channel = msg.obj as String
                    mWorkerThread!!.leaveChannel(channel)
                }
                ACTION_WORKER_CONFIG_ENGINE -> {
                    val configData = msg.obj as Array<Any>
                    mWorkerThread!!.configEngine(configData[0] as Int)
                }
            }
        }

    }

    private var mWorkerHandler: WorkerThreadHandler? = null
    private var mReady = false
    fun waitForReady() {
        while (!mReady) {
            try {
                sleep(20)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    override fun run() {

        Looper.prepare()
        mWorkerHandler = WorkerThreadHandler(this)
        ensureRtcEngineReadyLock()
        mReady = true

        // enter thread looper
        Looper.loop()
    }

    var rtcEngine: RtcEngine? = null
        private set

    fun joinChannel(channel: String, uid: Int) {
        if (currentThread() !== this) {

            val envelop = Message()
            envelop.what = ACTION_WORKER_JOIN_CHANNEL
            envelop.obj = arrayOf(channel)
            envelop.arg1 = uid
            mWorkerHandler!!.sendMessage(envelop)
            return
        }
        ensureRtcEngineReadyLock()
        rtcEngine!!.joinChannel(null, channel, "OpenVCall", uid)
        engineConfig.mChannel = channel
    }

    fun leaveChannel(channel: String) {
        if (currentThread() !== this) {

            val envelop = Message()
            envelop.what = ACTION_WORKER_LEAVE_CHANNEL
            envelop.obj = channel
            mWorkerHandler!!.sendMessage(envelop)
            return
        }
        if (rtcEngine != null) {
            rtcEngine!!.leaveChannel()
        }
        engineConfig.reset()

    }

    val engineConfig: EngineConfig

    private val mEngineEventHandler: MyEngineEventHandler
    fun configEngine(cRole: Int) {
        if (currentThread() !== this) {

            val envelop = Message()
            envelop.what = ACTION_WORKER_CONFIG_ENGINE
            envelop.obj = arrayOf<Any>(cRole)
            mWorkerHandler!!.sendMessage(envelop)
            return
        }
        ensureRtcEngineReadyLock()
        engineConfig.mClientRole = cRole
        rtcEngine!!.setClientRole(cRole)

    }

    private fun ensureRtcEngineReadyLock(): RtcEngine? {
        if (rtcEngine == null) {
            val appId = mContext.getString(R.string.private_app_id)
            if (TextUtils.isEmpty(appId)) {
                throw RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/")
            }
            try {
                rtcEngine = RtcEngine.create(mContext, appId, mEngineEventHandler.mRtcEventHandler)
            } catch (e: Exception) {

                throw RuntimeException(
                    "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                        e
                    )
                )
            }

            // Sets the channel profile of the Agora RtcEngine.
            // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            rtcEngine.enableAudioVolumeIndication(200, 3, false) // 200 ms
            rtcEngine.setLogFile(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + mContext.packageName + "/log/agora-rtc.log"
            )
        }
        return rtcEngine
    }

    fun eventHandler(): MyEngineEventHandler {
        return mEngineEventHandler
    }

    /**
     * call this method to exit
     * should ONLY call this method when this thread is running
     */
    fun exit() {
        if (currentThread() !== this) {

            mWorkerHandler!!.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT)
            return
        }
        mReady = false

        // TODO should remove all pending(read) messages


        // exit thread looper
        Looper.myLooper()!!.quit()
        mWorkerHandler!!.release()

    }

    companion object {

        private const val ACTION_WORKER_THREAD_QUIT = 0X1010 // quit this thread
        private const val ACTION_WORKER_JOIN_CHANNEL = 0X2010
        private const val ACTION_WORKER_LEAVE_CHANNEL = 0X2011
        private const val ACTION_WORKER_CONFIG_ENGINE = 0X2012
        fun getDeviceID(context: Context): String {
            // XXX according to the API docs, this value may change after factory reset
            // use Android id as device id
            return Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }
    }

    init {
        engineConfig = EngineConfig()
        val pref = PreferenceManager.getDefaultSharedPreferences(mContext)
        engineConfig.mUid = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_UID, 0)
        mEngineEventHandler = MyEngineEventHandler(mContext, engineConfig)
    }
}