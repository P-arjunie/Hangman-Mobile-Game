package com.example.game1

import kotlin.random.Random

class GameManager {
    private var lettersUsed: String = ""
    private lateinit var underscoreWord: String
    private lateinit var wordToGuess: String
    private val maxTries = 7
    private var currentTries = 0
    private var drawable: Int = R.drawable.game0
    private var score = 0
    private var maxScore =0
    private var consecutiveCorrectGuesses = 0
    private val scoresHistory = mutableListOf<Int>() // Store all scores for each game

    fun startNewGame(): GameState {
        lettersUsed = ""
        currentTries = 0
        drawable = R.drawable.game0
        score = 0 //resetting the score when a new game starts
        consecutiveCorrectGuesses = 0
        maxScore = scoresHistory.maxOrNull() ?: 0

        val randomIndex = Random.nextInt(0, GameConstants.words.size)
        wordToGuess = GameConstants.words[randomIndex]
        generateUnderscores(wordToGuess)
        return getGameState()
    }

    fun play(letter: Char): GameState {
        if (lettersUsed.contains(letter)) {
            return GameState.Running(lettersUsed, underscoreWord, drawable, score, scoresHistory.maxOrNull() ?: 0)
        }
        if (score>maxScore){
            maxScore = score
        }
        lettersUsed += letter
        val indexes = mutableListOf<Int>()

        wordToGuess.forEachIndexed { index, char ->
            if (char.equals(letter, true)) {
                indexes.add(index)
            }
        }

        var finalUnderscoreWord = underscoreWord.toCharArray()
        indexes.forEach { index ->
            finalUnderscoreWord[index] = letter
        }

        if (indexes.isEmpty()) {
            currentTries++
            score -= 50 // Deduct 50 points for incorrect guess
            score = maxOf(score, 0) // Ensure score does not go below 0
            consecutiveCorrectGuesses = 0 // Reset consecutive correct guesses
        } else {
            score += indexes.size * 100 // Award points for correct guess
            consecutiveCorrectGuesses++
            if (consecutiveCorrectGuesses == 4 || consecutiveCorrectGuesses == 8) {
                score *= 2 // Double score for every 4th and 8th consecutive correct guess
            }
        }

        underscoreWord = String(finalUnderscoreWord)
        return getGameState()
    }

    private fun generateUnderscores(word: String) {
        underscoreWord = word.map { if (it.isLetter()) '_' else it }.joinToString("")
    }

    private fun getHangmanDrawable(): Int {
        return when (currentTries) {
            0 -> R.drawable.game0
            1 -> R.drawable.game1
            2 -> R.drawable.game2
            3 -> R.drawable.game3
            4 -> R.drawable.game4
            5 -> R.drawable.game5
            6 -> R.drawable.game6
            7 -> R.drawable.game7
            else -> R.drawable.game7
        }
    }

    private fun getGameState(): GameState {
        if (underscoreWord.equals(wordToGuess, true)) {
            score += 1000 // Bonus for winning
            val maxScore = scoresHistory.maxOrNull() ?: 0
            scoresHistory.add(score) // Add the final score to scores history
            return GameState.Won(wordToGuess, score, maxScore)
        }

        if (currentTries == maxTries) {
            score -= 500 // Penalty for losing
            score = maxOf(score, 0) // Ensure score does not go below 0
            val maxScore = scoresHistory.maxOrNull() ?: 0
            scoresHistory.add(score) // Add the final score to scores history
            return GameState.Lost(wordToGuess, score, maxScore)
        }

        drawable = getHangmanDrawable()
        return GameState.Running(lettersUsed, underscoreWord, drawable, score, scoresHistory.maxOrNull() ?: 0)
    }
}