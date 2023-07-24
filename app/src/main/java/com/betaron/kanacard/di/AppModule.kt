package com.betaron.kanacard.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.betaron.kanacard.Constants.DATA_STORE_FILE_NAME
import com.betaron.kanacard.application.Preferences
import com.betaron.kanacard.data.repository.implementations.PreferencesRepositoryImpl
import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.betaron.kanacard.data.serializer.PreferencesSerializer
import com.betaron.kanacard.use_case.AlphabetUseCases
import com.betaron.kanacard.use_case.CheckAnswer
import com.betaron.kanacard.use_case.GetAlphabet
import com.betaron.kanacard.use_case.GetAlphabetSymbolsSet
import com.betaron.kanacard.use_case.GetLastSymbol
import com.betaron.kanacard.use_case.GetSelectedSymbols
import com.betaron.kanacard.use_case.SelectRandomSymbol
import com.betaron.kanacard.use_case.SetAlphabet
import com.betaron.kanacard.use_case.SetLastSymbol
import com.betaron.kanacard.use_case.SetSelectedSymbols
import com.betaron.kanacard.use_case.UtilUseCases
import com.betaron.kanacard.use_case.ValidateLanguage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return DataStoreFactory.create(
            serializer = PreferencesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = null
        )
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun providesAlphabetUseCases(
        application: Application,
        preferencesRepository: PreferencesRepository
    ): AlphabetUseCases {
        return AlphabetUseCases(
            getAlphabet = GetAlphabet(preferencesRepository),
            setAlphabet = SetAlphabet(preferencesRepository),
            getLastSymbol = GetLastSymbol(preferencesRepository),
            getAlphabetSymbolsSet = GetAlphabetSymbolsSet(application),
            getSelectedSymbols = GetSelectedSymbols(preferencesRepository),
            setSelectedSymbols = SetSelectedSymbols(preferencesRepository),
            setLastSymbol = SetLastSymbol(preferencesRepository),
            selectRandomSymbol = SelectRandomSymbol(),
            checkAnswer = CheckAnswer(application)
        )
    }

    @Provides
    @Singleton
    fun provideUtlUseCases(
        application: Application
    ): UtilUseCases {
        return UtilUseCases(
            validateLanguage = ValidateLanguage(application)
        )
    }
}