package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.betaron.kanacard.R
import com.betaron.kanacard.ui.main.MainViewModel

@Composable
fun SymbolsTable(
    modifier: Modifier = Modifier,
    ids: IntArray,
    viewModel: MainViewModel
) {
    val symbols = viewModel.state.value.alphabetSymbols
    val transcriptions = stringArrayResource(R.array.transcription)
    val selected = viewModel.state.value.selectedSymbols

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = 5)
    ){
        items(ids.toTypedArray()){ id ->
            ToggleButton(
                modifier = Modifier
                    .padding(8.dp),
                symbol = symbols[id],
                transcription = transcriptions[id],
                id = id,
                checked = id in selected,
                viewModel = viewModel)
        }
    }
}