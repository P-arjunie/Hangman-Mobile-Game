package com.example.game1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children

class GameActivity : AppCompatActivity() {
    private val gameManager = GameManager()
    private var maxScore = 0 // Store the maxScore outside of GameManager


    private lateinit var wordTextView: TextView
    private lateinit var lettersUsedTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var gameLostTextView: TextView
    private lateinit var gameWonTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var lettersLayout: ConstraintLayout
    private lateinit var maxScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)




        imageView = findViewById(R.id.imageView)
        wordTextView = findViewById(R.id.wordTextView)
        lettersUsedTextView = findViewById(R.id.lettersUsedTextView)
        gameLostTextView = findViewById(R.id.gameLostTextView)
        gameWonTextView = findViewById(R.id.gameWonTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        newGameButton = findViewById(R.id.newGameButton)
        lettersLayout = findViewById(R.id.lettersLayout)
        maxScoreTextView = findViewById(R.id.maxScoreTextView)

        // Initialize maxScore from intent extras or default to 0
        maxScore = intent.getIntExtra("maxScore", 0)
        maxScoreTextView.text = "Max Score: $maxScore"

        newGameButton.setOnClickListener {
            startNewGame()
        }

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            navigateToMainScreen()
        }

        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState = gameManager.play(letterView.text[0])
                    updateUI(gameState)
                    letterView.visibility = View.GONE
                }
            }
        }
    }

    private fun updateUI(gameState: GameState) {
        when (gameState) {
            is GameState.Lost -> showGameLost(gameState.wordToGuess, gameState.score, gameState.maxScore)
            is GameState.Running -> {
                wordTextView.text = gameState.underscoreWord
                lettersUsedTextView.text = "Letters used: ${gameState.lettersUsed}"
                scoreTextView.text = "Score: ${gameState.score}"
                maxScoreTextView.text = "Max Score: $maxScore"
                imageView.setImageDrawable(ContextCompat.getDrawable(this, gameState.drawable))
            }
            is GameState.Won -> showGameWon(gameState.wordToGuess, gameState.score, gameState.maxScore)
            else -> {}
        }
    }

    private fun showGameLost(wordToGuess: String, score: Int, maxScore: Int) {
        wordTextView.text = wordToGuess
        gameLostTextView.visibility = View.VISIBLE
        scoreTextView.text = "Score: $score"
        maxScoreTextView.text = "Max Score: $maxScore"
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game7))
        lettersLayout.visibility = View.GONE
    }

    private fun showGameWon(wordToGuess: String, score: Int, maxScore: Int) {
        wordTextView.text = wordToGuess
        gameWonTextView.visibility = View.VISIBLE
        scoreTextView.text = "Score: $score"
        maxScoreTextView.text = "Max Score: $maxScore"
        lettersLayout.visibility = View.GONE
    }

    private fun startNewGame() {
        gameLostTextView.visibility = View.GONE
        gameWonTextView.visibility = View.GONE
        val gameState = gameManager.startNewGame()
        lettersLayout.visibility = View.VISIBLE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
        updateUI(gameState)
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("maxScore", maxScore) // Pass maxScore back to MainActivity
        startActivity(intent)
        finish() // Optional: Close the current activity to prevent going back to it from the main screen
    }
}