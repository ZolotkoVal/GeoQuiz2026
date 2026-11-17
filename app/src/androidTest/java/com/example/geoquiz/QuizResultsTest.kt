package com.example.geoquiz

import android.graphics.Bitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class QuizResultsTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun screenshot(name: String) {
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val file = File(instrumentation.targetContext.getExternalFilesDir(null), "$name.png")
        val bitmap = requireNotNull(instrumentation.uiAutomation.takeScreenshot())
        file.outputStream().use { assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) }
        bitmap.recycle()
    }

    private fun finish(answers: List<Boolean>) {
        answers.forEachIndexed { index, answer ->
            compose.onNodeWithText(if (answer) "TRUE" else "FALSE").performClick()
            if (index < 5) compose.onNodeWithText("NEXT ›").performClick()
        }
    }

    @Test fun perfectScoreAppearsInPopupImmediatelyAfterLastAnswer() {
        screenshot("initial")
        finish(listOf(true, true, false, false, true, true))
        compose.onNodeWithText("Quiz results").assertIsDisplayed()
        compose.onNodeWithText("Correct answers: 6 / 6").assertIsDisplayed()
        screenshot("perfect-results")
    }

    @Test fun allWrongAnswersProduceZeroScore() {
        finish(listOf(false, false, true, true, false, false))
        compose.onNodeWithText("Quiz results").assertIsDisplayed()
        compose.onNodeWithText("Correct answers: 0 / 6").assertIsDisplayed()
    }

    @Test fun mixedScoreAndDismissedResultSurviveRecreation() {
        finish(listOf(true, false, false, true, true, false))
        compose.onNodeWithText("Correct answers: 3 / 6").assertIsDisplayed()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Quiz results").assertIsDisplayed()
        compose.onNodeWithText("Correct answers: 3 / 6").assertIsDisplayed()
        compose.onNodeWithText("OK").performClick()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Quiz results").assertDoesNotExist()
        compose.onNodeWithText("Correct answers: 3 / 6").assertIsDisplayed()
        compose.onNodeWithText("NEXT ›").assertDoesNotExist()
        compose.onNodeWithText("TRUE").assertDoesNotExist()
        compose.onNodeWithText("FALSE").assertDoesNotExist()
        screenshot("completed")
    }
}
