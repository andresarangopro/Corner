package com.cornershop.counterstest.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.databinding.FragmentCreateCounterBinding
import com.cornershop.counterstest.presentation.viewModels.CounterViewModelFactory
import com.cornershop.counterstest.presentation.viewModels.CountersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.viewModels.utils.Event


@AndroidEntryPoint
class CreateCounterFragment : Fragment() {
    
    private var _binding:FragmentCreateCounterBinding?=null
    private val binding get() = _binding


    lateinit var viewModel: CountersViewModel

    @Inject
    lateinit var viewModelFactory: CounterViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateCounterBinding.inflate(inflater, container, false)
        setupViewModel()
        viewModel.events.observe(viewLifecycleOwner, Observer(this::validateEvents))
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
            viewModel.postEvent(CountersViewModel.CounterEvent.CreateCounter(
                   binding?.etCounter?.text.toString()
                )
            )
        }
    }

    private fun validateEvents(event: Event<CountersViewModel.CounterNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
           when(navigation){
               is CountersViewModel.CounterNavigation.showLoaderSave->{
                   handlerLoaderSave(View.GONE,View.VISIBLE)
               }
               is CountersViewModel.CounterNavigation.hideLoaderSave->{
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

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CountersViewModel::class.java)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            CreateCounterFragment().apply {            }
    }
}