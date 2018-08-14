package com.example.preethi.ngo_connnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.preethi.ngo_connnect.NgoLogin.MyPREFERENCES;

public class NgoEventDetails extends AppCompatActivity {
    ArrayList<String> event_name;
    ArrayList<String> category;
    String eventName;
    ListView LISTVIEW;
    SharedPreferences sharedPreferences;
    ResultSet rs;
    NgoEventDetailsAdapter eventDetailsAdapter;
    ArrayList<String> EventName_ArrayList = new ArrayList<String>();
    ArrayList<String> Amount_ArrayList = new ArrayList<String>();
    ArrayList<String> Organisation_ArrayList = new ArrayList<String>();
    ArrayList<String> Location_ArrayList = new ArrayList<String>();
    ArrayList<String> Date_ArrayList = new ArrayList<String>();
    ArrayList<String> Time_ArrayList = new ArrayList<String>();
    ArrayList<String> Description_ArrayList = new ArrayList<String>();
    ArrayList<String> Poster_ArrayList = new ArrayList<String>();
    int flag = 0;
    String email , org;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        System.out.println(eventName);
        email = sharedPreferences.getString("Email" , null);
        org = sharedPreferences.getString("organisation" , null);



        LISTVIEW = (ListView) findViewById(R.id.eventdetailslist);
        NgoEventDetails.DisplayEventDetails doGetEventsList = new NgoEventDetails.DisplayEventDetails();
        doGetEventsList.execute("");
    }

    public void collaborate(View view) {

        final Button collaborate = (Button) findViewById(R.id.collaborate);

        collaborate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }



    public void sendMail() {
        System.out.println("Sending mail");
        final ProgressDialog dialog = new ProgressDialog(NgoEventDetails.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        TextView autoCompleteTextView = (TextView) findViewById(R.id.ngoeventnamedetail);
        final String joinEvent = autoCompleteTextView.getText().toString();
        TextView autoCompleteTextView1 = (TextView) findViewById(R.id.ngonamedetail);
        final String joinEventNgo = autoCompleteTextView1.getText().toString();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("team.unitedly@gmail.com", "unitedly.team5");
                    sender.sendMail("Confirmation Mail",
                             org + ", thank you for for your request to collaborate with the event " + joinEvent + ".",
                            "team.unitedly@gmail.com",
                            email);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
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

                String query = "select event_name , org , cost , loc , event_desc , event_time , event_date , poster from Events where event_name='"+eventName+"'";
                System.out.println(query);
                Statement stmt = con.createStatement();
                System.out.println("After Statement stmt");
                rs = stmt.executeQuery(query);
                System.out.println("Executing ResultSet statement");

                String query1 = "select NGO.email from NGO , Events where Events.org  = NGO.ngo_name";

                while (rs.next()) {
                    flag = 1;
                    EventName_ArrayList.add(rs.getString("event_name"));
                    Organisation_ArrayList.add(rs.getString("org"));
                    Location_ArrayList.add(rs.getString("loc"));
                    Date_ArrayList.add(rs.getString("event_date"));
                    Time_ArrayList.add(rs.getString("event_time"));
                    Description_ArrayList.add(rs.getString("event_desc"));
                    Poster_ArrayList.add(rs.getString("poster"));

                }
                if(flag == 0){
                    Toast.makeText(NgoEventDetails.this, "No details to show", Toast.LENGTH_SHORT).show();

                }

                eventDetailsAdapter = new NgoEventDetailsAdapter(NgoEventDetails.this,
                        EventName_ArrayList,
                        Organisation_ArrayList,
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
