package com.example.preethi.ngo_connnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.preethi.ngo_connnect.NgoLogin.MyPREFERENCES;

public class EventDetails extends AppCompatActivity {
    ArrayList<String> event_name;
    ArrayList<String> category;
    String eventName , accnum , accname , ifcs , branch , cost;
    ListView LISTVIEW;
    SharedPreferences sharedPreferences;
    ResultSet rs;
    EventDetailsAdapter eventDetailsAdapter;
    ArrayList<String> EventName_ArrayList = new ArrayList<String>();
    ArrayList<String> Amount_ArrayList = new ArrayList<String>();
    ArrayList<String> Organisation_ArrayList = new ArrayList<String>();
    ArrayList<String> Location_ArrayList = new ArrayList<String>();
    ArrayList<String> Date_ArrayList = new ArrayList<String>();
    ArrayList<String> Time_ArrayList = new ArrayList<String>();
    ArrayList<String> Description_ArrayList = new ArrayList<String>();
    ArrayList<String> Poster_ArrayList = new ArrayList<String>();
    int flag = 0;
    String joinEventNgo;

    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        System.out.println(eventName);

        LISTVIEW = (ListView) findViewById(R.id.eventdetailslist);
//        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        EventDetails.DisplayEventDetails doGetEventsList = new EventDetails.DisplayEventDetails();
        doGetEventsList.execute("");
    }

    public void join(View view) {
        Intent myIntent = new Intent(EventDetails.this, Join.class);
        TextView eventname = (TextView) findViewById(R.id.event_name);
        String joinEvent = eventname.getText().toString();
        TextView ngoname = (TextView) findViewById(R.id.ngo_name);
        joinEventNgo = ngoname.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("eventName", joinEvent);
        bundle.putString("ngoName", joinEventNgo);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    public void sponsor(View view) {
        Intent myIntent = new Intent(EventDetails.this, Sponsor.class);
        TextView autoCompleteTextView = (TextView) findViewById(R.id.event_name);
        String joinEvent = autoCompleteTextView.getText().toString();
        TextView autoCompleteTextView1 = (TextView) findViewById(R.id.ngo_name);
        String joinEventNgo = autoCompleteTextView1.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("eventName", joinEvent);
        bundle.putString("ngoName", joinEventNgo);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }


    public class DisplayEventDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }
        protected void onPostExecute(String r) {
            // Log.i("EventDetails","eee");
            LISTVIEW.setAdapter(eventDetailsAdapter);
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");

                Bundle bundle = getIntent().getExtras();
                String ngoname = bundle.getString("ngoName");

                String query = "select Events.event_name , Events.org , Events.cost , Events.loc , Events.event_desc , Events.event_time , Events.event_date , Events.poster , NGO.account_num , NGO.accholdername , NGO.ifsccode , NGO.branch from Events , NGO where Events.event_name='"+eventName+"' and NGO.ngo_name = 'Sahaya Foundation' ";
                System.out.println(query);
                Statement stmt = con.createStatement();
                System.out.println("After Statement stmt");
                rs = stmt.executeQuery(query);
                System.out.println("Executing ResultSet statement");

                if (rs.next()) {
                    flag = 1;
                    EventName_ArrayList.add(rs.getString("event_name"));
                    Organisation_ArrayList.add(rs.getString("org"));
                    Amount_ArrayList.add(rs.getString("cost"));
                    Location_ArrayList.add(rs.getString("loc"));
                    Date_ArrayList.add(rs.getString("event_date"));
                    Time_ArrayList.add(rs.getString("event_time"));
                    Description_ArrayList.add(rs.getString("event_desc"));
                    Poster_ArrayList.add(rs.getString("poster"));
                    accnum = rs.getString("account_num");
                    accname = rs.getString("accholdername");
                    ifcs = rs.getString("ifsccode");
                    branch = rs.getString("branch");
                    cost = rs.getString("cost");
                    sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("accnum", accnum);
                    editor.putString("accname", accname);
                    editor.putString("ifcs", ifcs);
                    editor.putString("branch" , branch);
                    editor.putString("amount" , cost);
                    editor.commit();
                    System.out.println(accnum + " " + accname + " " + ifcs + " " + branch);
                }
                System.out.println("Pass Amount : " + cost);



                eventDetailsAdapter = new EventDetailsAdapter(EventDetails.this,
                        EventName_ArrayList,
                        Organisation_ArrayList,
                        Amount_ArrayList ,
                        Location_ArrayList,
                        Date_ArrayList,
                        Time_ArrayList,
                        Description_ArrayList,
                        Poster_ArrayList
                );
            }catch (Exception ex){
                Log.e("ERROR", ex.getMessage());
            }
            return "";
        }

    }

}
