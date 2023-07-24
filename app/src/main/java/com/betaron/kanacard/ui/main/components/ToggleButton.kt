package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel
import com.betaron.kanacard.ui.theme.notoSerifJpRegular

@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    symbol: String,
    transcription: String,
    id: Int,
    viewModel: MainViewModel,
    checked: Boolean = false
) {
    var checkState by remember {
        mutableStateOf(checked)
    }

    val rememberSwitchSymbol: (checked: Boolean) -> Unit = remember {
        {
            checkState = !checkState
            viewModel.onEvent(MainEvent.SwitchSymbol(it, id))
        }
    }

    FilledIconToggleButton(
        modifier = modifier,
        checked = checkState,
        onCheckedChange = rememberSwitchSymbol
    ) {
        Row {
            Box(contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = symbol,
                    style = LocalTextStyle.current.merge(
                        TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both
                            )
                        )
                    ),
                    fontFamily = notoSerifJpRegular,
                    fontSize = 20.sp,
                    textAlign = TextAlign.End
                )
            }
            Box(contentAlignment = Alignment.TopStart) {
                Text(
                    text = transcription,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}