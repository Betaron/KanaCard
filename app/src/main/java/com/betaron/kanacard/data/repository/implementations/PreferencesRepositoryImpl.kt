package com.betaron.kanacard.data.repository.implementations

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.betaron.kanacard.enums.Alphabet

class PreferencesRepositoryImpl : PreferencesRepository {
    override fun switchAlphabet() {
        TODO("Not yet implemented")
    }

    override fun getCurrentAlphabet(): Alphabet {
        TODO("Not yet implemented")
    }

    override fun getSelected(): List<Boolean> {
        TODO("Not yet implemented")
    }

    override fun setSelected(indexes: List<Boolean>) {
        TODO("Not yet implemented")
    }

    override fun getLastSymbol(): Int {
        TODO("Not yet implemented")
    }

    override fun setLastSymbol(index: Int) {
        TODO("Not yet implemented")
    }

}