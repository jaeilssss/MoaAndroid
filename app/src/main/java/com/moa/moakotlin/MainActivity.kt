package com.moa.moakotlin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.base.BottomNavController
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.base.onBackPressedListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.ActivityMainBinding
import com.moa.moakotlin.ui.bottomsheet.WriteSelectFragment
import com.moa.moakotlin.ui.concierge.ConciergeWriteActivity
import kotlinx.coroutines.CoroutineScope

class MainActivity : AppCompatActivity() ,Transfer,BottomNavController{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var navGraph : NavGraph
    lateinit var backListener : onBackPressedListener
    lateinit var model : MainViewModel

    companion object
    {
        val REQUEST_WRITE_CODE = 1000
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        model = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
         navController = findNavController(R.id.mainFragment)
        if(User.getInstance().uid.equals("")){
            navGraph = navController.graph
            navGraph.startDestination = R.id.firstViewFragment
            navController.graph = navGraph
        }else if(FirebaseAuth.getInstance().currentUser!=null){
            // 이미 로그인 된 유저는 start destination 바뀜
                init()
            Toast.makeText(applicationContext, "환영합니다 ${User.getInstance().nickName}님!",Toast.LENGTH_SHORT).show()
            navGraph = navController.graph
            navGraph.startDestination = R.id.HomeFragment
            navController.graph = navGraph
            var data = getSharedPreferences("MyLatestNotification",Context.MODE_PRIVATE)
            data.getString("documentID","")?.let { model.setAlarmSnapShot(it)
            }

            model.setChattingRoomSnapShot()
        }
        binding.mainBottomNavigation.itemIconTintList = null

        model.isRead.observe(this, Observer {
            if(it){
                binding.mainBottomNavigation.removeBadge(R.id.Alarm)
            }else{
                var badge = binding.mainBottomNavigation.getOrCreateBadge(R.id.Alarm)
                badge.backgroundColor = Color.parseColor("#ffe402")

            }
        })

        model.latestChatRoom.observe(this, Observer {
            var chattingRoomData = getSharedPreferences("MyLatestChattingRoomTimeStamp",Context.MODE_PRIVATE)
            if(it==chattingRoomData.getString("timeStamp","")){
                binding.mainBottomNavigation.removeBadge(R.id.chattingRoomFragment)
            }else{
                var badge = binding.mainBottomNavigation.getOrCreateBadge(R.id.chattingRoomFragment)
                badge.backgroundColor = Color.parseColor("#ffe402")
            }
        })
//        model.isChattingRoomRead.observe(this, Observer {
//            if(it){
//                binding.mainBottomNavigation.removeBadge(R.id.chattingRoomFragment)
//            }else{
//                var badge = binding.mainBottomNavigation.getOrCreateBadge(R.id.chattingRoomFragment)
//                badge.backgroundColor = Color.parseColor("#ffe402")
//            }
//        })
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.writeSelectFragment ->{
//                    val bottomSheet = WriteSelectFragment()
//                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    var intent = Intent(this,ConciergeWriteActivity::class.java)
                    startActivityForResult(intent,REQUEST_WRITE_CODE)
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.HomeFragment ->{
                    navController.navigate(R.id.HomeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.MyPageFragment->{
//                    binding.mainBottomNavigation.menu.get(0).isChecked = true
                    navController.navigate(R.id.MyPageFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.chattingRoomFragment->{
                    binding.mainBottomNavigation.removeBadge(R.id.chattingRoomFragment)
                    model.isChattingRoomRead.value = true
                    navController.navigate(R.id.chattingRoomFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Alarm ->{
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
            putBoolean("isChattingAlarm",User.getInstance().isAgreeChattingAlarm)
            putBoolean("isEventAlarm",User.getInstance().isAgreeEventAlarm)
            putBoolean("isMarketingAlarm",User.getInstance().isAgreeMarketing)
            commit()  }
}

    override fun bottomVisible() {
        binding.mainBottomNavigation.visibility= View.VISIBLE
    }

    override fun bottomGone() {
        binding.mainBottomNavigation.visibility= View.GONE    }


        fun bottomNavigationGone(){
            binding.mainBottomNavigation.visibility= View.GONE
        }

    fun bottomNavigationVisible(){
        binding.mainBottomNavigation.visibility= View.VISIBLE
    }

    fun getMyaroundApt(db : FirebaseFirestore){
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