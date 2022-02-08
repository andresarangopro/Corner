package com.cornershop.counterstest.presentation.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.cornershop.counterstest.R
import kotlinx.android.synthetic.main.error_messages_view.view.*

class ErrorMessagesView: FrameLayout {

    lateinit var view :View

    var title:String? = null
        internal set
    var message:String?=null
        internal set

    var actionRetry:OnClickListener?=null
        private set

    constructor(context: Context, attrs: AttributeSet, defStyle:Int):super(
        context,
        attrs,
        defStyle){
        initView()
    }

    fun setActionRetry(action:OnClickListener){
        this.actionRetry = action
    }

    constructor(context: Context, attrs: AttributeSet):super(
        context,
        attrs ){
        initView()
    }

    constructor(context: Context):super(context){
        initView()
    }

    fun setView(){
        if(title?.isNotNullAndEmpty() == true) {
            view.tvTitle.visibility = View.VISIBLE
            view.tvTitle.text = title
        }else{
            if(view.tvTitle.visibility == View.VISIBLE)
                view.tvTitle.visibility = View.GONE
        }
        if(message?.isNotNullAndEmpty() == true) {
            view.tvMessage.visibility = View.VISIBLE
            view.tvMessage.text = message
        }else{
            if(view.tvMessage.visibility == View.VISIBLE)
                view.tvMessage.visibility = View.GONE
        }
        if(actionRetry != null) {
            view.tvRetry.visibility = View.VISIBLE
            view.tvRetry.setOnClickListener (actionRetry)

        }else{
            if(view.tvRetry.visibility == View.VISIBLE)
                view.tvRetry.visibility = View.GONE
        }
    }

    fun hideAll(){
        view.tvTitle.visibility = View.GONE
        view.tvMessage.visibility = View.GONE
        view.tvRetry.visibility = View.GONE
        actionRetry = null
    }

    fun String.isNotNullAndEmpty() = this?.isNotEmpty() && this?.isNotEmpty()

    private fun initView(){
        view = View.inflate(context, R.layout.error_messages_view, null)
        addView(view)
    }



}