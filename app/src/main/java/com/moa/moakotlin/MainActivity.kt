package com.moa.moakotlin

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.BottomNavController
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.base.onBackPressedListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.ActivityMainBinding
import com.moa.moakotlin.repository.push.FcmService
import com.moa.moakotlin.ui.concierge.ConciergeWriteActivity


class MainActivity : AppCompatActivity() ,Transfer,BottomNavController{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var navGraph : NavGraph
    lateinit var backListener : onBackPressedListener
    lateinit var model : MainViewModel

    var i =0
    companion object
    {
        val REQUEST_WRITE_CODE = 1000
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        model = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
         navController = findNavController(R.id.mainFragment)

        if(intent.getStringExtra("request").equals("채팅")){
            navGraph = navController.graph
            navGraph.startDestination = R.id.chattingRoomFragment
            navController.graph = navGraph
            binding.mainBottomNavigation.menu.get(1).isChecked = true
        }else if(intent.getStringExtra("request").equals("알림")){
            navGraph = navController.graph
            navGraph.startDestination = R.id.alarmFragment
            navController.graph = navGraph
            binding.mainBottomNavigation.menu.get(3).isChecked = true
        } else if(User.getInstance().uid.equals("")){
            navGraph = navController.graph
            navGraph.startDestination = R.id.firstViewFragment
            navController.graph = navGraph
        }else if(FirebaseAuth.getInstance().currentUser!=null){
            // 이미 로그인 된 유저는 start destination 바뀜
            init()
            navGraph = navController.graph
            navGraph.startDestination = R.id.HomeFragment
            navController.graph = navGraph
            var data = getSharedPreferences("MyLatestNotification", Context.MODE_PRIVATE)
            data.getString("documentID", "")?.let { model.setAlarmSnapShot(it)
            }
            model.setChattingRoomSnapShot()
        }

        binding.mainBottomNavigation.itemIconTintList = null

        model.isRead.observe(this, Observer {
            if (it) {
                binding.mainBottomNavigation.removeBadge(R.id.Alarm)
            } else {
                var badge = binding.mainBottomNavigation.getOrCreateBadge(R.id.Alarm)
                badge.backgroundColor = Color.parseColor("#ffe402")

            }
        })

        model.latestChatRoom.observe(this, Observer {
            var chattingRoomData = getSharedPreferences("MyLatestChattingRoomTimeStamp", Context.MODE_PRIVATE)
            if (it == chattingRoomData.getString("timeStamp", "")) {
                binding.mainBottomNavigation.removeBadge(R.id.chattingRoomFragment)
            } else {
                if (model.chattingRoom.value?.isRead == true) {
                    binding.mainBottomNavigation.removeBadge(R.id.chattingRoomFragment)
                } else {
                    var badge = binding.mainBottomNavigation.getOrCreateBadge(R.id.chattingRoomFragment)
                    badge.backgroundColor = Color.parseColor("#ffe402")
                }

            }
        })
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.writeSelectFragment -> {
//                    val bottomSheet = WriteSelectFragment()
//                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    var intent = Intent(this, ConciergeWriteActivity::class.java)
                    startActivityForResult(intent, REQUEST_WRITE_CODE)

                    return@setOnNavigationItemSelectedListener false
                }
                R.id.HomeFragment -> {
                    navController.navigate(R.id.HomeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.MyPageFragment -> {
//                    binding.mainBottomNavigation.menu.get(0).isChecked = true
                    navController.navigate(R.id.MyPageFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.chattingRoomFragment -> {
                    binding.mainBottomNavigation.removeBadge(R.id.chattingRoomFragment)
                    model.isChattingRoomRead.value = true
                    navController.navigate(R.id.chattingRoomFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Alarm -> {
                    binding.mainBottomNavigation.removeBadge(R.id.Alarm)
                    model.isRead.value = true
                    navController.navigate(R.id.alarmFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->return@setOnNavigationItemSelectedListener false
            }
        }

    }



fun init(){
getSharedPreferences("AlarmSetting", Context.MODE_PRIVATE)!!
        .edit {
            putBoolean("isChattingAlarm", User.getInstance().isAgreeChattingAlarm)
            putBoolean("isEventAlarm", User.getInstance().isAgreeEventAlarm)
            putBoolean("isMarketingAlarm", User.getInstance().isAgreeMarketing)
            commit()  }
}

    override fun bottomVisible() {
        binding.mainBottomNavigation.visibility= View.VISIBLE
    }

    override fun bottomGone() {
        binding.mainBottomNavigation.visibility= View.GONE
    }


        fun bottomNavigationGone(){
            binding.mainBottomNavigation.visibility= View.GONE
        }

    fun bottomNavigationVisible(){
        binding.mainBottomNavigation.visibility= View.VISIBLE
    }

    fun getMyaroundApt(db: FirebaseFirestore){
        db.collection("Apart").document(User.getInstance().aptCode)
                .get().addOnSuccessListener {
                    if(it.exists()){
                        var list = it.toObject(aptList::class.java)
                        if (list != null) {
                            println(list.aroundApt.get(0))
                            aptList.setInstance(list)

                        }
                    }
                }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        backListener.onBackPressed()
    }

    override fun setBottomControl(index: Int) {
        TODO("Not yet implemented")
    }

    override fun setClickableButton(index: Int) {
        TODO("Not yet implemented")
    }



    override fun onDestroy() {

        super.onDestroy()
    }



}