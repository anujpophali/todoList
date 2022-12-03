package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> notes = new ArrayList<>();
    static HashMap<String,String> titleAndDesc=new HashMap<String,String>();
    static ArrayList<String> titles=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lstView);

        SharedPreferences sharedPreferences= getApplicationContext()
                .getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
        HashSet<String> set=(HashSet<String>) sharedPreferences
                .getStringSet("notes",null);

        if(set==null){
            notes.add("Sample Note Title|/Sample Note Description");
        }
        else{
            notes=new ArrayList<>(set);
        }


        for(String s:notes){
            String[] sarr =s.split("[|/]");
            titles.add(sarr[0]);
            titleAndDesc.put(sarr[0],sarr[1]);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NotesEditor.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setTitle("Warning!")
                        .setMessage("Do You want to delete this note ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String title=titles.get(itemToDelete);
                                        titles.remove(itemToDelete);
                                        String desc=titleAndDesc.get(title);
                                        notes.remove(title +"|/"+desc);
                                        titleAndDesc.remove(title);

                                        arrayAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), "Your Task is successfully Deleted.", Toast.LENGTH_LONG).show();

                                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
                                        HashSet<String> set = new HashSet<>(MainActivity.notes);
                                        sharedPreferences.edit().putStringSet("notes", set).apply();

                                    }

                                }
                        )
                        .setNegativeButton("No", null)
                        .show();


                return true;

            }

        });

    }

    void createMenu(Menu menu){
        MenuItem i = menu.add(0,0, 0, "Share");
        MenuItem i1 = menu.add(0,1, 0, "Exit");
    }

    private boolean menuChoice(MenuItem item){
        if(item.getItemId()==R.id.plus){
            Intent intent=new Intent(getApplicationContext(),NotesEditor.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==0){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
            startActivity(Intent.createChooser(sharingIntent, "Share Via"));
        }
        else if(item.getItemId()==1){
            finish();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        createMenu(menu);
        createPlus(menu);
        return true;
    }

    void createPlus(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.option_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return menuChoice(item);
    }

}