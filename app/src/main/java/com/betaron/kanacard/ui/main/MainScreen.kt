package com.betaron.kanacard.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.ui.InsetsManager
import com.betaron.kanacard.ui.main.components.AnswerSection
import com.betaron.kanacard.ui.main.components.AutoSizeText
import com.betaron.kanacard.ui.main.components.SegmentedButton
import com.betaron.kanacard.ui.main.components.SymbolsTable
import com.betaron.kanacard.ui.theme.notoSerifJpRegular
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    insetsManager: InsetsManager
) {
    var contentHeight by remember { mutableStateOf(0.dp) }
    var scaffoldCollapsed by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val localDensity = LocalDensity.current
    val localView = LocalView.current
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )
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

    insetsManager.setUiWindowInsets(viewModel)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = with(localDensity) {
                        WindowInsets.systemBars
                            .getTop(localDensity)
                            .toDp()
                    })
                    .fillMaxWidth(),
                contentAlignment = Center
            ) {
                SegmentedButton(
                    items = alphabets,
                    defaultSelectedItemIndex = state.alphabet,
                    onItemSelection = {
                        viewModel.onEvent(MainEvent.SwitchAlphabet(it))
                    }
                )
            }
        },
        sheetContent = {
            scaffoldCollapsed = scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden
            LaunchedEffect(scaffoldCollapsed) {
                viewModel.onEvent(MainEvent.SaveSelected)
            }

            Box(
                modifier = Modifier
                    .height(contentHeight)
                    .padding(start = 12.dp, end = 12.dp),
                contentAlignment = TopCenter
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth(),
                        contentAlignment = CenterEnd
                    )
                    {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.hide()
                                }
                            },
                        ) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Hide")
                        }
                    }

                    SymbolsTable(
                        ids = intArrayOf(
                            *((1..35).toList().toIntArray()),
                            36, 0, 37, 0, 38,
                            *((39..43).toList().toIntArray()),
                            44, 0, 0, 0, 45,
                            0, 0, 0, 0, 46
                        ),
                        viewModel = viewModel
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        contentHeight = with(localDensity) {
                            coordinates.size.height.toDp() -
                                    systemBarsPaddings.calculateTopPadding()
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = TopEnd
                )
                {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
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

                    IconButton(
                        modifier = Modifier
                            .padding(48.dp),
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DynamicFeed,
                            contentDescription = "Expand",
                        )
                    }
                }

                LaunchedEffect(state.imeIsFullyState) {
                    if (!state.imeIsFullyState) {
                        localView.clearFocus()
                    }
                }

                AnswerSection(
                    viewModel = viewModel,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(bottom = with(localDensity) {
                            WindowInsets.systemBars
                                .getBottom(localDensity)
                                .toDp()
                        })
                        .imePadding()
                )
            }
        }
    }
}
