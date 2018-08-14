package com.example.preethi.ngo_connnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.preethi.ngo_connnect.NgoHome.MyPREFERENCES;


public class NgoDetailsProfile extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    DatabaseConnection databaseConnection;
    NgoProfileAdapter ngoProfileAdapter;
    ArrayList<String> UserName_ArrayList = new ArrayList<String>();
    ArrayList<String> Description_ArrayList = new ArrayList<String>();
    ArrayList<String> Address_ArrayList = new ArrayList<String>();
    ArrayList<String> Email_ArrayList = new ArrayList<String>();
    ArrayList<String> Contact_ArrayList = new ArrayList<String>();
    ArrayList<String> Experience_ArrayList = new ArrayList<String>();
    ArrayList<String> Website_ArrayList = new ArrayList<String>();
    ArrayList<String> Poster_Arraylist = new ArrayList<String>();

    ListView LISTVIEW;
    int flag = 0;
    ResultSet rs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_profile);

        LISTVIEW = (ListView) findViewById(R.id.profile);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        NgoDetailsProfile.DisplayProfileDetails doGetNgoProfile = new NgoDetailsProfile.DisplayProfileDetails();
        doGetNgoProfile.execute("");


    }

    public class DisplayProfileDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }
        protected void onPostExecute(String r) {
            // Log.i("EventDetails","eee");
            LISTVIEW.setAdapter(ngoProfileAdapter);
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");


                String ngo = getIntent().getStringExtra("org");
                System.out.println(ngo);

                String query = "select ngo_name , ngo_address , exp , email , weblink , domain , picture , phone from NGO where ngo_name = '"+ngo+"' ";
                System.out.println(query);
                Statement stmt = con.createStatement();
                System.out.println("After Statement stmt");
                rs = stmt.executeQuery(query);
                System.out.println("Executing ResultSet statement");

                while (rs.next()) {
                    flag = 1;
                    UserName_ArrayList.add(rs.getString("ngo_name"));
                    Description_ArrayList.add(rs.getString("domain"));
                    Address_ArrayList.add(rs.getString("ngo_address"));
                    Email_ArrayList.add(rs.getString("email"));
                    Contact_ArrayList.add(rs.getString("phone"));
                    Experience_ArrayList.add(rs.getString("exp"));
                    Website_ArrayList.add(rs.getString("weblink"));
                    Poster_Arraylist.add(rs.getString("picture"));
                }
                if(flag == 0){
                    Toast.makeText(NgoDetailsProfile.this, "No details to show", Toast.LENGTH_SHORT).show();

                }

                ngoProfileAdapter = new NgoProfileAdapter(NgoDetailsProfile.this ,
                        UserName_ArrayList,
                        Description_ArrayList,
                        Address_ArrayList ,
                        Email_ArrayList,
                        Contact_ArrayList,
                        Experience_ArrayList,
                        Website_ArrayList,
                        Poster_Arraylist
                );
            }catch (Exception ex){
                Log.e("ERROR", ex.getMessage());
            }
            return "";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.ngomenulayout , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent1 = new Intent(getApplicationContext(), NgoHome.class);
            startActivity(intent1);
        } else  if(id == R.id.nav_addevent) {
            Intent intent1 = new Intent(getApplicationContext(), AddEvent.class);
            startActivity(intent1);
        } else if (id == R.id.nav_yourevent) {
            Intent intent1 = new Intent(getApplicationContext(), YourEvents.class);
            startActivity(intent1);
        } else if (id == R.id.nav_profile) {
            Intent intent1 = new Intent(getApplicationContext(), NgoProfile.class);
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }

}
