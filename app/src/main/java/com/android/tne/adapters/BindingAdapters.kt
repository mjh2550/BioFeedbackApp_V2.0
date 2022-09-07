package com.android.tne.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("test_adapter")
    fun setText(view : TextView , text : String){
        view.text = text + "\n" + text
    }
}