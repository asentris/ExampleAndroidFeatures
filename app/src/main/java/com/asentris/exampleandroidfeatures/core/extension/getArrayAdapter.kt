package com.asentris.exampleandroidfeatures.core.extension

import android.content.Context
import android.widget.ArrayAdapter
import com.asentris.exampleandroidfeatures.R

fun <T> List<T>.getArrayAdapter(context: Context): ArrayAdapter<T> =
    ArrayAdapter(context, R.layout.item_dropdown, this)