package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NotesEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        createBell(menu);
        return true;
    }

    void createBell(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.bell_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return menuChoice(item);
    }

    private boolean menuChoice(MenuItem item) {
        return false;
    }
}