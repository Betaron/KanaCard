package com.betaron.kanacard.data.repository.implementations

import android.util.Log
import androidx.datastore.core.DataStore
import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.kanacard.application.Alphabet
import com.kanacard.application.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {
    private val preferencesFlow: Flow<Preferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("PrefsRepo", "Error reading sort order preferences.", exception)
                emit(Preferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun switchAlphabet() {
        val newVal = if (getCurrentAlphabet() == Alphabet.hiragana)
            Alphabet.katakana else Alphabet.hiragana
        dataStore.updateData { preferences ->
            preferences.toBuilder().setAlphabet(newVal).build()
        }
    }

    override suspend fun getCurrentAlphabet(): Alphabet {
        return preferencesFlow.first().alphabet
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