package com.betaron.kanacard.ui.main

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.KeyboardTab
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.ui.main.components.AutoSizeText
import com.betaron.kanacard.ui.main.components.FilledTonalTextIconButton
import com.betaron.kanacard.ui.main.components.SegmentedButton
import com.betaron.kanacard.ui.theme.notoSerifJpRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val alphabets = listOf(
        "Hiragana",
        "Katakana"
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Center
            ) {
                SegmentedButton(
                    modifier = Modifier
                        .padding(16.dp),
                    items = alphabets,
                    defaultSelectedItemIndex = state.alphabet,
                    onItemSelection = {
                        viewModel.onEvent(MainEvent.SwitchAlphabet(it))
                    }
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    FilledTonalTextIconButton(
                        modifier = Modifier
                            .padding(top = 4.dp, end = 32.dp, bottom = 8.dp),
                        text = "Skip",
                        imageVector = Icons.Outlined.KeyboardDoubleArrowRight,
                        iconContentDescription = "Skip",
                        onClick = {
                            viewModel.onEvent(MainEvent.SkipSymbol)
                        }
                    )

                    OutlinedTextField(
                        value = viewModel.state.value.answer,
                        onValueChange = {
                            viewModel.onEvent(MainEvent.EnteredAnswer(it, context))
                        },
                        label = { Text(text = "Answer") },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 18.sp
                        ),
                        modifier = Modifier
                            .width(150.dp),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.onEvent(MainEvent.CheckAnswer)
                        }),
                        supportingText = {
                            if (viewModel.state.value.answerInputError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Incorrect language",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            if (viewModel.state.value.answerInputError)
                                Icon(
                                    Icons.Outlined.Error,
                                    "error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                        }
                    )

                    FilledTonalTextIconButton(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 32.dp, bottom = 8.dp),
                        text = "Check",
                        imageVector = Icons.Outlined.KeyboardTab,
                        iconContentDescription = "Check",
                        onClick = {
                            viewModel.onEvent(MainEvent.CheckAnswer)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            contentAlignment = Center
        ) {
            Card(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Center
                ) {
                    AutoSizeText(
                        text = state.alphabetSymbols[state.currentSymbolIndex],
                        style = LocalTextStyle.current.merge(
                            TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                ),
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.Both
                                ),
                                letterSpacing = (-24).sp
                            )
                        ),
                        fontFamily = notoSerifJpRegular
                    )
                }
            }
        }
    }
}
