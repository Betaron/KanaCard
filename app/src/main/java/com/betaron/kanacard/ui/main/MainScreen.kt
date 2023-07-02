package com.betaron.kanacard.ui.main

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.ui.main.components.SegmentedButton

@OptIn(ExperimentalMaterial3Api::class)
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
        Row {
            Button(
                onClick = {
                    viewModel.onEvent(MainEvent.PickNewSymbol)
                }) {
                Text(text = "skip")
            }
            OutlinedTextField(
                value = viewModel.state.value.answer,
                onValueChange = {
                    viewModel.onEvent(MainEvent.EnteredAnswer(it))
                },
                label = { Text("Answer") },
                singleLine = true
            )
        }
    }
}
