package com.moa.moakotlin.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moa.moakotlin.R
import kotlinx.android.synthetic.main.bottom_sheet_concierge_read_option.view.*
import kotlinx.android.synthetic.main.bottom_sheet_needer_hire_status.view.*

class NeederHireStatusBottomSheet(val itemCLick: () -> Unit): BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.bottom_sheet_needer_hire_status, container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.ConciergeReadHireStatusComplete.setOnClickListener {
            itemCLick()
            dismiss()
        }

    }

}