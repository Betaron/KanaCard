package com.betaron.kanacard.use_case

class SelectRandomSymbol {
    operator fun invoke(
        selectedSymbols: List<Int>,
        previousSymbolIndex: Int
    ): Int {
        return if (selectedSymbols.isEmpty()) 0
        else if (selectedSymbols.size == 1) selectedSymbols[0]
        else {
            val mutableSelected = selectedSymbols.toMutableList()
            mutableSelected.remove(previousSymbolIndex)
            mutableSelected.random()
        }
    }
}