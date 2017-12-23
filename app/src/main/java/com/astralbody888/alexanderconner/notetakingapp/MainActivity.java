package com.astralbody888.alexanderconner.notetakingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<String> notesList;
    static ArrayAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addNote){
            Intent intent = new Intent(getApplicationContext(), NoteEditActivity.class);
            startActivity(intent);
        };
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.notesListView);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.astralbody888.alexanderconner.notetakingapp", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("Notes", null);
        if (set == null)
        {
            notesList = new ArrayList<>();
            notesList.add("Add a note.");
        } else {
            notesList = new ArrayList<>(set);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(tapItemListener);
        listView.setOnItemLongClickListener(longTapItemListener);
    }

    AdapterView.OnItemClickListener tapItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent notesIntent = new Intent(MainActivity.this, NoteEditActivity.class);
            notesIntent.putExtra("NoteID", i);
            startActivity(notesIntent);
        }
    };

    AdapterView.OnItemLongClickListener longTapItemListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            final int itemToDelete = i;

            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to delete this note?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notesList.remove(itemToDelete);
                            adapter.notifyDataSetChanged();

                            //Save the deletion
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.astralbody888.alexanderconner.notetakingapp", Context.MODE_PRIVATE);

                            HashSet<String> set = new HashSet<>(MainActivity.notesList);
                            sharedPreferences.edit().putStringSet("Notes", set).apply();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            //Return true only returns a longclick, false returns a longclick followed by shortclick
            return true;
        }
    };
}
