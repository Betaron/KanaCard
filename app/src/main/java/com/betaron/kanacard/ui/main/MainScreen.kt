package com.betaron.kanacard.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.ui.main.components.SegmentedButton

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val alphabets = listOf(
        "Hiragana",
        "Katakana"
    )

    SegmentedButton(
        items = alphabets,
        defaultSelectedItemIndex = state.alphabet,
        onItemSelection = {
        viewModel.onEvent(MainEvent.SwitchAlphabet(it))
    })
}
