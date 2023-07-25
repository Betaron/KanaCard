package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel

@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    tableItemsIds: List<Int> = listOf(),
    viewModel: MainViewModel
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = title
        )
        Spacer(modifier = Modifier.weight(1f))
        IconToggleButton(
            onCheckedChange = {
                viewModel.onEvent(MainEvent.SelectSymbolsGroup(it, tableItemsIds))
            },
            checked = viewModel.state.value.selectedSymbols.containsAll(tableItemsIds)
        ) {
            Icon(
                imageVector =
                if (viewModel.state.value.selectedSymbols.containsAll(tableItemsIds))
                    Icons.Filled.CheckCircle
                else
                    Icons.Outlined.CheckCircle,
                contentDescription = "SelectGroup"
            )
        }
    }
}