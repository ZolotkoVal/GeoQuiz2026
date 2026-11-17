package com.example.geoquiz

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class QuizUiTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun answerButtonsDisappearAndNextAdvancesOnlyAfterAnswer() {
        compose.onNodeWithText(questions[0].text).assertIsDisplayed()
        compose.onNodeWithText("NEXT ›").assertIsNotEnabled()
        compose.onNodeWithText("TRUE").performClick()
        compose.onNodeWithText("TRUE").assertDoesNotExist()
        compose.onNodeWithText("FALSE").assertDoesNotExist()
        compose.onNodeWithText("NEXT ›").assertIsEnabled().performClick()
        compose.onNodeWithText(questions[1].text).assertIsDisplayed()
        compose.onNodeWithText("TRUE").assertIsDisplayed()
        compose.onNodeWithText("FALSE").assertIsDisplayed()
    }

    @Test fun lastAnswerRemovesNextAndBothAnswerButtons() {
        questions.forEachIndexed { index, question ->
            compose.onNodeWithText(question.text).assertIsDisplayed()
            compose.onNodeWithText(if (question.answer) "TRUE" else "FALSE").performClick()
            if (index < questions.lastIndex) compose.onNodeWithText("NEXT ›").performClick()
        }
        compose.onNodeWithText("NEXT ›").assertDoesNotExist()
        compose.onNodeWithText("TRUE").assertDoesNotExist()
        compose.onNodeWithText("FALSE").assertDoesNotExist()
    }

    @Test fun answeredStateSurvivesActivityRecreation() {
        compose.onNodeWithText("TRUE").performClick()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText(questions[0].text).assertIsDisplayed()
        compose.onNodeWithText("TRUE").assertDoesNotExist()
        compose.onNodeWithText("NEXT ›").assertIsEnabled().performClick()
        compose.onNodeWithText(questions[1].text).assertIsDisplayed()
    }
}
