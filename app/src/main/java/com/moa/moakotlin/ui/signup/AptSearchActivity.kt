package com.moa.moakotlin.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.MyApp
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.databinding.ActivityAptSearchBinding
import com.moa.moakotlin.recyclerview.algoria.SearchAptAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AptSearchActivity : AppCompatActivity() {

  lateinit var model : AptSearchActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAptSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(AptSearchActivityViewModel::class.java)
        binding.model  = model
        var adapter = SearchAptAdapter()

        binding.searchAptActivityRcv.adapter = adapter
        binding.back.setOnClickListener { finish() }
        binding.searchAptActivityRcv.layoutManager = LinearLayoutManager(this)

        binding.aptSearchEdit.addTextChangedListener {
            CoroutineScope(Dispatchers.Main).launch {
                model.updateSearchView()
            }
        }

        model.AptList.observe(this, Observer {
            adapter.submitList(it)
        })


        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent()

                intent.putExtra("apt",adapter.currentList[position])

                setResult(7000,intent)

                finish()

            }
        })
    }


}