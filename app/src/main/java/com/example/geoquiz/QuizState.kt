package com.example.geoquiz

/** -1 means unanswered; 0/1 store the user's false/true selection. */
data class QuizState(
    val index: Int = 0,
    val answers: List<Int> = List(questions.size) { -1 }
) {
    init {
        require(index in questions.indices)
        require(answers.size == questions.size && answers.all { it in -1..1 })
    }

    val answered: Boolean get() = answers[index] != -1
    val finished: Boolean get() = index == questions.lastIndex && answered
    val score: Int get() = questions.indices.count {
        answers[it] != -1 && (answers[it] == 1) == questions[it].answer
    }

    fun answer(value: Boolean): QuizState {
        if (answered) return this
        return copy(answers = answers.mapIndexed { position, old ->
            if (position == index) if (value) 1 else 0 else old
        })
    }

    fun next(): QuizState =
        if (answered && index < questions.lastIndex) copy(index = index + 1) else this
}
