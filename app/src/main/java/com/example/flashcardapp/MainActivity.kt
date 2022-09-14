package com.example.flashcardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardQuestion = findViewById<TextView>(R.id.card_question)
        val cardAnswer = findViewById<TextView>(R.id.card_answer)

        cardQuestion.setOnClickListener{
            cardQuestion.visibility = View.INVISIBLE
            cardAnswer.visibility = View.VISIBLE
        }
    }
}