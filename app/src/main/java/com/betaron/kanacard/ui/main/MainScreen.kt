package com.betaron.kanacard.ui.main

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.ui.main.components.AnswerSection
import com.betaron.kanacard.ui.main.components.AutoSizeText
import com.betaron.kanacard.ui.main.components.SegmentedButton
import com.betaron.kanacard.ui.theme.notoSerifJpRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel()
) {
    var answerRowHeight by remember { mutableStateOf(0.dp) }
    var bottomSheetContentHeight by remember { mutableStateOf(0.dp) }

    val scaffoldDragHandleHeight = 24.dp
    val localDensity = LocalDensity.current
    val scaffoldState = rememberBottomSheetScaffoldState()
    val state = viewModel.state.value
    val alphabets = listOf(
        "Hiragana",
        "Katakana"
    )

    val systemBarsPaddings = with(localDensity) {
        PaddingValues(
            top = WindowInsets.systemBars.getTop(localDensity).toDp(),
            bottom = WindowInsets.systemBars.getBottom(localDensity).toDp()
        )
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = answerRowHeight +
                scaffoldDragHandleHeight +
                systemBarsPaddings.calculateBottomPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = systemBarsPaddings.calculateTopPadding())
                    .fillMaxWidth(),
                contentAlignment = Center
            ) {
                SegmentedButton(
                    modifier = Modifier
                        .padding(8.dp),
                    items = alphabets,
                    defaultSelectedItemIndex = state.alphabet,
                    onItemSelection = {
                        viewModel.onEvent(MainEvent.SwitchAlphabet(it))
                    }
                )
            }
        },
        sheetContent = {
            Box(
                modifier = Modifier
                    .height(bottomSheetContentHeight + answerRowHeight)
                    .fillMaxWidth()
            ) {
                Crossfade(targetState = scaffoldState.bottomSheetState.targetValue) { sheetValue ->
                    when (sheetValue) {
                        SheetValue.PartiallyExpanded ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = TopCenter
                            ) {
                                AnswerSection(
                                    context = context,
                                    viewModel = viewModel,
                                    modifier = Modifier
                                        .onGloballyPositioned { coordinates ->
                                            answerRowHeight = with(localDensity) {
                                                coordinates.size.height.toDp()
                                            }
                                        }
                                        .padding(bottom = 32.dp)
                                )
                            }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = TopCenter
                            ) {
                                Text(text = "aaa")
                            }
                        }
                    }
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
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        bottomSheetContentHeight = with(localDensity) {
                            coordinates.size.height.toDp()
                        }
                    }
                    .padding(32.dp)
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
