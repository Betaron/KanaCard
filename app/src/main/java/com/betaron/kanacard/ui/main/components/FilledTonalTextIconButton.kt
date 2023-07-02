package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FilledTonalTextIconButton(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    iconContentDescription: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalIconButton(
            modifier = Modifier
                .padding(bottom = 4.dp),
            onClick = onClick
        ) {
            Icon(imageVector, contentDescription = iconContentDescription)
        }
        Text(text = text)
    }
}