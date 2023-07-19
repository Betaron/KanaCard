package com.betaron.kanacard.ui

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel

class InsetsManager(
    private val view: View
) {
    var imeVisible: Boolean = false
    var imeHeight: Int = 0

    fun setUiWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            imeVisible = windowInsets.isVisible(WindowInsetsCompat.Type.ime())
            imeHeight = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            windowInsets
        }
    }

    fun animateKeyboardDisplay() {
        val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(
                insets: WindowInsets,
                runningAnimations: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                imeHeight = insets.getInsets(
                    WindowInsetsCompat.Type.ime()
                ).bottom

                view.updatePadding(bottom = imeHeight)

                return insets
            }

            override fun onEnd(animation: WindowInsetsAnimation) {
                if (!imeVisible)
                    view.clearFocus()
            }
        }
        view.setWindowInsetsAnimationCallback(cb)
    }
}