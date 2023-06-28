package com.betaron.kanacard.ui

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
}