package com.betaron.kanacard.ui.main.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.KeyboardTab
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaron.kanacard.R
import com.betaron.kanacard.ui.main.MainEvent
import com.betaron.kanacard.ui.main.MainViewModel

@Composable
fun AnswerSection(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        FilledTonalTextIconButton(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp),
            text = stringResource(R.string.skip),
            imageVector = Icons.Outlined.KeyboardDoubleArrowRight,
            iconContentDescription = "Skip",
            onClick = {
                viewModel.onEvent(MainEvent.SkipSymbol)
            }
        )

        Spacer(modifier = Modifier.width(32.dp))

        OutlinedTextField(
            value = viewModel.state.value.answer,
            onValueChange = {
                viewModel.onEvent(MainEvent.EnteredAnswer(it))
            },
            label = { Text(text = stringResource(R.string.answer)) },
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
                        text = stringResource(R.string.input_error),
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

        Spacer(modifier = Modifier.width(32.dp))

        FilledTonalTextIconButton(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp),
            text = stringResource(R.string.check),
            imageVector = Icons.Outlined.KeyboardTab,
            iconContentDescription = "Check",
            onClick = {
                viewModel.onEvent(MainEvent.CheckAnswer)
            }
        )
    }
}