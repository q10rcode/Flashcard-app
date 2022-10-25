package com.example.flashcardapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val cardQuestion = findViewById<TextView>(R.id.card_question)
        val cardAnswer = findViewById<TextView>(R.id.card_answer)


        if(allFlashcards.isNotEmpty()) {
            cardQuestion.text = allFlashcards[0].question
            cardAnswer.text = allFlashcards[0].answer
        }


        //Switch from Question to Answer
        cardQuestion.setOnClickListener {
            val cx = cardAnswer.width / 2
            val cy = cardAnswer.height / 2

            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(cardAnswer, cx, cy, 0f, finalRadius)


            cardQuestion.visibility = View.INVISIBLE
            cardAnswer.visibility = View.VISIBLE

            anim.duration = 300
            anim.start()
        }



        //Switch from Answer to Question
        cardAnswer.setOnClickListener {
            val cx = cardAnswer.width / 2
            val cy = cardAnswer.height / 2

            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(cardQuestion, cx, cy, 0f, finalRadius)

            cardQuestion.visibility = View.VISIBLE
            cardAnswer.visibility = View.INVISIBLE

            anim.duration = 300
            anim.start()




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

        //Launcher to recieve data from add question
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

            val data: Intent? = result.data

            if(data != null){
                val questionText =  data.getStringExtra("QUESTION_KEY")
                val answerText =  data.getStringExtra("ANSWER_KEY")

                cardQuestion.text = questionText
                cardAnswer.text = answerText

                flashcardDatabase.insertCard(Flashcard(questionText.toString(), answerText.toString()))
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                if(!questionText.isNullOrEmpty() && !answerText.isNullOrEmpty()) {
                    flashcardDatabase.insertCard(Flashcard(questionText, answerText))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }

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
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        val nextCardButton = findViewById<ImageView>(R.id.next_button)
        var currentCardIndex = 0



        //Move to next card
        nextCardButton.setOnClickListener {

            val leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in)

            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    Log.i("Index", "Animation start")

                    cardQuestion.startAnimation(leftOutAnim)
                }

                override fun onAnimationEnd(animation: Animation?) {
                    cardQuestion.startAnimation(rightInAnim)
                    //set new card
                    if (currentCardIndex != allFlashcards.size && currentCardIndex >= 0) {
                        currentCardIndex = getRandomNumber(0, allFlashcards.size - 1)
                        cardQuestion.text = allFlashcards[currentCardIndex].question
                        cardAnswer.text = allFlashcards[currentCardIndex].answer
                    }  else if(allFlashcards.size == 0){ //Pre
                        cardQuestion.text ="There are no more flash cards :("
                        cardAnswer.text = "Use the plus sign to create more"
                        Log.i("Index", "currentCardIndex: $currentCardIndex")
                    } else { //If the index is at the end
                        cardQuestion.text = allFlashcards[0].question
                        cardAnswer.text = allFlashcards[0].answer
                        currentCardIndex = 0
                    } // end of new card logic

                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })

            cardQuestion.startAnimation(leftOutAnim)


        } //End of next button

        //Delete Button
        val deleteButton = findViewById<ImageView>(R.id.delete_button)

        deleteButton.setOnClickListener {
            val flashcardQuestionToDelete = findViewById<TextView>(R.id.card_question).text.toString()
            if (allFlashcards.size > 0 ) {
                flashcardDatabase.deleteCard(flashcardQuestionToDelete)
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                cardQuestion.text ="Deleted!"
                cardAnswer.text = "Deleted!"
            }
        } //End of delete button





    } //End of onCreate
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    fun getRandomNumber(minNumber: Int, maxNumber: Int): Int {
        return (minNumber..maxNumber).random() // generated random from 0 to 10 included
    }
}