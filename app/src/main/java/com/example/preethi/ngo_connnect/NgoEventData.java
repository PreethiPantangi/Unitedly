package com.example.preethi.ngo_connnect;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NgoEventData extends AppCompatActivity {

    ArrayList<String> eventname = new ArrayList<>();
    ArrayList<String> poster = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    NgoEventDataAdapter ngoEventDataAdapter;
    String yourEventName;
    ListView LISTVIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_event_data);

        LISTVIEW = (ListView) findViewById(R.id.ngoeventdata);

        NgoEventData.DoGetEventData doGetEventData = new NgoEventData.DoGetEventData();
        doGetEventData.execute("");

        Bundle bundle = getIntent().getExtras();
        yourEventName = bundle.getString("yourEventName");

    }

    public class DoGetEventData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        protected void onPostExecute(String r) {
            LISTVIEW.setAdapter(ngoEventDataAdapter);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");

                Statement stmt2 = con.createStatement();
                String query2 = " select Events.event_name , Events.poster  from Events where Events.event_name = '"+yourEventName+"' ";
                System.out.println(query2);
                ResultSet resultSet = stmt2.executeQuery(query2);
                while (resultSet.next()) {
                    eventname.add(resultSet.getString("event_name"));
                    poster.add(resultSet.getString("poster"));

                    String query1 = "select count(*) as total from Joinee where event_name = '"+ resultSet.getString("event_name") +"'";
                    System.out.println(query1);
                    String query = "select count(*) as sponsor from sponsor where event_name = '"+ resultSet.getString("event_name") +"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs2 = stmt.executeQuery(query);
                    Statement stmt1 = con.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(query1);
                    if(rs1.next()) {
                        data.add(Integer.toString(rs1.getInt(1)));
                    }
                    if(rs2.next()) {
                        data.add(Integer.toString(rs2.getInt(1)));
                    }

                }
                System.out.println("Event Name : " + eventname);
                System.out.println("Poster : " + poster);
                System.out.println("Joinee : " + data);

                ngoEventDataAdapter = new NgoEventDataAdapter(NgoEventData.this,
                        eventname,
                        poster,
                        data
                );

            } catch (Exception ex) {
                Log.e("ERROR", ex.getMessage());
            }
            return "";
        }
    }
}
