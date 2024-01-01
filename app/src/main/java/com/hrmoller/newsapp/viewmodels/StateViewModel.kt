package com.hrmoller.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


//Base ViewModel with state handling

abstract class StateViewModel<S : UiState>(initialValue: S) : ViewModel() {

    protected val _state: MutableStateFlow<S> = MutableStateFlow(initialValue)

    val state: StateFlow<S>
        get() = _state.asStateFlow()
}

interface UiState