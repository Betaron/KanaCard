package com.betaron.kanacard.data.serializer

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.betaron.kanacard.Constants
import com.betaron.kanacard.application.Preferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object  PreferencesSerializer : Serializer<Preferences> {
    override val defaultValue: Preferences = Preferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Preferences {
        try {
            return Preferences.parseFrom(input)
        } catch (exception : InvalidProtocolBufferException){
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) =
        t.writeTo(output)
}

private val Context.dataStore  : DataStore<Preferences>  by  dataStore(
    fileName = Constants.DATA_STORE_FILE_NAME,
    serializer = PreferencesSerializer
)