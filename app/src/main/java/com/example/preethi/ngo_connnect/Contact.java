package com.example.preethi.ngo_connnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.preethi.NGO_Connect.R;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menulayout , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_event) {
            Intent intent1 = new Intent(getApplicationContext(), UserHome.class);
            startActivity(intent1);
        } else if (id == R.id.nav_ngo) {
            Intent intent1 = new Intent(getApplicationContext(), Ngo.class);
            startActivity(intent1);
        } else if (id == R.id.nav_contact) {
            Intent intent1 = new Intent(getApplicationContext(), Contact.class);
            startActivity(intent1);
        } else if (id == R.id.nav_ngos) {
            Intent intent1 = new Intent(getApplicationContext(), NgoLogin.class);
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }
}
