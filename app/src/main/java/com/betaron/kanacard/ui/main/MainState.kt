package com.betaron.kanacard.ui.main

data class MainState(
    val alphabet: Int = 0,
    var alphabetSymbols: Array<String> = emptyArray(),
    val currentSymbolIndex: Int = 0,
    val selectedSymbols: MutableList<Int> = mutableListOf(),
    val answer: String = "",
    val answerInputError: Boolean = false,
    val imeIsFullyState: Boolean = false
)
