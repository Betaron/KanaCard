package com.betaron.kanacard.ui.main

data class MainState(
    val alphabet: Int = 0,
    var alphabetSymbols: Array<Array<String>> = arrayOf(),
    val currentSymbolIndex: Int = 0,
    val selectedSymbols: List<Int> = listOf(),
    val answer: String = "",
    val answerInputError: Boolean = false,
    val imeIsFullyState: Boolean = false,
    val isCardShake: Boolean = false
)
