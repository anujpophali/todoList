package com.example.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashSet;


public class NotesEditor extends AppCompatActivity {
    int position;
    EditText edtTitle,edtDesc;
    String title,desc,newTitle,newDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        edtTitle=findViewById(R.id.title);
        edtDesc=findViewById(R.id.description);

        Intent intent=getIntent();
        position=intent.getIntExtra("noteId",-1);

        if(position==-1){
            edtTitle.setText("");
            edtDesc.setText("");
        }else{
            title=MainActivity.titles.get(position);
            desc=MainActivity.titleAndDesc.get(title);
            edtTitle.setText(title);
            edtDesc.setText(desc);
        }

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
        switch (item.getItemId()) {

            case android.R.id.home:
                newTitle=edtTitle.getText().toString();
                newDesc=edtDesc.getText().toString();
                String save=newTitle+"|"+newDesc;
                if(position==-1){
                    MainActivity.notes.add(save);
                    MainActivity.titles.add(newTitle);
                    MainActivity.titleAndDesc.put(newTitle,newDesc);
                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet<>(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                    this.finish();
                }else{
                    MainActivity.notes.set(position,save);
                    MainActivity.titles.set(position,newTitle);
                    MainActivity.titleAndDesc.remove(title);
                    MainActivity.titleAndDesc.put(newTitle,newDesc);
                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet<>(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                    this.finish();
                }
                return true;


        }

        return false;
    }
}