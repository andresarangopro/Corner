package com.cornershop.counterstest.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cornershop.counterstest.usecase.CounterUseCases
import com.example.requestmanager.CounterService
import javax.inject.Inject

class CounterViewModelFactory @Inject constructor(
    private val counterUseCases: CounterUseCases
):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountersViewModel(counterUseCases) as T
    }
}