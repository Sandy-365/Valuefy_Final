package com.example.valuefy_final_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ToDo extends AppCompatActivity {

    ImageButton backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        backbtn = findViewById(R.id.back_to_do);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to MainActivity
                Intent intent = new Intent(ToDo.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Closes the current activity
            }
        });
    }
}