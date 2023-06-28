package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository

class SetLastSymbol(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(symbolIndex: Int) {
        preferencesRepository.setLastSymbol(symbolIndex)
    }
}