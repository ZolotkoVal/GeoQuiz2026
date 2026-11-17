package com.example.geoquiz

import org.junit.Assert.*
import org.junit.Test

class QuizStateTest {
    @Test fun cannotSkipUnansweredQuestion() {
        val state = QuizState()
        assertEquals(state, state.next())
        assertEquals(0, state.score)
        assertFalse(state.answered)
    }

    @Test fun repeatedAnswerDoesNotChangeScoreOrSelection() {
        val state = QuizState().answer(true)
        assertEquals(1, state.score)
        assertEquals(state, state.answer(false))
        assertEquals(state, state.answer(true))
    }

    @Test fun everyAnswerCombinationHasExactScoreAndCannotPassLastQuestion() {
        repeat(64) { mask ->
            var state = QuizState()
            var expected = 0
            questions.forEachIndexed { index, question ->
                val selection = mask and (1 shl index) != 0
                if (selection == question.answer) expected++
                state = state.answer(selection)
                assertEquals(expected, state.score)
                assertEquals(index, state.index)
                assertTrue(state.answered)
                if (index < questions.lastIndex) state = state.next()
            }
            assertTrue(state.finished)
            assertEquals(5, state.index)
            assertEquals(state, state.next())
            assertEquals(state, state.answer(true))
            assertEquals(state, state.answer(false))
        }
    }
}
