package com.betaron.kanacard.use_case

import android.app.Application
import com.betaron.kanacard.R

class ValidateLanguage(
    application: Application
) {
    private val validationExpression =
        Regex(application.resources.getString(R.string.languageExpression))

    operator fun invoke(text: String): Boolean {
        return validationExpression.matches(text)
    }
}