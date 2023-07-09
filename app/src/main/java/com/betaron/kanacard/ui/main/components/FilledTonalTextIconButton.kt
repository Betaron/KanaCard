package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                .padding(bottom = 8.dp)
                .size(50.dp),
            onClick = onClick
        ) {
            Icon(
                imageVector,
                contentDescription = iconContentDescription,
            )
        }
        Text(
            modifier = Modifier
                .width(64.dp),
            text = text,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}