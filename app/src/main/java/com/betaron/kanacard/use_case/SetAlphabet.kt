package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.betaron.kanacard.extensions.toEnum
import com.betaron.kanacard.application.Alphabet

class SetAlphabet (
    private val preferencesRepository : PreferencesRepository
) {
    suspend operator fun  invoke(alphabetIndex : Int) {
        val alphabet = alphabetIndex.toEnum<Alphabet>() ?: Alphabet.hiragana;
        preferencesRepository.setAlphabet(alphabet)
    }
}
