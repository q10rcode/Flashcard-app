package com.example.flashcardapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardQuestion = findViewById<TextView>(R.id.card_question)
        val cardAnswer = findViewById<TextView>(R.id.card_answer)

        //Switch from Question to Answer
        cardQuestion.setOnClickListener {
            cardQuestion.visibility = View.INVISIBLE
            cardAnswer.visibility = View.VISIBLE
        }

        //Switch from Answer to Question
        cardAnswer.setOnClickListener {
            cardQuestion.visibility = View.VISIBLE
            cardAnswer.visibility = View.INVISIBLE


        }
        val firstOp = findViewById<TextView>(R.id.wrong1)
        val secondOp = findViewById<TextView>(R.id.wrong2)
        val thirdOp = findViewById<TextView>(R.id.correct)

        firstOp.setOnClickListener {
            firstOp.setBackgroundColor(Color.rgb(255, 0, 0))
        }

        secondOp.setOnClickListener {
            secondOp.setBackgroundColor(Color.rgb(255, 0, 0))
        }

        thirdOp.setOnClickListener {
            thirdOp.setBackgroundColor(Color.rgb(0, 255, 0))
        }


        var isShowing = true
        val hideButton = findViewById<ImageView>(R.id.toggle_choices_visibility)

        //make choice appear and disappear
        hideButton.setOnClickListener {
            if (isShowing){
                hideButton.setImageResource(R.drawable.hidden)

                firstOp.visibility = View.INVISIBLE
                secondOp.visibility = View.INVISIBLE
                thirdOp.visibility = View.INVISIBLE

                isShowing = false
            } else {
                hideButton.setImageResource(R.drawable.eye)

                firstOp.visibility = View.VISIBLE
                secondOp.visibility = View.VISIBLE
                thirdOp.visibility = View.VISIBLE

                isShowing = true
            }
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

            val data: Intent? = result.data

            if(data != null){
                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")

                cardQuestion.text = questionString
                cardAnswer.text = answerString

                firstOp.visibility = View.INVISIBLE
                secondOp.visibility = View.INVISIBLE
                thirdOp.visibility = View.INVISIBLE
                hideButton.visibility = View.INVISIBLE
            }
        }

            val addQuestionButton = findViewById<ImageView>(R.id.add_question_button)

        //Move to add question activity
        addQuestionButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }
    }
}