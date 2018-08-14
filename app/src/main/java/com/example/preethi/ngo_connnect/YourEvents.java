package com.example.preethi.ngo_connnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class YourEvents extends AppCompatActivity {

    String ngo;
    ListView LISTVIEW;
    TextView your_event_name;
    ImageView your_event_poster;
    YourEventsAdapter yourEventsAdapter;
    ArrayList<String> youreventnames = new ArrayList<>();
    ArrayList<String> youreventposters = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_events);

        ngo = getIntent().getStringExtra("org");
        System.out.println(ngo);

        LISTVIEW = (ListView) findViewById(R.id.youreventslistview);
        YourEvents.DoGetYourEvents doGetYourEventsList = new YourEvents.DoGetYourEvents();
        doGetYourEventsList.execute("");

        your_event_name = (TextView) findViewById(R.id.youreventname);
    }

    public class DoGetYourEvents extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        protected void onPostExecute(String r) {
            LISTVIEW.setAdapter(yourEventsAdapter);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");
                String query = "select event_name , poster from Events where org = '"+ ngo +"'";
                System.out.println(query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    youreventnames.add(rs.getString("event_name"));
                    youreventposters.add(rs.getString("poster"));
                    String query1 = "select count(*) as total from Joinee where event_name = '"+ rs.getString("event_name") +"'";
                    System.out.println(query1);
                    String query2 = "select count(*) as sponsor from sponsor where event_name = '"+ rs.getString("event_name") +"'";
                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query2);
                    Statement stmt1 = con.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(query1);
                    if(rs1.next()) {
                        data.add(Integer.toString(rs1.getInt(1)));
                    }
                    if(rs2.next()) {
                        data.add(Integer.toString(rs2.getInt(1)));
                    }

                }
                System.out.println(data);
                System.out.println(youreventnames);

                yourEventsAdapter = new YourEventsAdapter(YourEvents.this,
                        youreventnames,
                        youreventposters,
                        data
                );
            } catch (Exception ex) {
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
