package com.betaron.kanacard.ui.main.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current
) {
    var scaledTextStyle by remember {
        mutableStateOf(style.copy(fontSize = 0.sp))
    }
    var fontSize by remember { mutableStateOf(0.sp) }

    val localDensity = LocalDensity.current

    Text(
        text = text,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = false,
        style = scaledTextStyle,
        onTextLayout = { result ->
            val length =
                if (result.layoutInput.text.isNotEmpty()) result.layoutInput.text.length
                else 1
            val scaleByHeight = result.layoutInput.constraints.maxHeight / 2
            val scaleByWidth = result.layoutInput.constraints.maxWidth / length

            val resultSize = with(localDensity) {
                if (scaleByHeight < scaleByWidth)
                    scaleByHeight.toSp()
                else
                    scaleByWidth.toSp()
            }

            if (resultSize != fontSize) {
                fontSize = resultSize
            }

            scaledTextStyle = scaledTextStyle.copy(
                fontSize = fontSize
            )
        },
    )
}
