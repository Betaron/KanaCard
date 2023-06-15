package com.betaron.kanacard.data.model

import androidx.datastore.core.Serializer
import com.betaron.kanacard.enums.Alphabet

data class Preferences(
    var alphabet: Alphabet,
    var selected: List<Boolean>,
    var lastSymbolIndex: Int
)