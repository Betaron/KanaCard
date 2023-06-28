package com.betaron.kanacard.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.ui.main.components.SegmentedButton

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val alphabets = listOf(
        "Hiragana",
        "Katakana"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        SegmentedButton(
            items = alphabets,
            defaultSelectedItemIndex = state.alphabet,
            onItemSelection = {
                viewModel.onEvent(MainEvent.SwitchAlphabet(it))
            }
        )
        Card {
            Text(text = state.alphabetSymbols[state.currentSymbolIndex])
        }
        Button(
            onClick = {
                viewModel.onEvent(MainEvent.PickNewSymbol)
        }) {
            Text(text = "random")
        }
    }
}
