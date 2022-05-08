package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG="MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            setUserAnswer(true)
        }

        falseButton.setOnClickListener {
            setUserAnswer(false)
        }

        val nextClickListener = View.OnClickListener {
            nextQuestion()
        }

        nextButton.setOnClickListener(nextClickListener)
        questionTextView.setOnClickListener(nextClickListener)
        prevButton.setOnClickListener {
            prevQuestion()
        }

        updateQuestion()
    }

    private fun prevQuestion(){
        currentIndex--
        if (currentIndex<0) currentIndex = questionBank.size-1

        updateQuestion()
    }

    private fun nextQuestion(){
        currentIndex = (currentIndex+1)%questionBank.size

        updateQuestion()
    }

    private fun checkEndAndShowResult() {
        var trueAnswers=0
        var isEnd=true
        for (currentQuestion in questionBank) {
            if (currentQuestion.userAnswer==null) isEnd=false

            if (currentQuestion.userAnswer==currentQuestion.answer) {
                trueAnswers++
            }
        }

        if (isEnd) {
            var percentTrue = (trueAnswers * 100)/questionBank.size

            Toast.makeText(this,"Вы ответили правильно на $percentTrue% вопросов",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResultAnswer(userAnswer: Boolean) = if (userAnswer == questionBank[currentIndex].answer) {
        resources.getString(R.string.correct_toast)
    } else {
        resources.getString(R.string.incorrect_toast)
    }

    private fun setUserAnswer(userAnswer: Boolean){
        questionBank[currentIndex].userAnswer=userAnswer

        updateQuestion()
        checkEndAndShowResult()
    }

    private fun updateQuestion() {
        val nameQuestion=resources.getString(questionBank[currentIndex].textResId)

        var uiQuestion = "${currentIndex+1}. $nameQuestion"
        if (questionBank[currentIndex].userAnswer!=null) {
            val userAnswer = if (questionBank[currentIndex].userAnswer!!){
                resources.getString(R.string.true_button)
            } else {
                resources.getString(R.string.false_button)
            }

            uiQuestion=uiQuestion+"\n\n"+"Вы ответили \""+userAnswer+"\" - "+getResultAnswer(questionBank[currentIndex].userAnswer!!)
        }

        questionTextView.text = uiQuestion

        if (questionBank[currentIndex].userAnswer !== null) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }
}