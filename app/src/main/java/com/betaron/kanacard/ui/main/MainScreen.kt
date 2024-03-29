package com.betaron.kanacard.ui.main

import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringArrayResource
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
import com.betaron.kanacard.ui.main.components.CardFace
import com.betaron.kanacard.ui.main.components.FlipCard
import com.betaron.kanacard.ui.main.components.RotationAxis
import com.betaron.kanacard.ui.main.components.SegmentedButton
import com.betaron.kanacard.ui.main.components.SymbolsTable
import com.betaron.kanacard.ui.main.components.TableHeader
import com.betaron.kanacard.ui.theme.notoSerifJpRegular
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(), insetsManager: InsetsManager
) {
    var contentHeight by remember { mutableStateOf(0.dp) }
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
    val transcriptions = stringArrayResource(R.array.transcription)
    val alphabets = listOf(
        stringResource(R.string.hira), stringResource(R.string.kata)
    )

    val empty = remember { listOf(0, 37, 39, 47, 48, 49, 51, 52, 53, 54) }
    val monographs = remember { (1..55).toList() }
    val monographsWithDiacritics = remember { (56..80).toList() }
    val digraphs = remember { (81..101).toList() }
    val digraphsWithDiacritics = remember { (102..116).toList() }

    val systemBarsPaddings = with(localDensity) {
        PaddingValues(
            top = WindowInsets.systemBars.getTop(localDensity).toDp(),
            bottom = WindowInsets.systemBars.getBottom(localDensity).toDp()
        )
    }

    val shake = animateDpAsState(
        targetValue = if (state.isCardShake) 0.dp else 0.1.dp,
        animationSpec = repeatable(iterations = 8,
            repeatMode = RepeatMode.Reverse,
            animation = keyframes {
                durationMillis = 30
                (-16).dp at 10
                16.dp at 20
            }),
        label = "Shake"
    )

    var flipState by remember {
        mutableStateOf(CardFace.Front)
    }

    insetsManager.setUiWindowInsets(viewModel)

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = false,

        sheetDragHandle = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
            ) {
                BottomSheetDefaults.DragHandle(
                    modifier = Modifier.align(CenterHorizontally)
                )
            }
        },
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = with(localDensity) {
                        WindowInsets.systemBars
                            .getTop(localDensity)
                            .toDp()
                    })
                    .fillMaxWidth(), contentAlignment = Center
            ) {
                SegmentedButton(items = alphabets,
                    defaultSelectedItemIndex = state.alphabet,
                    onItemSelection = {
                        viewModel.onEvent(MainEvent.SwitchAlphabet(it))
                    })
            }
        },
        sheetContent = {
            viewModel.onEvent(
                MainEvent.SetSheetCollapsedState(
                    scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden
                )
            )
            LaunchedEffect(state.isScaffoldCollapsed) {
                if (state.isScaffoldCollapsed) viewModel.onEvent(MainEvent.SaveSelected)
            }

            Box(
                modifier = Modifier
                    .height(contentHeight)
                    .padding(start = 12.dp, end = 12.dp),
                contentAlignment = TopCenter
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    TableHeader(
                        modifier = Modifier.align(CenterHorizontally),
                        title = stringResource(R.string.monographs),
                        tableItemsIds = monographs - empty.toSet(),
                        viewModel = viewModel
                    )

                    SymbolsTable(
                        modifier = Modifier.height(550.dp),
                        ids = monographs,
                        spaces = empty,
                        columns = 5,
                        viewModel = viewModel
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(8.dp)
                    )

                    TableHeader(
                        modifier = Modifier.align(CenterHorizontally),
                        title = stringResource(R.string.monographs_with_diacritics),
                        tableItemsIds = monographsWithDiacritics,
                        viewModel = viewModel
                    )

                    SymbolsTable(
                        modifier = Modifier.height(250.dp),
                        ids = monographsWithDiacritics,
                        columns = 5,
                        viewModel = viewModel
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(8.dp)
                    )

                    TableHeader(
                        modifier = Modifier.align(CenterHorizontally),
                        title = stringResource(R.string.digraphs),
                        tableItemsIds = digraphs,
                        viewModel = viewModel
                    )

                    SymbolsTable(
                        modifier = Modifier.height(350.dp),
                        ids = digraphs,
                        columns = 3,
                        viewModel = viewModel
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(8.dp)
                    )

                    TableHeader(
                        modifier = Modifier.align(CenterHorizontally),
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
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.onGloballyPositioned { coordinates ->
                contentHeight = with(localDensity) {
                    coordinates.size.height.toDp() - systemBarsPaddings.calculateTopPadding()
                }
            }) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    AnimatedContent(
                        targetState = state.currentSymbolIndex, transitionSpec = {
                            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                        }, label = "CardSlide"
                    ) { targetSymbolIndex ->
                        FlipCard(modifier = Modifier
                            .offset(x = shake.value)
                            .fillMaxSize()
                            .padding(24.dp), cardFace = flipState, onClick = {
                            localView.playSoundEffect(SoundEffectConstants.CLICK)
                            flipState = it.next
                        }, axis = RotationAxis.AxisY, front = {
                            Box(contentAlignment = TopEnd) {
                                Box(
                                    modifier = Modifier.fillMaxSize(), contentAlignment = Center
                                ) {
                                    AnimatedContent(
                                        targetState = state.alphabet, label = "SoftSwitch"
                                    ) { targetAlphabet ->
                                        AutoSizeText(
                                            text = state.alphabetSymbols[targetAlphabet][targetSymbolIndex],
                                            style = LocalTextStyle.current.merge(
                                                TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false
                                                    ), lineHeightStyle = LineHeightStyle(
                                                        alignment = LineHeightStyle.Alignment.Center,
                                                        trim = LineHeightStyle.Trim.Both
                                                    ), letterSpacing = (-24).sp
                                                )
                                            ),
                                            fontFamily = notoSerifJpRegular
                                        )
                                    }
                                }
                                IconButton(modifier = Modifier.padding(24.dp), onClick = {
                                    localView.playSoundEffect(SoundEffectConstants.CLICK)
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                    keyboardController?.hide()
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.DynamicFeed,
                                        contentDescription = "Expand",
                                    )
                                }
                            }
                        }, back = {
                            Box(
                                modifier = Modifier.fillMaxSize(), contentAlignment = Center
                            ) {
                                AutoSizeText(
                                    text = "[" + transcriptions[targetSymbolIndex] + "]",
                                    style = LocalTextStyle.current.merge(
                                        TextStyle(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false
                                            ), lineHeightStyle = LineHeightStyle(
                                                alignment = LineHeightStyle.Alignment.Center,
                                                trim = LineHeightStyle.Trim.Both
                                            )
                                        )
                                    )
                                )
                            }
                        })
                    }
                }

                LaunchedEffect(state.imeIsFullyState) {
                    if (!state.imeIsFullyState) {
                        localView.clearFocus()
                    }
                }

                AnswerSection(viewModel = viewModel,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(bottom = with(localDensity) {
                            WindowInsets.systemBars
                                .getBottom(localDensity)
                                .toDp()
                        })
                        .imePadding(),
                    onSkipClick = {
                        localView.playSoundEffect(SoundEffectConstants.CLICK)
                        viewModel.onEvent(MainEvent.SkipSymbol)
                        flipState = CardFace.Front
                    },
                    onCheckClick = {
                        localView.playSoundEffect(SoundEffectConstants.CLICK)
                        viewModel.onEvent(MainEvent.CheckAnswer)
                        flipState = CardFace.Front
                    })
            }
        }
    }
}
