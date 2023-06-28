package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository

class GetSelectedSymbols(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): List<Int> {
        var selectedSymbols = preferencesRepository.getSelected()

        if (selectedSymbols.isEmpty()) {
            selectedSymbols = listOf(0, 1, 2, 3, 4)
            preferencesRepository.setSelected(selectedSymbols)
        }

        return selectedSymbols
    }
}
