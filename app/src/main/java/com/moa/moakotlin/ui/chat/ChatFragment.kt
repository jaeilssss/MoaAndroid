package com.moa.moakotlin.ui.chat

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.rotationMatrix
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.data.*
import com.moa.moakotlin.databinding.FragmentChatBinding
import com.moa.moakotlin.recyclerview.chat.ChatAdapter
import kotlinx.android.synthetic.main.chatting_room_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : BaseFragment() {

    lateinit var binding: FragmentChatBinding
    var check =0
    lateinit var model: ChatViewModel
    lateinit var navController: NavController
    lateinit var db : ListenerRegistration
    lateinit var roomId : String
     lateinit var opponentUser : User
    lateinit var transfer : Transfer
    lateinit var rcv : RecyclerView
     var needer : Needer ? = null
    var helper : Helper ? = null
//    lateinit var myActivity : MainActivity
    var firebase  = FirebaseFirestore.getInstance()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null){
            transfer = activity as Transfer
        }
        myActivity = activity as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        transfer.bottomVisible()
        check = 0
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat,container , false)

        (context as MainActivity).backListener = this

        myActivity.bottomNavigationGone()

        roomId = arguments?.getString("roomId")?:"x"
        opponentUser = arguments?.getParcelable<User>("opponentUser")!!
        CurrentChat.getInstance().nickName =opponentUser.nickName
                needer?.images?.map { print(it)  }
        needer = arguments?.getParcelable<Needer>("Needer") ?:null
        helper = arguments?.getParcelable<Helper>("helper") ?:null

        binding.ChatOpponentNickname.text = opponentUser.nickName
        binding.back.setOnClickListener { onBackPressed() }
        navController = findNavController()
         rcv = binding.ChatRcv
        rcv.setHasFixedSize(true)
        rcv.setItemViewCacheSize(100)

       var adapter = context?.let { ChatAdapter(navController, it, ArrayList<Chat>(),opponentUser) }!!

        var manager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        manager.stackFromEnd = true
        rcv.layoutManager  = manager
        rcv.scrollToPosition(adapter.itemCount-1)
        model = ViewModelProvider(this).get(ChatViewModel::class.java)
        model.setReadTrue(roomId)
        binding.model = model
        model.setSnapShot(roomId)
//        onScrollListener(rcv,adapter)
            CoroutineScope(Dispatchers.Main).launch {
                if(Chat.getInstance().get(roomId)==null){
                    if(model.initView(roomId)){
                        adapter.list = Chat.getInstance().get(roomId)!!
                        rcv.adapter = adapter
                        rcv.scrollToPosition(adapter.itemCount-1)
                    }else{
                        // initView 가 false 인 결
                    }
                }else{
                    if(Chat.getInstance().get(roomId)?.size!=0){
                        Chat.getInstance().get(roomId)?.let { model.nextChat(roomId, it) }
                        adapter.list = Chat.getInstance().get(roomId)!!
                        rcv.adapter = adapter
                        rcv.scrollToPosition(adapter.itemCount-1)
                    }
                }
            }

        model.msg.observe(viewLifecycleOwner,Observer{
            adapter.list.add(model.msg.value!!)
            adapter.notifyItemChanged(adapter.list.size-1)
            var lastCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            if(lastCompletelyVisibleItemPosition==adapter.list.size-2){
                binding.ChatRcv.scrollToPosition(adapter.itemCount-1)
            } else if(model.msg.value!!.uid.equals(User.getInstance().uid)){
                binding.ChatRcv.scrollToPosition(adapter.itemCount-1)
            }else{
                println("왜 ....")
                // 상대방이 쓴 채팅은 밑으로 안내려가짐

            }
        })
        binding.ChatGoToAlbum.setOnClickListener{
            requestPhoto()
        }
        binding.ChatSend.setOnClickListener {
            if(opponentUser.uid.equals("-1")){
                Toast.makeText(context,"탈퇴한 이용자에게는 메시지를 보낼 수 없습니다",Toast.LENGTH_SHORT).show()
            } else if(model.talk.get()?.length!! >0){
                model.send(roomId,opponentUser)
            }
        }
        return binding.root
    }

    override fun onPause() {
        CurrentChat.getInstance().nickName = ""
        super.onPause()
    }

    fun requestPhoto(){
        when{
                ContextCompat.checkSelfPermission(
                        activity?.applicationContext!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )==PackageManager.PERMISSION_GRANTED ->{
                                 var bundle = Bundle()
                                bundle.putString("roomId",roomId)
                                bundle.putString("opponentUid",opponentUser.uid)
                                navController.navigate(R.id.imagePickerFragment,bundle)
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                        //교육용!!
                    showContextPopupPermission()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }
    }
  private fun showContextPopupPermission(){
      AlertDialog.Builder(activity?.applicationContext!!).setTitle("권한이 필요합니다")
              .setMessage("사진을 불러오기 위해 권한이 필요합니다")
              .setPositiveButton("동의하기"){ _, _ ->
                  requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
              }
              .setNegativeButton("취소하기") { _, _ ->}
              .create()
              .show()
  }
    override fun onStop() {
        super.onStop()
        model.setReadTrue(roomId)
        model.deleteSnapShot()
    }

    override fun onBackPressed() {

//        if(needer!=null){
//            var bundle = Bundle()
//
//            bundle.putParcelable("needer",needer)
//            bundle.putParcelable("writer",opponentUser)
//
//            navController.popBackStack()
//            //이거 .. Pop백스택 해야하는대?....
////            navController.navigate(R.id.neederReadFragment,bundle)
//        }else{
//            navController.popBackStack()
//        }

//        if(helper!=null){
//            var bundle = Bundle()
//            bundle.putParcelable("data",helper)
//            bundle.putParcelable("writer",opponentUser)
//            navController.popBackStack(R.id.HelperReadFragment,false)
//        }
            navController.popBackStack()

    }
    fun onScrollListener(rcv: RecyclerView,adapter: ChatAdapter){
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var firstCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                var lastCompletelyVisibleItemPosition = (rcv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(firstCompletelyVisibleItemPosition == 0){
                    CoroutineScope(Dispatchers.Main).launch {
                      var result = model.scroll(roomId,adapter.list.get(0).timeStamp)
                        var size = result.size
                        adapter.joined(result)
                        rcv.scrollToPosition(size+lastCompletelyVisibleItemPosition)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            1000->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 권한이 부여되었으므로
                    var bundle = Bundle()
                    bundle.putString("roomId",roomId)
                    bundle.putString("opponentUid",opponentUser.uid)
                    navController.navigate(R.id.imagePickerFragment,bundle)
                }else{
                    Toast.makeText(context,"권한이 거부되었습니다!",Toast.LENGTH_SHORT).show()
                }
            }
            else->{

            }
        }
    }
private fun navigatesPhotos(){

}

}