package com.betaron.kanacard.use_case

import android.app.Application
import com.betaron.kanacard.R
import com.betaron.kanacard.application.Alphabet

class GetAlphabetSymbolsSet(
    application: Application
) {
    private val hiragana: Array<String> = application.resources.getStringArray(R.array.hiragana)
    private val katakana: Array<String> = application.resources.getStringArray(R.array.katakana)

    operator fun invoke(alphabetIndex: Int): Array<String> {
        return if (alphabetIndex == Alphabet.hiragana_VALUE) hiragana
        else katakana
    }
}