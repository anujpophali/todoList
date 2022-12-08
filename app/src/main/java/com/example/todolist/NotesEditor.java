package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Calendar;
import java.util.Date;

public class NotesEditor extends AppCompatActivity {
    int position;
    EditText edtTitle,edtDesc;
    String title,desc,newTitle,newDesc;
    TimePicker timePicker;
    Button btn;
    int hrs,mins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        edtTitle = findViewById(R.id.title);
        edtDesc = findViewById(R.id.description);
        timePicker = findViewById(R.id.timep);

        Intent intent = getIntent();
        position = intent.getIntExtra("noteId", -1);

        if (position == -1) {
            edtTitle.setText("");
            edtDesc.setText("");
        } else {
            title = MainActivity.titles.get(position);
            desc = MainActivity.titleAndDesc.get(title);
            edtTitle.setText(title);
            edtDesc.setText(desc);
        }

        Button setNotification = findViewById(R.id.timebtn);
        setNotification.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createNotification();
            }
        });
    }
//

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
                break;
            case R.id.bell:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My Task: "+title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Description: "+desc);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
                break;
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void createNotification()
    {
        NotificationManager nm=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskMaster";
            String description = "TaskMaster notifcation.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("11111", name, importance);
            channel.setDescription(description);
            nm.createNotificationChannel(channel);
        }
        LocalTime time = LocalTime.now();
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,"11111")
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle(title)
                .setContentText("Scheduled At: "+" "+time).setOngoing(true);
        builder.setAutoCancel(true);
        Intent notificationIntent = new Intent(this,MainActivity.class);
        notificationIntent.putExtra("notificationID", time);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        nm.notify(0,builder.build());
    }

}