package com.betaron.kanacard.ui.main

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

                _state.value = state.value.copy(
                    answerInputError = !utilUseCases.validateLanguage(event.text)
                )
            }

            is MainEvent.CheckAnswer -> {
                val isCorrect = alphabetUseCases.checkAnswer(
                    state.value.answer, state.value.currentSymbolIndex
                )

                if (isCorrect) {
                    pickNewSymbol()
                    _state.value = state.value.copy(
                        answer = ""
                    )
                } else
                    _state.value = state.value.copy(
                        isCardShake = !state.value.isCardShake
                    )
            }

            is MainEvent.SelectSymbol -> {
                if (event.checked)
                    _state.value = state.value.copy(
                        selectedSymbols = state.value.selectedSymbols + event.index
                    )
                else
                    _state.value = state.value.copy(
                        selectedSymbols = state.value.selectedSymbols - event.index
                    )
            }

            is MainEvent.SaveSelected -> {
                viewModelScope.launch {
                    alphabetUseCases.setSelectedSymbols(_state.value.selectedSymbols)
                }
            }

            is MainEvent.SwitchImeVisibility -> {
                _state.value = state.value.copy(
                    imeIsFullyState = event.state
                )
            }

            is MainEvent.SelectSymbolsGroup -> {
                if (event.checked)
                    _state.value = state.value.copy(
                        selectedSymbols = (state.value.selectedSymbols + event.symbolsIds).distinct()
                    )
                else
                    _state.value = state.value.copy(
                        selectedSymbols = state.value.selectedSymbols - event.symbolsIds.toSet()
                    )
            }

            is MainEvent.SetSelectedSymbols -> {
                _state.value = state.value.copy(
                    selectedSymbols = event.symbolsIds
                )
            }

            is MainEvent.SetShakeState -> {
                _state.value = state.value.copy(
                    isCardShake = event.state
                )
            }

            else -> {}
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
                selectedSymbols = alphabetUseCases.getSelectedSymbols().toMutableList()
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
