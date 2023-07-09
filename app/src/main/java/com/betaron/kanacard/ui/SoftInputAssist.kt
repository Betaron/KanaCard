package com.betaron.kanacard.ui

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins

class SoftInputAssist(private val view: View) {
    fun setUiWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            if (view.marginBottom == 0) {
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    updateMargins(
                        left = insets.left,
                        top = insets.top,
                        right = insets.right,
                        bottom = insets.bottom
                    )
                }
            }

            windowInsets
        }
    }

    fun animateKeyboardDisplay() {
        val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(
                insets: WindowInsets,
                runningAnimations: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                val imeHeight = insets.getInsets(
                    WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.systemBars()
                ).bottom

                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    updateMargins(
                        bottom = imeHeight
                    )
                }

                return insets
            }
        }
        view.setWindowInsetsAnimationCallback(cb)
    }
}