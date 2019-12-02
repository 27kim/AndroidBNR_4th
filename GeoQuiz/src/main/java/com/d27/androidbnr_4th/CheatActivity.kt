package com.d27.androidbnr_4th

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_cheat.*


class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false
    lateinit var answerTextView: TextView
    lateinit var showAnswerButton: Button

    private val cheatViewModel by lazy {
        ViewModelProviders.of(this).get(CheatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        showAnswerButton = findViewById(R.id.show_answer_button)
        answerTextView = findViewById(R.id.answer_text_view)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)

            cheatViewModel.isCheating = true
            setAnswerShownResult(cheatViewModel.isCheating)
        }
        setAnswerShownResult(cheatViewModel.isCheating)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
//            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.isCheating)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        const val EXTRA_ANSWER_IS_TRUE = "answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "answer_shown"

        fun newIntent(context: Context, flag: Boolean): Intent {
            return Intent(context, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, flag)
            }
        }
    }
}
