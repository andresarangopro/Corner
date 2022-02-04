package com.cornershop.counterstest.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.databinding.FragmentWelcomeBinding
import com.cornershop.counterstest.presentation.utils.onQueryTextChanged


class CountersFragment : Fragment() {

    private var _binding: FragmentCountersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.onQueryTextChanged {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountersBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CountersFragment().apply { }
    }
}