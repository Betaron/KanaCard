package com.betaron.kanacard.ui.main

data class MainState(
    val alphabet : Int = 0,
    var alphabetSymbols: Array<String> = emptyArray(),
    val currentSymbol : String = "",
    val currentSymbolIndex: Int = 0
)
