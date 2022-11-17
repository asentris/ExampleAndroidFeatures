package com.asentris.exampleandroidfeatures.core.extension

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.hideSoftKeyboard(view: View) {
    val imm: InputMethodManager? =
        ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}