package com.moa.moakotlin.ui.concierge.needer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.recyclerview.concierge.CalendarAdapter
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CustomCalendarActivity : AppCompatActivity() {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private lateinit var selectedDate: LocalDateTime
    private lateinit var selectedDate2: LocalDateTime
    private var isDelete = true
    private lateinit var nowYear : String
    private lateinit var nowMonth : String
    private lateinit var nowDay : String
    private lateinit var  calendarAdapter : CalendarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)
        initWidgets()
        selectedDate = LocalDateTime.now();
        selectedDate2 = LocalDateTime.now()
        val formatnowDate: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MMMM d")
        var nowDate = selectedDate2.format(formatnowDate)
        var list  = nowDate.split(" ")
         nowYear = list.get(0)
         nowMonth = list.get(1)
         nowDay = list.get(2)

        setMonthView()

        findViewById<Button>(R.id.CustomCalendarSubmit).setOnClickListener {
            var intent = Intent()
            intent.putExtra("hopeYear",selectedDate.year.toString())
            intent.putExtra("hopeMonth",selectedDate.monthValue.toString())
            intent.putExtra("hopeDay",calendarAdapter.list[calendarAdapter.selectPosition])
            setResult(6000,intent)
            finish()
        }
    }

    private fun visibleTextPrev(){
        if(selectedDate?.month==selectedDate2.month &&
            selectedDate?.year==selectedDate2.year){
        findViewById<Button>(R.id.CustomCalendarPrev).text = "  "
        }else{
            findViewById<Button>(R.id.CustomCalendarPrev).text ="이전"
        }
    }

    private fun setButtonBackgroundChange(check : Boolean){
        if(check){
            findViewById<Button>(R.id.CustomCalendarSubmit).setBackgroundResource(R.drawable.button_shape_main_color)
            findViewById<Button>(R.id.CustomCalendarSubmit).isClickable = true
        }else{
            findViewById<Button>(R.id.CustomCalendarSubmit).setBackgroundResource(R.drawable.shape_unable_radius_15)
            findViewById<Button>(R.id.CustomCalendarSubmit).isClickable = false
        }

    }
    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
    }

    private fun setMonthView() {


        monthYearText!!.text = selectedDate?.let { monthYearFromDate(it) }
        val daysInMonth = selectedDate?.let { daysInMonthArray(it) }
        visibleTextPrev()

        if (daysInMonth != null) {
            check(daysInMonth)
            if(isDelete){
                removeWeek(daysInMonth)
            }
        }
        calendarAdapter = CalendarAdapter(daysInMonth,nowYear,nowMonth,nowDay,selectedDate)


        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter

        calendarAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                if(calendarAdapter.list[position].equals("").not()){
                    if(selectedDate?.year!! > selectedDate2?.year!!){
                        calendarAdapter.selectPosition = position
                        calendarAdapter.notifyDataSetChanged()
                        setButtonBackgroundChange(true)
                    }else{
                        if(selectedDate?.month!! > selectedDate2?.month!!){
                            calendarAdapter.selectPosition = position
                            calendarAdapter.notifyDataSetChanged()
                            setButtonBackgroundChange(true)
                        }else{
                            if(nowDay.toInt()<=calendarAdapter.list[position].toInt()){
                                calendarAdapter.selectPosition = position
                                calendarAdapter.notifyDataSetChanged()
                                setButtonBackgroundChange(true)
                            }
                        }
                    }


                }

            }

        })
    }


    private fun check(daysInMonth : ArrayList<String>){
        for(i in 0..daysInMonth?.size!!){
            if(i==7){
                break
            }
            isDelete = !daysInMonth.get(i).equals("").not()
        }
    }

    private fun removeWeek(daysInMonth : ArrayList<String>){
        for(i in 0..6){
            daysInMonth?.removeAt(0)
        }
    }
    private fun daysInMonthArray(date: LocalDateTime): ArrayList<String> {
        val daysInMonthArray: ArrayList<String> = ArrayList()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDateTime = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek: Int = firstOfMonth.getDayOfWeek().getValue()
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDateTime): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MMMM")
        return date.format(formatter)
    }

    fun previousMonthAction(view: View?) {
        if(selectedDate?.month==selectedDate2.month &&
                selectedDate?.year==selectedDate2.year){

        }else{
            selectedDate = selectedDate!!.minusMonths(1)
            setMonthView()
            setButtonBackgroundChange(false)
            calendarAdapter.selectPosition = -1
        }

    }

    fun nextMonthAction(view: View?) {
        selectedDate = selectedDate!!.plusMonths(1)
        setButtonBackgroundChange(false)
        calendarAdapter.selectPosition = -1
        setMonthView()
    }
}