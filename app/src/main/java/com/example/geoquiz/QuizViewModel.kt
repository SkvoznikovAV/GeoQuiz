package com.example.geoquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel

private const val TAG="ololo QuizViewModel"

class QuizViewModel: ViewModel() {

    var currentIndex = 0
    var percentTrueAnswers = -1

    private val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true)
    )

    val currentQuestionAnswer: Boolean
        get()=questionBank[currentIndex].answer

    val currentQuestionTextResId: Int
        get()=questionBank[currentIndex].textResId

    var currentQuestionUserAnswer: Boolean?
        get()=questionBank[currentIndex].userAnswer
        set(value) {
            questionBank[currentIndex].userAnswer = value
        }

    fun moveToNext(){
        currentIndex = (currentIndex+1)%questionBank.size
    }

    fun moveToPrev(){
        currentIndex--
        if (currentIndex<0) currentIndex = questionBank.size-1
    }

    fun checkEnd(): Boolean {
        var trueAnswers=0
        var isEnd=true
        for (currentQuestion in questionBank) {
            if (currentQuestion.userAnswer==null) isEnd=false

            if (currentQuestion.userAnswer==currentQuestion.answer) {
                trueAnswers++
            }
        }

        if (isEnd) {
            percentTrueAnswers = (trueAnswers * 100) / questionBank.size
            return true
        } else {
            return false
        }
    }
}