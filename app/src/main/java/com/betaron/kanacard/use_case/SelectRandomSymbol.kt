package com.betaron.kanacard.use_case

class SelectRandomSymbol {
    operator fun invoke(
        selectedSymbols: List<Int>,
        previousSymbolIndex: Int
    ) : Int {
        return if (selectedSymbols.size == 1) 0
        else {
            val mutableSelected = selectedSymbols.toMutableList()
            mutableSelected.remove(previousSymbolIndex)
            mutableSelected.random()
        }
    }
}