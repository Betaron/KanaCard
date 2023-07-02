package com.betaron.kanacard.ui.main

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaron.kanacard.use_case.AlphabetUseCases
import com.betaron.kanacard.use_case.UtilUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alphabetUseCases: AlphabetUseCases,
    private val utilUseCases: UtilUseCases
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

            is MainEvent.SkipSymbol -> {
                pickNewSymbol()
            }

            is MainEvent.EnteredAnswer -> {
                if (event.text.length <= 3)
                    _state.value = state.value.copy(
                        answer = event.text
                    )
                else
                    Toast.makeText(event.context, "length", Toast.LENGTH_SHORT).show()

                if (!utilUseCases.validateLanguage(event.text))
                    Toast.makeText(event.context, "language", Toast.LENGTH_SHORT).show()
            }

            is MainEvent.CheckAnswer -> {
                val isCorrect = alphabetUseCases.checkAnswer(
                    state.value.answer, state.value.currentSymbolIndex
                )

                if (isCorrect) pickNewSymbol()
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

    private fun pickNewSymbol() {
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
