package com.betaron.kanacard.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.betaron.kanacard.Constants.DATA_STORE_FILE_NAME
import com.betaron.kanacard.data.repository.implementations.PreferencesRepositoryImpl
import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.betaron.kanacard.data.serializer.PreferencesSerializer
import com.kanacard.application.Preferences
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
    fun providePreferencesDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return  DataStoreFactory.create(
            serializer = PreferencesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME)},
            corruptionHandler = null
        )
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(dataStore: DataStore<Preferences>) : PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }
}