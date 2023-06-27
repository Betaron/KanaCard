package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository

class GetLastSymbol(
    private val preferencesRepository : PreferencesRepository
) {
    suspend operator fun invoke() : Int {
        return  preferencesRepository.getLastSymbol()
    }
}