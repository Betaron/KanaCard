package com.betaron.kanacard.data.repository.interfaces

import com.kanacard.application.Alphabet

interface PreferencesRepository {
    suspend fun switchAlphabet()
    suspend fun getCurrentAlphabet() : Alphabet
    fun getSelected() : List<Boolean>
    fun setSelected(indexes : List<Boolean>)
    fun getLastSymbol() : Int
    fun setLastSymbol(index: Int)
}