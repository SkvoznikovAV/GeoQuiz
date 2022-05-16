package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG="MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX,0)?: 0
        quizViewModel.currentIndex = currentIndex

        initViews()
        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.d(TAG, "onSaveInstanceState() called with: outState = $outState")
        outState.putInt(KEY_INDEX,quizViewModel.currentIndex)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun initViews() {
        trueButton = findViewById(R.id.true_button)
        trueButton.setOnClickListener {
            setUserAnswer(true)
        }

        falseButton = findViewById(R.id.false_button)
        falseButton.setOnClickListener {
            setUserAnswer(false)
        }

        val nextClickListener = View.OnClickListener {
            nextQuestion()
        }

        nextButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener(nextClickListener)

        questionTextView = findViewById(R.id.question_text_view)
        questionTextView.setOnClickListener(nextClickListener)

        prevButton = findViewById(R.id.prev_button)
        prevButton.setOnClickListener {
            prevQuestion()
        }

        cheatButton = findViewById(R.id.cheat_button)
        cheatButton.setOnClickListener {
            val intent = CheatActivity.newIntent(this@MainActivity,quizViewModel.currentQuestionAnswer)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

    }

    private fun prevQuestion(){
        quizViewModel.moveToPrev()
        updateQuestion()
    }

    private fun nextQuestion(){
        quizViewModel.moveToNext()
        updateQuestion()
    }

    private fun getResultAnswer(userAnswer: Boolean) = if (userAnswer == quizViewModel.currentQuestionAnswer) {
        resources.getString(R.string.correct_toast)
    } else {
        resources.getString(R.string.incorrect_toast)
    }

    private fun setUserAnswer(userAnswer: Boolean){
        quizViewModel.currentQuestionUserAnswer=userAnswer
        updateQuestion()

        if (quizViewModel.checkEnd()) {
            Toast.makeText(this,"Вы ответили правильно на ${quizViewModel.percentTrueAnswers}% вопросов",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateQuestion() {
        val nameQuestion=resources.getString(quizViewModel.currentQuestionTextResId)

        var uiQuestion = "${quizViewModel.currentIndex+1}. $nameQuestion"
        if (quizViewModel.currentQuestionUserAnswer!=null) {
            val userAnswer = if (quizViewModel.currentQuestionUserAnswer!!){
                resources.getString(R.string.true_button)
            } else {
                resources.getString(R.string.false_button)
            }

            uiQuestion=uiQuestion+"\n\n"+"Вы ответили \""+userAnswer+"\" - "+getResultAnswer(quizViewModel.currentQuestionUserAnswer!!)
        }

        if (quizViewModel.isCheater){
            uiQuestion += "\n\nТы подсмотрел ответ на этот вопрос паскуденыш!!!"
        }


        questionTextView.text = uiQuestion

        if (quizViewModel.currentQuestionUserAnswer !== null) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK){
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false) ?: false
        }
    }
}