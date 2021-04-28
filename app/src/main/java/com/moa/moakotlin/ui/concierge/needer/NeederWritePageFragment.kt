package com.moa.moakotlin.ui.concierge.needer

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentNeederWritePageBinding
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class NeederWritePageFragment : Fragment() {

    lateinit var binding: FragmentNeederWritePageBinding

    lateinit var navController: NavController

    lateinit var model: NeederWritePageViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_needer_write_page, container, false)
        navController = findNavController()

        model = ViewModelProvider(this, KidViewModelFactory(navController))
            .get(NeederWritePageViewModel::class.java)
        binding.model = model

        var now = LocalDateTime.now()

        model.year.set(now.year)
        model.month.set(now.monthValue)
        model.day.set(now.dayOfMonth)

    var adapter = context?.let { KidWritePictureAdapter(ArrayList(), it,resources) }
        var manager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        if(Picture.getInstance()!=null){
            adapter?.list = Picture.getInstance()
            binding.pictureCount.text = adapter?.list?.size.toString()
        }
        binding.rcv.layoutManager = manager
        binding.rcv.adapter = adapter
        binding.goToAlbum.setOnClickListener {
            navController.navigate(R.id.action_kidWritePageFragment_to_kidImagePicker)
        }

        binding.categoryLayout.setOnClickListener {
            selectFirstType()
        }
        binding.submit.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                if (adapter != null) {
                    model.submit(adapter.list)
                }

            }
            return@setOnClickListener
        }
        return binding.root
    }

    fun selectFirstType(){
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var items = resources.getStringArray(R.array.mainCategory)
        builder.setItems(items,DialogInterface.OnClickListener { dialogInterface, i ->
            model.firstType = items.get(i)
            detailType(items.get(i))
        })
        var alertDialog : AlertDialog =builder.create()
        alertDialog.setTitle("원하는 영역을 선택해주세요!")
        alertDialog.show()
    }
    fun detailType(firstType : String){
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var items : Array<String>
        if(firstType.equals("인테리어")){
            items = resources.getStringArray(R.array.interiorDetail)
        }else if(firstType.equals("육아/교육")){
            items = resources.getStringArray(R.array.kidDetail)
        }else if(firstType.equals("품앗이")){
            items = resources.getStringArray(R.array.laborDetail)
        }else if(firstType.equals("반려동물")){
            items = resources.getStringArray(R.array.petDetail)
        }else{
            return
        }
        builder.setItems(items,DialogInterface.OnClickListener { dialogInterface, i ->
            model.secondType = items.get(i)
        })
        var alertDialog : AlertDialog =builder.create()
        alertDialog.setTitle("세부 영역을 선택해주세요")
        alertDialog.show()
    }

    override fun onStop() {
        super.onStop()
    }
}




