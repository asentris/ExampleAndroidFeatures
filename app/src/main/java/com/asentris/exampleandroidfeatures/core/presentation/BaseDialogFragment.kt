package com.asentris.exampleandroidfeatures.core.presentation

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onStart() {
        super.onStart()
        val mp = ViewGroup.LayoutParams.MATCH_PARENT
        val wc = ViewGroup.LayoutParams.WRAP_CONTENT
        val height =
            if (wc >= 400) wc
            else 400
        dialog?.window?.setLayout(mp, height)
    }
}