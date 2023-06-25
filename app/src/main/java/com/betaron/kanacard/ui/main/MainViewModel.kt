package com.betaron.kanacard.ui.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaron.kanacard.use_case.AlphabetUseCases
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alphabetUseCases: AlphabetUseCases
) : ViewModel() {
    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    private var loadAlphabetJob : Job? = null

    init {
        loadAlphabet()
    }

    fun onEvent(event: MainEvent){
        when(event){
            is MainEvent.SwitchAlphabet -> {
                if (state.value.alphabet != event.alphabetIndex)
                    viewModelScope.launch {
                        _state.value = state.value.copy(
                            alphabet = event.alphabetIndex
                        )
                        alphabetUseCases.setAlphabet(_state.value.alphabet)
                    }
            }
        }
    }

    private fun loadAlphabet() {
        runBlocking {
            _state.value = state.value.copy(
                alphabet = alphabetUseCases.getAlphabet()
            )
        }
    }
}
