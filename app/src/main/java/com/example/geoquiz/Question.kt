package com.example.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int,
                    val answer: Boolean,
                    var userAnswer: Boolean? = null,
                    var isCheater: Boolean = false)