package com.cornershop.counterstest.presentation.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.cornershop.counterstest.R

class ItemTimesView : FrameLayout {


    constructor(context: Context, attrs: AttributeSet, defStyle:Int):super(
        context,
        attrs,
        defStyle){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet):super(
        context,
        attrs ){
        initView()
    }

    constructor(context: Context):super(context){
        initView()
    }

    private fun initView(){
        val view = View.inflate(context, R.layout.items_times_view, null)
        addView(view)
    }


}