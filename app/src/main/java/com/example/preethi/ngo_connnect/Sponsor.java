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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.preethi.ngo_connnect.NgoLogin.MyPREFERENCES;

public class Sponsor extends AppCompatActivity {

    TextView eventname , ngoname , accnum , accname , ifsc , branch;
    EditText sponsorername , sponsoreramt , sponsoreremail , sponsorercontact , referenceid;
    Button sponsor;
    String ngoName , eventName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);

        eventname = (TextView) findViewById(R.id.sponsoreventname);
        ngoname = (TextView) findViewById(R.id.sponsorngoname);
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        ngoName = bundle.getString("ngoName");
        System.out.println("Event Name and details!");
        System.out.println(eventName);
        System.out.println(ngoName);
        eventname.setText(eventName);
        ngoname.setText(ngoName);

        accnum = (TextView) findViewById(R.id.accountnumber);
        accname = (TextView) findViewById(R.id.accountname);
        ifsc = (TextView) findViewById(R.id.accifsccode);
        branch = (TextView) findViewById(R.id.accbranch);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String accno = sharedPreferences.getString("accnum" , null);
        String name = sharedPreferences.getString("accname" , null);
        String ifsccode = sharedPreferences.getString("ifcs" , null);
        String accbranch = sharedPreferences.getString("branch" , null);
        System.out.println(accno + " " + name + " " + ifsccode +" " + accbranch);
        accnum.setText(accno);
        accname.setText(name);
        ifsc.setText(ifsccode);
        branch.setText(accbranch);

        sponsorername = (EditText) findViewById(R.id.sname);
        sponsoreramt = (EditText) findViewById(R.id.samount);
        sponsoreremail = (EditText) findViewById(R.id.semail);
        sponsorercontact = (EditText) findViewById(R.id.scontact);
        sponsor = (Button) findViewById(R.id.sponsorevent);
        referenceid = (EditText) findViewById(R.id.sreferenceid);

        System.out.println("In Sponsored Class : " + sponsorername.getText().toString());

        sponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sponsor.SponsorEvent sponsorerDetails = new Sponsor.SponsorEvent();
                sponsorerDetails.execute("");
                sendMail();

            }
        });
    }

    public void sendMail() {
        final ProgressDialog dialog = new ProgressDialog(Sponsor.this);
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("team.unitedly@gmail.com", "unitedly.team5");
                    sender.sendMail("Confirmation Mail",
                            sponsorername.getText().toString() + ", thank you for sponsoring " + sponsoreramt.getText().toString() + " to the event " + eventname + ". Your Reference ID " + referenceid.getText().toString() + " has been sent to the NGO.",
                            "team.unitedly@gmail.com",
                            sponsoreremail.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    public class SponsorEvent extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        Boolean isSuccess = false;
        String event = eventname.getText().toString().trim();
        String ngo = ngoname.getText().toString().trim();
        String sponsorerName = sponsorername.getText().toString().trim();
        String sponsoreramountt = sponsoreramt.getText().toString().trim();
        String sponsoreremailid = sponsoreremail.getText().toString().trim();
        String sponsorercontactno = sponsorercontact.getText().toString().trim();
        String refid = referenceid.getText().toString().trim();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Sponsor.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected void onPostExecute(String r) {
            Toast.makeText(Sponsor.this, r, LENGTH_SHORT).show();

            if (isSuccess) {
                Intent i = new Intent(Sponsor.this, UserHome.class);
                startActivity(i);
                finish();
            }
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");
                String query1 = "select * from sponsor where phone = '" + sponsorercontact + "' and sponsoreremail = '" + sponsoreremail + "'";
                String query = "insert into sponsor(event_name , ngo_name  , sponsorername , sponsoreremail , phone , event_amt , referenceid) values( '" + event + "' , ' " + ngo + " ' ,'" + sponsorerName + "','" + sponsoreremailid + "','" + sponsorercontactno + "' ,'" + sponsoreramountt + "' , '" + refid + "' )";

                System.out.println(query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query1);
                if (rs.next()) {
                    Toast.makeText(getBaseContext(), "Already registered!", LENGTH_LONG).show();
                } else {
                    int flag = stmt.executeUpdate(query);
                    System.out.println(flag);
                    System.out.println(sponsorercontactno);
                    stmt.executeQuery(query1);
                    isSuccess = true;
                    System.out.println("Sponsored");
                    Toast.makeText(getBaseContext(), "Successfully Sponsored for the event!!", LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }

    }

}
