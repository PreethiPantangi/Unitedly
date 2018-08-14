package com.example.preethi.ngo_connnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.preethi.ngo_connnect.NgoLogin.MyPREFERENCES;

public class NgoHome extends AppCompatActivity {

    ListView LISTVIEW;
    Button knowmore;
    TextView event_name , ngo_name;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    ArrayList<String> eventnames = new ArrayList<>();
    ArrayList<String> ngonames = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<String> eventdate = new ArrayList<>();
    NgoHomeAdapter adapter;
    String ngoemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_home);


        LISTVIEW = (ListView) findViewById(R.id.ngohome_page);
        NgoHome.DoGetEvents doGetEventsList = new NgoHome.DoGetEvents();
        doGetEventsList.execute("");

        event_name = (TextView) findViewById(R.id.eventname);
        ngo_name = (TextView) findViewById(R.id.ngoname);


    }


    public void onClick(View view) {
        Intent myIntent = new Intent(NgoHome.this, NgoEventDetails.class);

        TextView eventname = (TextView) findViewById(R.id.ngoeventname);
        TextView ngoname = (TextView) findViewById(R.id.ngongoname);
        String getEventName = eventname.getText().toString();
        String getNgoName = ngoname.getText().toString();

        System.out.println(getEventName);
        System.out.println(getNgoName);

        Bundle bundle = new Bundle();
        bundle.putString("eventName", getEventName);
        bundle.putString("ngoName" , getNgoName);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
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

                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String organisation = sharedPreferences.getString("organisation" , null);

                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");
                String query = "select event_name , org , poster , event_date from Events where org not LIKE '"+ organisation +"'";
                System.out.println(query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    System.out.println(rs.getString("poster"));
                    eventnames.add(rs.getString("event_name"));
                    ngonames.add(rs.getString("org"));
                    posters.add(rs.getString("poster"));
                    eventdate.add(rs.getString("event_date"));
                }

                System.out.println( posters );

                adapter = new NgoHomeAdapter(NgoHome.this,
                        eventnames,
                        ngonames,
                        posters,
                        eventdate
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
        } else if(id == R.id.nav_addevent) {
            Intent intent1 = new Intent(getApplicationContext(), AddEvent.class);
            startActivity(intent1);
        } else if (id == R.id.nav_yourevent) {
            Intent intent1 = new Intent(getApplicationContext(), YourEvents.class);
            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            intent1.putExtra("org" , sharedPreferences.getString("organisation","") );
            startActivity(intent1);
            return true;
        } else if (id == R.id.nav_profile) {
            Intent intent1 = new Intent(getApplicationContext(), NgoProfile.class);
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }
}
