package com.cornershop.counterstest.presentation.ui

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ErrorMessagesViewBinding
import com.cornershop.counterstest.databinding.FragmentCountersBinding
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.adapter.CounterListViewAdapter
import com.cornershop.counterstest.presentation.dialogs.MessageDialog
import com.cornershop.counterstest.presentation.viewModels.CounterEvent
import com.cornershop.counterstest.presentation.viewModels.CounterNavigation
import com.cornershop.counterstest.presentation.viewModels.CountersViewModel
import com.cornershop.counterstest.presentation.viewModels.utils.State
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountersFragment : Fragment() {

    private var _binding: FragmentCountersBinding? = null
    private val binding get() = _binding!!

    lateinit var counterListAdapter:CounterListViewAdapter

    private val viewModel:CountersViewModel by viewModels()

    private fun validateEvents(state: State<CounterNavigation>?) {
        state?.getContentIfNotHandled()?.let { navigation ->
            when(navigation){
                is CounterNavigation.setLoaderState-> navigation.run {
                     when(this.state){
                         true -> binding.loader.visibility = View.VISIBLE
                         else -> binding.loader.visibility = View.GONE
                     }
                }

                is CounterNavigation.setCounterList->navigation.run{
                    val itemsCount= listCounter?.size?:0
                    setTimesAndItems(itemsCount,timesSum)
                    if(itemsCount <= 0)
                        setErrorPlaceHolder(this)
                    counterListAdapter.submitList(listCounter)
                }

                is CounterNavigation.updateCounterList ->navigation.run {
                    val itemsCount= listCounter?.size?:0
                    clearPlaceHolder()
                    if(itemsCount <= 0)
                        setErrorPlaceHolder(this)
                    setTimesAndItems(itemsCount,timesSum)
                    counterListAdapter.submitList(listCounter)
                    counterListAdapter.notifyDataSetChanged()
                }

                is CounterNavigation.setSelectedItemState-> navigation.run{
                    setVisibilityTopSetup(View.VISIBLE,View.GONE)
                    binding.tvSelectedItems.text = "${items?.let {
                        resources.getString(R.string.n_selected,
                            it
                        )
                    }}"
                }

                is CounterNavigation.hideSelectedItemState->{
                    setVisibilityTopSetup(View.GONE,View.VISIBLE)
                    counterListAdapter.submitList(viewModel.listCounterAdapter.value)
                }

                is CounterNavigation.hideSwipeLoaderSave->{
                    binding.srwCounterList.isRefreshing = false
                }

                is CounterNavigation.onErrorLoadingCounterList-> navigation.run{
                    showErrorNotCountersYet(title,message)
                }

                is CounterNavigation.onNoResultCounterList->navigation.run {
                    setTimesAndItems(0,0)
                    setErrorPlaceHolder(this)
                    counterListAdapter.submitList(listOf())
                }

                is CounterNavigation.onErrorLoadingCounterListNetwork-> navigation.run{
                    binding.errMessage.title = title?.let { resources.getString(it) }
                    binding.errMessage.message = message?.let { resources.getString(it) }
                    binding.errMessage.setActionRetry {
                        viewModel.postEvent(CounterEvent.getListCounterInit)
                        binding.errMessage.hideAll()
                    }
                    binding.errMessage.setView()
                }
            }
        }
    }

    private fun showErrorNotCountersYet(title:Int?, message:Int?) {
        binding.errMessage.title = title?.let { resources.getString(it) }
        binding.errMessage.message = message?.let { resources.getString(it) }
        binding.errMessage.setView()
    }

    private fun setTimesAndItems(items:Int?,times:Int?) {
        if (items != null) {
            if(items > 0) {
                binding.viewTimesItems.visibility = View.VISIBLE
                binding.viewTimesItems.tvItems.text = resources.getString(R.string.n_items, items)
                binding.viewTimesItems.tvTimes.text = resources.getString(R.string.n_times, times)
            }else{
                binding.viewTimesItems.visibility = View.GONE
            }
        }
    }

    private fun setErrorPlaceHolder(counterNavigation: CounterNavigation){
        when(counterNavigation){
            is CounterNavigation.onNoResultCounterList-> {
                showErrorNotCountersYet(null, R.string.no_results)
            }
            else->{
                showErrorNotCountersYet(R.string.no_counters, R.string.no_counters_phrase)

            }
        }
    }

    private fun clearPlaceHolder() {
        binding.errMessage.message = ""
        binding.errMessage.title = ""
        binding.errMessage.setView()
    }

    private fun setVisibilityTopSetup(clSelectVisibility:Int, searchViewVisibility: Int) {
        binding.clSelected.visibility = clSelectVisibility
        binding.searchView.visibility = searchViewVisibility
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(str: String?): Boolean {
                viewModel.postEvent(CounterEvent.FilterCounter(str))
                return false
            }

        })

        setupListAdapter()

        binding.btnAddCounter.setOnClickListener {
            val action= CountersFragmentDirections.actionCountersFragmentToCreateCounterFragment()
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.countersFragment, true).build()
            findNavController().navigate(action, navOptions)
        }

        binding.ivDeleteCounter.setOnClickListener {
            var dialog = MessageDialog.Builder()
                .setMessage(getString(R.string.delete_x_question,viewModel.listSelectedCounterAdapter.value))
                .setPositiveButton(getString(R.string.delete),DialogInterface.OnClickListener { dialogInterface, i ->
                    viewModel.postEvent(CounterEvent.DeleteSelectedCounters)
                })
                .setNegativeButton(getString(R.string.cancel),DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                }).dialog(AlertDialog.Builder(requireContext())).build()

            dialog.show(getChildFragmentManager(),"")
        }

        binding.ivCancel.setOnClickListener {
            setVisibilityTopSetup(View.GONE,View.VISIBLE)
            viewModel.postEvent(CounterEvent.unselectAll)
        }

        binding.srwCounterList.setOnRefreshListener {
            viewModel.postEvent(CounterEvent.getListCounterFromSwipe)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountersBinding.inflate(inflater,container,false)
        binding.loader.visibility = View.GONE
        viewModel.states.observe(viewLifecycleOwner, Observer(this::validateEvents))
        return binding.root
    }

    private fun setupListAdapter( ) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext()
            )
            counterListAdapter = CounterListViewAdapter(
                {counter->
                    viewModel.postEvent(CounterEvent.IncreaseCounter(counter))},
                { counter->
                    viewModel.postEvent(CounterEvent.DecreaseCounter(counter))
                },
                {listCounter->
                    viewModel.postEvent(CounterEvent.SelectCounters(listCounter))
                }
            )
            adapter = counterListAdapter
        }
    }

}