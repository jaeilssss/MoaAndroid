package com.moa.moakotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.base.onBackPressedListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.ActivityMainBinding
import com.moa.moakotlin.ui.bottomsheet.WriteSelectFragment

class MainActivity : AppCompatActivity() ,Transfer{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var navGraph : NavGraph
    lateinit var backListener : onBackPressedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
         navController = findNavController(R.id.mainFragment)
        if(User.getInstance().uid.equals("")){
            println("@@@@@@@@@@@@@@@")
            navGraph = navController.graph
            navGraph.startDestination = R.id.firstViewFragment
            navController.graph = navGraph
        }else if(FirebaseAuth.getInstance().currentUser!=null){
            // 이미 로그인 된 유저는 start destination 바뀜
            Toast.makeText(applicationContext, "환영합니다 ${User.getInstance().nickName}님!",Toast.LENGTH_SHORT).show()
            navGraph = navController.graph
            navGraph.startDestination = R.id.HomeFragment
            navController.graph = navGraph
        }


        binding.mainBottomNavigation.itemIconTintList = null
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.writeSelectFragment ->{
                    val bottomSheet = WriteSelectFragment()
                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.HomeFragment ->{
                    navController.navigate(R.id.HomeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.MyPageFragment->{
                    navController.navigate(R.id.MyPageFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.chattingRoomFragment->{
                    navController.navigate(R.id.chattingRoomFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->return@setOnNavigationItemSelectedListener false
            }
        }
    }

    override fun bottomVisible() {
        binding.mainBottomNavigation.visibility= View.VISIBLE
    }

    override fun bottomGone() {
        binding.mainBottomNavigation.visibility= View.GONE    }

    override fun showToast(content: String) {
        Toast.makeText(applicationContext,content,Toast.LENGTH_SHORT).show()
    }


    fun getMyaroundApt(db : FirebaseFirestore){
        db.collection("Apart").document(User.getInstance().aptCode)
                .get().addOnSuccessListener {
                    if(it.exists()){
                        println("존재!!")
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
}