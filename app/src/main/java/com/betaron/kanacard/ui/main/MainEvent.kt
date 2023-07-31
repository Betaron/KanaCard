package com.betaron.kanacard.ui.main

sealed class MainEvent {
    data class SwitchAlphabet(val alphabetIndex: Int) : MainEvent()
    data class EnteredAnswer(val text: String) : MainEvent()
    data class SelectSymbol(val checked: Boolean, val index: Int) : MainEvent()
    data class SelectSymbolsGroup(val checked: Boolean, val symbolsIds: List<Int>) : MainEvent()
    data class SetSelectedSymbols(val symbolsIds: List<Int>) : MainEvent()
    data class SetSheetCollapsedState(val state: Boolean) : MainEvent()
    data class SwitchImeVisibility(val state: Boolean) : MainEvent()
    data class SetShakeState(val state: Boolean) : MainEvent()
    object SkipSymbol : MainEvent()
    object CheckAnswer : MainEvent()
    object SaveSelected : MainEvent()
}
