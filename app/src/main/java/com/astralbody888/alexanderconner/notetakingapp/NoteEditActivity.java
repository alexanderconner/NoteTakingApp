package com.astralbody888.alexanderconner.notetakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditActivity extends AppCompatActivity {

    private EditText editText;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        editText = findViewById(R.id.mainEditText);

        Intent callingIntent = getIntent();
        noteId = callingIntent.getIntExtra("NoteID", -1); //tells which note to open

        if (noteId != -1)
        {
            editText.setText(MainActivity.notesList.get(noteId));
        } else {
            //If noteID is -1 we are adding a new note, not loading an old one
            MainActivity.notesList.add("");
            noteId = MainActivity.notesList.size() - 1;
            MainActivity.adapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                MainActivity.notesList.set(noteId, charSequence.toString());
                MainActivity.adapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.astralbody888.alexanderconner.notetakingapp", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet<>(MainActivity.notesList);
                sharedPreferences.edit().putStringSet("Notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
