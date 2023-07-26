package com.betaron.kanacard.ui.main

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.R
import com.betaron.kanacard.ui.InsetsManager
import com.betaron.kanacard.ui.main.components.AnswerSection
import com.betaron.kanacard.ui.main.components.AutoSizeText
import com.betaron.kanacard.ui.main.components.SegmentedButton
import com.betaron.kanacard.ui.main.components.SymbolsTable
import com.betaron.kanacard.ui.main.components.TableHeader
import com.betaron.kanacard.ui.theme.notoSerifJpRegular
import kotlinx.coroutines.delay
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
        stringResource(R.string.hira),
        stringResource(R.string.kata)
    )

    val monographs = remember {
        (1..35).toList() +
                listOf(36, 0, 37, 0, 38) +
                (39..43).toList() +
                listOf(44, 0, 0, 0, 45) +
                listOf(0, 0, 0, 0, 46)
    }

    val monographsWithDiacritics = remember {
        (47..71).toList()
    }

    val digraphs = remember {
        (72..92).toList()
    }

    val digraphsWithDiacritics = remember {
        (93..107).toList()
    }

    val systemBarsPaddings = with(localDensity) {
        PaddingValues(
            top = WindowInsets.systemBars.getTop(localDensity).toDp(),
            bottom = WindowInsets.systemBars.getBottom(localDensity).toDp()
        )
    }

    fun Modifier.shake(enabled: Boolean) = composed(
        factory = {
            val infiniteTransition = rememberInfiniteTransition()
            val shake by infiniteTransition.animateFloat(
                initialValue = -16f,
                targetValue = 16f,
                animationSpec = infiniteRepeatable(
                    animation = tween(30, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Modifier.offset(
                x = if (enabled) shake.dp else 0.dp
            )
        },
        inspectorInfo = debugInspectorInfo {
            name = "shake"
            properties["enabled"] = enabled
        }
    )

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

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        TableHeader(
                            modifier = Modifier
                                .align(CenterHorizontally),
                            title = stringResource(R.string.monographs),
                            tableItemsIds = monographs.distinct() - 0,
                            viewModel = viewModel
                        )

                        SymbolsTable(
                            modifier = Modifier
                                .height(550.dp),
                            ids = monographs,
                            columns = 5,
                            viewModel = viewModel
                        )

                        Divider(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(8.dp)
                        )

                        TableHeader(
                            modifier = Modifier
                                .align(CenterHorizontally),
                            title = stringResource(R.string.monographs_with_diacritics),
                            tableItemsIds = monographsWithDiacritics,
                            viewModel = viewModel
                        )

                        SymbolsTable(
                            modifier = Modifier
                                .height(250.dp),
                            ids = monographsWithDiacritics,
                            columns = 5,
                            viewModel = viewModel
                        )

                        Divider(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(8.dp)
                        )

                        TableHeader(
                            modifier = Modifier
                                .align(CenterHorizontally),
                            title = stringResource(R.string.digraphs),
                            tableItemsIds = digraphs,
                            viewModel = viewModel
                        )

                        SymbolsTable(
                            modifier = Modifier
                                .height(350.dp),
                            ids = digraphs,
                            columns = 3,
                            viewModel = viewModel
                        )

                        Divider(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(8.dp)
                        )

                        TableHeader(
                            modifier = Modifier
                                .align(CenterHorizontally),
                            title = stringResource(R.string.digraphs_with_diacritics),
                            tableItemsIds = digraphsWithDiacritics,
                            viewModel = viewModel
                        )

                        SymbolsTable(
                            modifier = Modifier
                                .padding(bottom = systemBarsPaddings.calculateBottomPadding())
                                .height(250.dp),
                            ids = digraphsWithDiacritics,
                            columns = 3,
                            viewModel = viewModel
                        )
                    }
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
                LaunchedEffect(state.isCardShake) {
                    if (state.isCardShake) {
                        delay(300)
                        viewModel.onEvent(MainEvent.SetShakeState(false))
                    }
                }

                Card(
                    modifier = Modifier
                        .shake(state.isCardShake)
                        .weight(1f)
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Box(contentAlignment = TopEnd)
                    {
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
                        IconButton(
                            modifier = Modifier
                                .padding(24.dp),
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
                        .imePadding(),
                    onSkipClick = {
                        viewModel.onEvent(MainEvent.SkipSymbol)
                    },
                    onCheckClick = {
                        viewModel.onEvent(MainEvent.CheckAnswer)
                    }
                )
            }
        }
    }
}
