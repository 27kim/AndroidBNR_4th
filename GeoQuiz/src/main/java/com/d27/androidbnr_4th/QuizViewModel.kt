package com.d27.androidbnr_4th

import android.text.BoringLayout
import androidx.lifecycle.ViewModel
import com.d27.androidbnr_4th.util.Log

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    init {
        Log.d("ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel instance about to be destroyed")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        currentIndex = if ((currentIndex - 1) > 0) {
            (currentIndex - 1) % questionBank.size
        } else {
            questionBank.size-1
        }
    }
}