package com.betaron.kanacard.data.repository.implementations

import android.util.Log
import androidx.datastore.core.DataStore
import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.betaron.kanacard.application.Alphabet
import com.betaron.kanacard.application.Preferences
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

    override suspend fun getAlphabet(): Alphabet {
        return preferencesFlow.first().alphabet
    }

    override suspend fun setAlphabet(alphabet: Alphabet) {
        dataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .setAlphabet(alphabet)
                .build()
        }
    }

    override suspend fun getSelected(): List<Int> {
        return preferencesFlow.first().selectedList
    }

    override suspend fun setSelected(indexes: List<Int>) {
        dataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .clearSelected()
                .addAllSelected(indexes)
                .build()
        }
    }

    override suspend fun getLastSymbol(): Int {
        return preferencesFlow.first().lastSymbolIndex
    }

    override suspend fun setLastSymbol(index: Int) {
        dataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .setLastSymbolIndex(index)
                .build()
        }
    }
}