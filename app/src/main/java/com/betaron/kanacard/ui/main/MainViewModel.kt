package com.betaron.kanacard.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaron.kanacard.use_case.AlphabetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alphabetUseCases: AlphabetUseCases
) : ViewModel() {
    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    init {
        loadAlphabet()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.SwitchAlphabet -> {
                if (state.value.alphabet != event.alphabetIndex)
                    _state.value = state.value.copy(
                        alphabet = event.alphabetIndex,
                        alphabetSymbols =
                        alphabetUseCases.getAlphabetSymbolsSet(event.alphabetIndex)
                    )

                viewModelScope.launch {
                    alphabetUseCases.setAlphabet(_state.value.alphabet)
                }
            }

            is MainEvent.PickNewSymbol -> {
                val randomIndex = alphabetUseCases.selectRandomSymbol(
                    state.value.selectedSymbols,
                    state.value.currentSymbolIndex
                )

                _state.value = state.value.copy(
                    currentSymbolIndex = randomIndex
                )

                viewModelScope.launch {
                    alphabetUseCases.setLastSymbol(state.value.currentSymbolIndex)
                }
            }
        }
    }

    private fun loadAlphabet() {
        runBlocking {
            val alphabetIndex = alphabetUseCases.getAlphabet()
            val lastSymbolIndex = alphabetUseCases.getLastSymbol()
            _state.value = state.value.copy(
                alphabet = alphabetIndex,
                alphabetSymbols = alphabetUseCases.getAlphabetSymbolsSet(alphabetIndex),
                currentSymbolIndex = lastSymbolIndex,
                selectedSymbols = alphabetUseCases.getSelectedSymbols()
            )
        }
    }
}
