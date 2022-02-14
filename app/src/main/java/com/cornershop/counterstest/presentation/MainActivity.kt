package com.cornershop.counterstest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
