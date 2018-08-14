package com.example.preethi.ngo_connnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UserHome extends AppCompatActivity {

    ListView LISTVIEW;
    TextView event_name , ngo_name , eventname;
    Button knowmore;
    ArrayList<String> eventnames = new ArrayList<>();
    ArrayList<String> ngonames = new ArrayList<>();
    ArrayList<String> eventdate = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    Data adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        LISTVIEW = (ListView) findViewById(R.id.entrylistview);
        UserHome.DoGetEvents doGetEventsList = new UserHome.DoGetEvents();
        doGetEventsList.execute("");
    }


    public class DoGetEvents extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        protected void onPostExecute(String r) {
            LISTVIEW.setAdapter(adapter);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");
                String query = "select event_name , org , event_date , poster from Events";
                System.out.println(query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    System.out.println(rs.getString("poster"));
                    eventnames.add(rs.getString("event_name"));
                    ngonames.add(rs.getString("org"));
                    eventdate.add(rs.getString("event_date"));
                    posters.add(rs.getString("poster"));
                }

                System.out.println( posters );

                adapter = new Data(UserHome.this,
                        eventnames,
                        ngonames,
                        eventdate,
                        posters
                );
            } catch (Exception ex) {
                Log.e("ERROR", ex.getMessage());
            }
            return "";
        }
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
