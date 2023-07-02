package com.betaron.kanacard.use_case

import android.app.Application
import com.betaron.kanacard.R
import com.betaron.kanacard.application.Alphabet

class CheckAnswer(
    application: Application
) {
    private val answers: Array<String> = application.resources.getStringArray(R.array.transcription)

    operator fun invoke(answer: String, currentSymbolIndex: Int): Boolean {
        return answer == answers[currentSymbolIndex]
    }
}