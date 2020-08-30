package com.kardani.fwebview

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisibility(view: View, visible: Boolean){

    if(visible){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.GONE
    }

}