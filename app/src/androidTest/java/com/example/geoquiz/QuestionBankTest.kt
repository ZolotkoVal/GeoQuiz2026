package com.example.geoquiz

import org.junit.Assert.assertEquals
import org.junit.Test

class QuestionBankTest {
    @Test fun bankMatchesAllSixPdfStatementsAndAnswers() {
        assertEquals(listOf(
            "Canberra is the capital of Australia.",
            "The Pacific Ocean is larger than the Atlantic Ocean.",
            "The Suez Canal connects the Red Sea and the Indian Ocean.",
            "The source of the Nile River is in Egypt.",
            "The Amazon River is the longest river in the Americas.",
            "Lake Baikal is the world's oldest and deepest freshwater lake."
        ), questions.map { it.text })
        assertEquals(listOf(true, true, false, false, true, true), questions.map { it.answer })
    }
}
