package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect
import com.betaron.kanacard.R
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel

@Composable
fun SymbolsTable(
    modifier: Modifier = Modifier,
    ids: List<Int>,
    spaces: List<Int> = listOf(),
    columns: Int,
    viewModel: MainViewModel
) {
    val state = rememberLazyGridState()
    val symbols = viewModel.state.value.alphabetSymbols
    val transcriptions = stringArrayResource(R.array.transcription)
    val selected = viewModel.state.value.selectedSymbols
    val haptic = LocalHapticFeedback.current

    fun LazyGridState.gridItemKeyAtPosition(hitPoint: Offset): Int? =
        layoutInfo.visibleItemsInfo.find { itemInfo ->
            itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
        }?.key as? Int

    fun Modifier.gridDragHandler(
        lazyGridState: LazyGridState
    ) = pointerInput(Unit) {
        var initialKey: Int? = null
        var currentKey: Int? = null
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                lazyGridState.gridItemKeyAtPosition(offset)?.let { key ->
                    initialKey = key
                    currentKey = key
                    viewModel.onEvent(MainEvent.SelectSymbol(true, key))
                }
            },
            onDragCancel = { initialKey = null },
            onDragEnd = { initialKey = null },
            onDrag = { change, _ ->
                if (initialKey != null) {
                    lazyGridState.gridItemKeyAtPosition(change.position)?.let { key ->
                        if (currentKey != key) {
                            viewModel.onEvent(
                                MainEvent.SetSelectedSymbols(
                                    viewModel.state.value.selectedSymbols
                                        .asSequence()
                                        .minus(initialKey!!..currentKey!!)
                                        .minus(currentKey!!..initialKey!!)
                                        .plus(initialKey!!..key)
                                        .plus(key..initialKey!!)
                                        .minus(spaces.toSet())
                                        .distinct()
                                        .toList()
                                )
                            )
                            currentKey = key
                        }
                    }
                }
            }
        )
    }

    LazyVerticalGrid(
        modifier = modifier
            .gridDragHandler(state),
        state = state,
        columns = GridCells.Fixed(columns),
        userScrollEnabled = false
    ) {
        items(
            ids,
            key = { it },
            contentType = { Int }
        ) { id ->
            ToggleButton(
                modifier = Modifier
                    .padding(8.dp, top = 0.dp, bottom = 2.dp),
                symbol = symbols[viewModel.state.value.alphabet][id],
                transcription = transcriptions[id],
                id = id,
                checked = id in selected,
                viewModel = viewModel,
                isStub = id in spaces
            )
        }
    }
}
