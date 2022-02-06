package com.cornershop.counterstest.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.adapter.CounterRecyclerViewAdapter
import com.cornershop.counterstest.presentation.utils.onQueryTextChanged
import com.cornershop.counterstest.presentation.viewModels.CounterViewModelFactory
import com.cornershop.counterstest.presentation.viewModels.CountersViewModel
import com.cornershop.counterstest.presentation.viewModels.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_counters.view.*
import javax.inject.Inject


@AndroidEntryPoint
class CountersFragment : Fragment() {

    private var _binding: FragmentCountersBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: CountersViewModel

    @Inject
    lateinit var viewModelFactory: CounterViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun validateEvents(event: Event<CountersViewModel.CounterNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when(navigation){
                is CountersViewModel.CounterNavigation.setLoaderState-> navigation.run {
                    /*      true -> loader.visibility = View.VISIBLE
                        else -> loader.visibility = View.GONE*/
                }

                is CountersViewModel.CounterNavigation.setCounterList->navigation.run{
                    setupList(binding.recyclerView, listCounter)
                }
            }
        }
    }

    private fun observePlaylists(view: View) {
        viewModel.counterList.observe(this as LifecycleOwner, { counterList ->
            if (counterList.getOrNull() != null)
                setupList(view.recyclerView, counterList.getOrNull()!!)
            else {
                //TODO
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.searchView.onQueryTextChanged {

        }

        binding.btnAddCounter.setOnClickListener {
            val action= CountersFragmentDirections.actionCountersFragmentToCreateCounterFragment()
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountersBinding.inflate(inflater,container,false)

        setupViewModel()
        viewModel.events.observe(viewLifecycleOwner, Observer(this::validateEvents))
        observePlaylists(binding.root)
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CountersViewModel::class.java)
    }

    private fun setupList(
        view: View?,
        playlists: List<Counter>?
    ) {
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)

            adapter = CounterRecyclerViewAdapter(playlists,{id->
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CountersFragment().apply { }
    }
}