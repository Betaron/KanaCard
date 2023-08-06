package com.betaron.kanacard.ui.main.components

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel
import com.betaron.kanacard.ui.theme.notoSerifJpRegular

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    symbol: String,
    transcription: String,
    id: Int,
    viewModel: MainViewModel,
    checked: Boolean = false,
    isStub: Boolean = false
) {
    val localView = LocalView.current
    val rememberSelectSymbol: (checked: Boolean) -> Unit = remember {
        {
            viewModel.onEvent(MainEvent.SelectSymbol(it, id))
            localView.playSoundEffect(SoundEffectConstants.CLICK)
        }
    }

    val alpha = if (isStub) 0f else 1f
    val enabled = !isStub

    val scale by animateFloatAsState(
        targetValue = if (checked) 0.9f else 1f,
        animationSpec = tween(100, easing = LinearEasing),
        label = "Check scale"
    )

    val delay = (id - 1) * 8

    FilledIconToggleButton(
        modifier = modifier
            .scale(scale)
            .alpha(alpha),
        checked = viewModel.state.value.selectedSymbols.contains(id),
        onCheckedChange = rememberSelectSymbol,
        enabled = enabled
    ) {
        Row {
            Box(contentAlignment = Alignment.CenterEnd) {
                AnimatedContent(
                    targetState = symbol,
                    transitionSpec =
                    if (!viewModel.state.value.isScaffoldCollapsed) {
                        {
                            fadeIn(
                                animationSpec = tween(200, delayMillis = delay)
                            ) togetherWith fadeOut(
                                animationSpec = tween(200, delayMillis = delay)
                            )
                        }
                    } else {
                        { EnterTransition.None togetherWith ExitTransition.None }
                    },
                    label = "Alphabet switch",
                ) { targetSymbol ->
                    Text(
                        text = targetSymbol,
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
                        fontSize = 24.sp,
                        textAlign = TextAlign.End
                    )
                }
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