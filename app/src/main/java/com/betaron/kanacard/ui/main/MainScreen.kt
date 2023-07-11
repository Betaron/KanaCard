package com.betaron.kanacard.ui.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.positionInRoot
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
    val displayHeight = LocalContext.current.resources.displayMetrics.heightPixels
    val localDensity = LocalDensity.current
    val scaffoldState = rememberBottomSheetScaffoldState()
    val state = viewModel.state.value
    val alphabets = listOf(
        "Hiragana",
        "Katakana"
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = answerRowHeight + 48.dp,
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
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomSheetContentHeight),
                contentAlignment = TopCenter
            ) {
                if (scaffoldState.bottomSheetState.currentValue != SheetValue.Expanded) {
                    AnswerSection(
                        context = context,
                        viewModel = viewModel,
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                answerRowHeight = with(localDensity) {
                                    coordinates.size.height.toDp()
                                }
                                Log.i("answerRowHeight", answerRowHeight.toString())
                            }
                    )
                } else {
                    Text(
                        text = "aaa",
                        Modifier.padding(128.dp)
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
                    .onGloballyPositioned { coordinates ->
                        bottomSheetContentHeight = with(localDensity) {
                            (displayHeight - coordinates.positionInRoot().y).toDp() - 24.dp
                        }
                    }
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
