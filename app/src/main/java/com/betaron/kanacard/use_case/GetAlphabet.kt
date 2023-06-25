package com.betaron.kanacard.use_case

import com.betaron.kanacard.data.repository.interfaces.PreferencesRepository
import com.betaron.kanacard.extensions.toInt

class GetAlphabet(
    private val preferencesRepository: PreferencesRepository
) {
  suspend operator fun invoke() : Int {
      return  preferencesRepository.getCurrentAlphabet().toInt()
  }
}