package com.betaron.kanacard.use_case

data class AlphabetUseCases(
    val getAlphabet: GetAlphabet,
    val setAlphabet: SetAlphabet,
    val getLastSymbol: GetLastSymbol,
    val getAlphabetSymbolsSet: GetAlphabetSymbolsSet,
    val setLastSymbol: SetLastSymbol,
    val getSelectedSymbols: GetSelectedSymbols,
    val selectRandomSymbol: SelectRandomSymbol,
    val checkAnswer: CheckAnswer
)
