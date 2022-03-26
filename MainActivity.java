package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    boolean isShowingAnswers = true;
    boolean flashAnswer = false;

    @SuppressWarnings("all")
    @Deprecated
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        TextView flashcardQuestion = findViewById(R.id.flashcard_question);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer);

        if (!allFlashcards.isEmpty()) {
            flashcardQuestion.setText(allFlashcards.get(0).getQuestion());
            flashcardAnswer.setText(allFlashcards.get(0).getAnswer());
        }

        //Bill Clinton, George H.W. Bush, Barrack Obama
        TextView firstChoice = findViewById(R.id.firstChoice);
        TextView secondChoice = findViewById(R.id.secondChoice);
        TextView thirdChoice = findViewById(R.id.thirdChoice);

        //eye icon, eye stroked icon
        ImageView toggleVis = findViewById(R.id.toggle_visibility);
        ImageView toggleInVis = findViewById(R.id.toggle_invisibility);

        //shows the answer to the flashcard question
        flashcardQuestion.setOnClickListener(v -> {
            flashcardQuestion.setVisibility(View.INVISIBLE);
            flashcardAnswer.setVisibility(View.VISIBLE);
        });

        //set incorrect option to grey background
        firstChoice.setOnClickListener(v ->
                findViewById(R.id.firstChoice).setBackgroundColor(getResources().getColor(R.color.gray, null))
        );

        //set incorrect option to grey background
        secondChoice.setOnClickListener(v ->
                findViewById(R.id.secondChoice).setBackgroundColor(getResources().getColor(R.color.gray, null))
        );

        //set correct option to teal background
        thirdChoice.setOnClickListener(v -> {
            findViewById(R.id.thirdChoice).setBackgroundColor(getResources().getColor(R.color.teal_200, null));
            thirdChoice.setTextColor(getResources().getColor(R.color.black, null));
        });

        //listener for eye icon
        findViewById(R.id.toggle_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch the boolean value of isShowingAnswers
                isShowingAnswers = !isShowingAnswers;

                if (isShowingAnswers) {
                    // if we are meant to show answers, set the stroke eye icon invisible
                    // set the regular eye icon and all the options visible
                    ((ImageView) findViewById(R.id.toggle_visibility)).setImageResource(R.drawable.icon_visible);
                    toggleInVis.setVisibility(View.INVISIBLE);
                    toggleVis.setVisibility(View.VISIBLE);
                    firstChoice.setVisibility(View.VISIBLE);
                    secondChoice.setVisibility(View.VISIBLE);
                    thirdChoice.setVisibility(View.VISIBLE);
                } else {
                    // if we are not meant to show answers, set the stroke eye icon visible
                    // set the regular eye icon and all the options invisible
                    ((ImageView) findViewById(R.id.toggle_invisibility)).setImageResource(R.drawable.icon_invisible);
                    toggleInVis.setVisibility(View.VISIBLE);
                    toggleVis.setVisibility(View.INVISIBLE);
                    firstChoice.setVisibility(View.INVISIBLE);
                    secondChoice.setVisibility(View.INVISIBLE);
                    thirdChoice.setVisibility(View.INVISIBLE);
                }
            }
        });

        //listener for stroke eye icon. Does same thing as regular eye listener
        findViewById(R.id.toggle_invisibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowingAnswers = !isShowingAnswers;

                if (isShowingAnswers) {
                    ((ImageView) findViewById(R.id.toggle_visibility)).setImageResource(R.drawable.icon_visible);
                    toggleInVis.setVisibility(View.INVISIBLE);
                    toggleVis.setVisibility(View.VISIBLE);
                    firstChoice.setVisibility(View.VISIBLE);
                    secondChoice.setVisibility(View.VISIBLE);
                    thirdChoice.setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) findViewById(R.id.toggle_invisibility)).setImageResource(R.drawable.icon_invisible);
                    toggleInVis.setVisibility(View.VISIBLE);
                    toggleVis.setVisibility(View.INVISIBLE);
                    firstChoice.setVisibility(View.INVISIBLE);
                    secondChoice.setVisibility(View.INVISIBLE);
                    thirdChoice.setVisibility(View.INVISIBLE);
                }
            }
        });

        //listener for add new flashcard button
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            //creates an intent on click and opens AddCardActivity expecting results
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                //0 is an arbitrary number and is used to keep track of which activity result we expect
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFlashcards==null||allFlashcards.size()==0){
                    return;
                }
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;
                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if(currentCardDisplayedIndex>=allFlashcards.size()){
                    Snackbar.make(flashcardQuestion, "You've reached the end of the cards, going back to start.",Snackbar.LENGTH_SHORT).show();
                    currentCardDisplayedIndex=0;
                }
                // set the question and answer TextViews with data from the database
                flashcardQuestion.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                flashcardAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            }
        });
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    //After AddNewCardActivity has been run
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) { // this 0 needs to match the 0 we used when we called startActivityForResult!
            String customQ = data.getExtras().getString("customQ"); // 'string1' needs to match the key we used when we put the string in the Intent
            String customA = data.getExtras().getString("customA");

            TextView flashcardQuestion = findViewById(R.id.flashcard_question);
            TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
                flashcardQuestion.setText(customQ);
                flashcardAnswer.setText(customA);

            flashcardDatabase.insertCard(new Flashcard(customQ, customA));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }
}

