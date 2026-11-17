package com.example.geoquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val QuizSaver = listSaver<QuizState, Int>(
    save = { listOf(it.index) + it.answers },
    restore = { QuizState(index = it.first(), answers = it.drop(1)) }
)

@Composable
fun GeoQuizApp() {
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF6200EE))) {
        var state by rememberSaveable(stateSaver = QuizSaver) { mutableStateOf(QuizState()) }
        var showResults by rememberSaveable { mutableStateOf(false) }
        val onAnswer: (Boolean) -> Unit = { selection ->
            if (!state.answered) {
                state = state.answer(selection)
                if (state.finished) showResults = true
            }
        }
        Scaffold(
            topBar = {
                Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
                    .windowInsetsPadding(WindowInsets.statusBars).height(56.dp).padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart) {
                    Text(stringResource(R.string.app_name), color = Color.White, fontSize = 20.sp)
                }
            }
        ) { insets ->
            Column(Modifier.fillMaxSize().padding(insets).verticalScroll(rememberScrollState())
                .padding(16.dp)) {
                Text(questions[state.index].text, Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, fontSize = 14.sp)
                Spacer(Modifier.height(16.dp))
                Box(Modifier.fillMaxWidth().height(48.dp)) {
                    if (!state.answered) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = { onAnswer(true) }, shape = RoundedCornerShape(4.dp)) {
                                Text(stringResource(R.string.answer_true))
                            }
                            Button(onClick = { onAnswer(false) }, shape = RoundedCornerShape(4.dp)) {
                                Text(stringResource(R.string.answer_false))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                if (!state.finished) {
                    Button(onClick = { state = state.next() }, enabled = state.answered,
                        modifier = Modifier.align(Alignment.End), shape = RoundedCornerShape(4.dp)) {
                        Text(stringResource(R.string.next))
                    }
                } else if (!showResults) {
                    Text(stringResource(R.string.result_score, state.score, questions.size),
                        Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }
        if (showResults && state.finished) {
            AlertDialog(
                onDismissRequest = { showResults = false },
                title = { Text(stringResource(R.string.results_title)) },
                text = { Text(stringResource(R.string.result_score, state.score, questions.size)) },
                confirmButton = {
                    TextButton(onClick = { showResults = false }) { Text(stringResource(R.string.ok)) }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeoQuizPreview() { GeoQuizApp() }
