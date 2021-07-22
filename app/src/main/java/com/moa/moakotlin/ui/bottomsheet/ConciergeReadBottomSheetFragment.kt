package com.moa.moakotlin.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moa.moakotlin.R
import kotlinx.android.synthetic.main.bottom_sheet_concierge_read_option.view.*


 class ConciergeReadBottomSheetFragment(val itemCLick: (Int) -> Unit): BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.bottom_sheet_concierge_read_option, container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.ConciergeReadOptionModify.setOnClickListener {
            itemCLick(0)
            dismiss()
        }
        view.ConciergeReadOptionDelete.setOnClickListener {
            itemCLick(1)
            dismiss()
        }
    }

}