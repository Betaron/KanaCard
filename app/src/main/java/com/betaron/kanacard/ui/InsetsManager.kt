package com.betaron.kanacard.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel
import java.util.Timer
import kotlin.concurrent.schedule


class InsetsManager(
    private val view: View
) {
    private var imeHeight: Int = 0

    fun setUiWindowInsets(viewModel: MainViewModel) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val currentImeHeight = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            if (imeHeight != currentImeHeight) {
                imeHeight = currentImeHeight

                Timer(false).schedule(300) {
                    viewModel.onEvent(MainEvent.SwitchImeVisibility(imeHeight > 0))
                }
            }

            windowInsets
        }
    }
}