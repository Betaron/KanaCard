package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository

class SetSelectedSymbols(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(symbolsIndexes: List<Int>) {
        preferencesRepository.setSelected(symbolsIndexes)
    }
}