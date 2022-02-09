package com.cornershop.counterstest.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentWelcomeBinding
import android.content.Context

import android.content.SharedPreferences
import androidx.navigation.NavOptions

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPref:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater,container, false)
        sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )!!
        if(isOpenBefore() == true)
            getStartedAction()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutInclude?.buttonStart?.setOnClickListener {
            getStartedAction()
            commitFragmentIsOpenBefore()
        }

    }

    private fun commitFragmentIsOpenBefore() {
        val edit = sharedPref.edit()
        edit.putBoolean(getString(R.string.pref_previously_started), java.lang.Boolean.TRUE)
        edit.commit()
    }


    fun isOpenBefore(): Boolean? {
        return sharedPref?.getBoolean(getString(R.string.pref_previously_started),false)
    }

    private fun getStartedAction() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToCountersFragment()
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.welcomeFragment, true).build()
        findNavController().navigate(action, navOptions)
    }

    override fun onResume() {
        super.onResume()

    }
}