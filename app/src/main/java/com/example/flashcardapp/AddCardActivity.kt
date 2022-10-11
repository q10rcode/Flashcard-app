package com.example.flashcardapp

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)




        findViewById<ImageView>(R.id.cancel_button).setOnClickListener {
            finish()
        } //end of cancel button

        val questionEditText = findViewById<EditText>(R.id.question_text)
        val answerEditText = findViewById<EditText>(R.id.answer_text)



        val saveButton = findViewById<ImageView>(R.id.save_button)

        //Extract data from EditText
        saveButton.setOnClickListener {


                val questionString = questionEditText.text.toString()
                val answerString = answerEditText.text.toString()

                val data = Intent()
                data.putExtra("QUESTION_KEY", questionString)
                data.putExtra("ANSWER_KEY", answerString)

                setResult(RESULT_OK, data)
                finish()

        }
    }
}