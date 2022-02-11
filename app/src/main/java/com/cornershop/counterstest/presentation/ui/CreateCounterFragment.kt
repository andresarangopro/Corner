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
    
    private var _binding:FragmentCreateCounterBinding?=null
    private val binding get() = _binding


    private val viewModel:CountersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        binding?.ivCancel?.setOnClickListener{
            val action = CreateCounterFragmentDirections.actionCreateCounterFragmentToCountersFragment()
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.createCounterFragment, true).build()
            findNavController().navigate(action, navOptions)
        }

        binding?.tvSave?.setOnClickListener {
            viewModel.postEvent(
                CounterEvent.CreateCounter(
                   binding?.etCounter?.text.toString()
                )
            )
        }
    }

    private fun validateEvents(state: State<CounterNavigation>?) {
        state?.getContentIfNotHandled()?.let { navigation ->
           when(navigation){
               is CounterNavigation.showLoaderSave->{
                   handlerLoaderSave(View.GONE,View.VISIBLE)
               }
               is CounterNavigation.hideLoaderSave->{
                   handlerLoaderSave(View.VISIBLE,View.GONE)
                   cleanInputs()
               }
           }
        }
    }

    private fun handlerLoaderSave(visibilityText:Int,visibilityLoader:Int){
        binding?.tvSave?.visibility = visibilityText
        binding?.loaderSave?.visibility = visibilityLoader
    }

    fun cleanInputs(){
        binding?.etCounter?.setText("")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateCounterFragment().apply {            }
    }
}