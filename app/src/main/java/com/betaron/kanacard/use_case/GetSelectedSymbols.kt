package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository

class GetSelectedSymbols(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): List<Int> {
        var selectedSymbols = preferencesRepository.getSelected()

        if (selectedSymbols.isEmpty()) {
            selectedSymbols = (1..5).toList()
            preferencesRepository.setSelected(selectedSymbols)
        }

        return selectedSymbols
    }
}
