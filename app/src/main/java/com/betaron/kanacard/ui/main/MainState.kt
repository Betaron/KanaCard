package com.betaron.kanacard.ui.main

data class MainState(
    val alphabet: Int = 0,
    var alphabetSymbols: Array<String> = emptyArray(),
    val currentSymbolIndex: Int = 0,
    val selectedSymbols: List<Int> = emptyList(),
    val answer: String = "",
    val answerInputError: Boolean = false
)
