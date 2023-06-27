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

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val alphabetUseCases: AlphabetUseCases
) : ViewModel() {
    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    private val hiragana: Array<String> = application.resources.getStringArray(R.array.hiragana)
    private val katakana: Array<String> = application.resources.getStringArray(R.array.katakana)

    init {
        loadAlphabet()
    }

    fun onEvent(event: MainEvent){
        when(event){
            is MainEvent.SwitchAlphabet -> {
                if (state.value.alphabet != event.alphabetIndex)
                    viewModelScope.launch {
                        _state.value = state.value.copy(
                            alphabet = event.alphabetIndex,
                            alphabetSymbols =
                                if (event.alphabetIndex == Alphabet.hiragana_VALUE) hiragana
                                else katakana
                        )

                        _state.value = state.value.copy(
                            currentSymbol =
                                state.value.alphabetSymbols[state.value.currentSymbolIndex]
                        )

                        alphabetUseCases.setAlphabet(_state.value.alphabet)
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
                alphabetSymbols =
                    if (alphabetIndex == Alphabet.hiragana_VALUE) hiragana
                    else katakana,
                currentSymbolIndex = lastSymbolIndex
            )

            _state.value = state.value.copy(
                currentSymbol =
                    state.value.alphabetSymbols[state.value.currentSymbolIndex]
            )
        }
    }
}
