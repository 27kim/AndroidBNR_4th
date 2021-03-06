package com.d27.androidbnr_4th

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.d27.androidbnr_4th.CheatActivity.Companion.EXTRA_ANSWER_SHOWN
import com.d27.androidbnr_4th.util.Log

private const val KEY_INDEX = "index"
const val REQUEST_CODE_CHEAT = 0

class MainActivity() : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel by lazy {
        Log.d("Got a QuizViewModel")
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            initCheater()
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            initCheater()
        }
        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            startActivityForResult(CheatActivity.newIntent(this, answerIsTrue), REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    private fun initCheater() {
        quizViewModel.isCheater = false
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId =
            when {
                quizViewModel.isCheater -> R.string.judgment_toast
                userAnswer == correctAnswer -> R.string.correct_toast
                else -> R.string.incorrect_toast
            }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("onSaveInstanceState")

        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onResume() {
        super.onResume()
        Log.d("called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data
                ?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
                ?: false

        }
    }
}
