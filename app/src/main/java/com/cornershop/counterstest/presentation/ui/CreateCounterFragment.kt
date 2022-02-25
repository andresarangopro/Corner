package com.cornershop.counterstest.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.databinding.FragmentCreateCounterBinding
import com.cornershop.counterstest.presentation.viewModels.CountersViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.viewModels.CounterEvent
import com.cornershop.counterstest.presentation.viewModels.CounterNavigation
import com.cornershop.counterstest.presentation.viewModels.utils.State


@AndroidEntryPoint
class CreateCounterFragment : Fragment() {

    private var _binding: FragmentCreateCounterBinding? = null

    private val binding get() = _binding

    private val viewModel: CountersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateCounterBinding.inflate(inflater, container, false)
        viewModel.states.observe(viewLifecycleOwner, Observer(this::validateEvents))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.ivCancel?.setOnClickListener {
            val action =
                CreateCounterFragmentDirections.actionCreateCounterFragmentToCountersFragment()
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.createCounterFragment, true).build()
            findNavController().navigate(action, navOptions)
        }

        binding?.tvSave?.setOnClickListener {
            when (validateNameCounter()) {
                true -> {
                    viewModel.postEvent(
                        CounterEvent.CreateCounter(
                            binding?.etCounter?.text.toString()
                        )
                    )
                }
                false -> {
                    binding?.etCounter?.error = resources.getString(R.string.err_empty_title)
                }
            }
        }
    }

    private fun validateNameCounter(): Boolean {
        return binding?.etCounter?.text?.isNotBlank() == true && binding?.etCounter?.text?.isNotEmpty() == true
    }

    private fun validateEvents(state: State<CounterNavigation>?) {
        state?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is CounterNavigation.ShowLoaderSave -> {
                    handlerLoaderSave(View.GONE, View.VISIBLE)
                }
                is CounterNavigation.HideLoaderSave -> {
                    handlerLoaderSave(View.VISIBLE, View.GONE)
                    cleanInputs()
                }
                else -> {}
            }
        }
    }

    private fun handlerLoaderSave(visibilityText: Int, visibilityLoader: Int) {
        binding?.tvSave?.visibility = visibilityText
        binding?.loaderSave?.visibility = visibilityLoader
    }

    private fun cleanInputs() {
        binding?.etCounter?.setText("")
    }

}