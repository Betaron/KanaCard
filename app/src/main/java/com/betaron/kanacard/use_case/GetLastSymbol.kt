package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository

class GetLastSymbol(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Int {
        val lastSymbol = preferencesRepository.getLastSymbol()
        return if (lastSymbol == 0) 1 else lastSymbol
    }
}