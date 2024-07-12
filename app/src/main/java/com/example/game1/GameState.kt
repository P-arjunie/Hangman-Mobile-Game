package com.example.game1

sealed class GameState(val score: Int, val maxScore: Int) {
    class Running(
        val lettersUsed: String,
        val underscoreWord: String,
        val drawable: Int,
        score: Int, // Include score parameter in constructor
        maxScore: Int // Include maxScore parameter in constructor
    ) : GameState(score, maxScore)

    open class EndGame(
        val wordToGuess: String,
        score: Int, // Include score parameter in constructor
        maxScore: Int // Include maxScore parameter in constructor
    ) : GameState(score, maxScore)

    class Lost(
        wordToGuess: String,
        score: Int, // Include score parameter in constructor
        maxScore: Int // Include maxScore parameter in constructor
    ) : EndGame(wordToGuess, score, maxScore)

    class Won(
        wordToGuess: String,
        score: Int, // Include score parameter in constructor
        maxScore: Int // Include maxScore parameter in constructor
    ) : EndGame(wordToGuess, score, maxScore)
}
