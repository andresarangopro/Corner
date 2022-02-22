package com.cornershop.counterstest.presentation.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.cornershop.counterstest.databinding.ItemsTimesViewBinding

class ItemTimesView : FrameLayout {

    private lateinit var binding: ItemsTimesViewBinding
    lateinit var tvItems: TextView
    lateinit var tvTimes: TextView

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }


    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs
    ) {
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun initView() {
        binding = ItemsTimesViewBinding.inflate(
            LayoutInflater.from(context),
            parent as ViewGroup?, false
        )

        tvItems = binding.tvItems
        tvTimes = binding.tvTimes
        addView(binding.root)
    }


}