package com.betaron.kanacard.data.repository.interfaces

import com.betaron.kanacard.application.Alphabet

interface PreferencesRepository {
    suspend fun setAlphabet(alphabet: Alphabet)
    suspend fun getAlphabet(): Alphabet
    suspend fun getSelected(): List<Int>
    suspend fun setSelected(indexes: List<Int>)
    suspend fun getLastSymbol(): Int
    suspend fun setLastSymbol(index: Int)
}