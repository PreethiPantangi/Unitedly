package com.example.preethi.ngo_connnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.preethi.NGO_Connect.R;

public class EntryActivity extends AppCompatActivity {

    Button ngo, publicman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        ngo = findViewById(R.id.button);
        ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ngoLogin();
            }
        });
        publicman = findViewById(R.id.button1);
        publicman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonManLogin();
            }
        });
    }

    public void ngoLogin() {
        Intent intent = new Intent(getBaseContext(), NgoLogin.class);
        startActivity(intent);
    }

    public void commonManLogin() {
        Intent intent = new Intent(getBaseContext(), UserProfile.class);
        startActivity(intent);
    }
}
