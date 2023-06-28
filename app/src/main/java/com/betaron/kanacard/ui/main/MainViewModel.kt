package com.betaron.kanacard.ui.main

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaron.kanacard.R
import com.betaron.kanacard.use_case.AlphabetUseCases
import com.kanacard.application.Alphabet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alphabetUseCases: AlphabetUseCases
) : ViewModel() {
    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    init {
        loadAlphabet()
    }

    fun onEvent(event: MainEvent){
        when(event){
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
                val randomIndex = if (state.value.selectedSymbols.size == 1) 0
                else {
                    val mutableSelected = state.value.selectedSymbols.toMutableList()
                    mutableSelected.remove(state.value.currentSymbolIndex)
                    mutableSelected.random()
                }
                _state.value = state.value.copy(
                    currentSymbolIndex = randomIndex
                )
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
                currentSymbolIndex = lastSymbolIndex
            )
            _state.value = state.value.copy(
                selectedSymbols = listOf(0, 1, 2, 3, 4)
            )
        }
    }
}
