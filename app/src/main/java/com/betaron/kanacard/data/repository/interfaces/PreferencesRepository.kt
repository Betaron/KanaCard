package com.betaron.kanacard.data.repository.interfaces

import com.betaron.kanacard.enums.Alphabet

interface PreferencesRepository {
    fun switchAlphabet()
    fun getCurrentAlphabet() : Alphabet
    fun getSelected() : List<Boolean>
    fun setSelected(indexes : List<Boolean>)
    fun getLastSymbol() : Int
    fun setLastSymbol(index: Int)
}