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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.preethi.ngo_connnect.NgoLogin.MyPREFERENCES;

public class Join extends AppCompatActivity {
    TextView  accnum , accname , ifsc , branch , cost;
    String eventName, ngoName, usname;
    Button join;
    private String ev_name, ev_joinee, ev_email, ev_contact, ev_designation, ev_ngo, ev_type;//, nc_password, nc_confirm_password, nc_weblink, nc_facebook, nc_linkedin, nc_domain, nc_description;
    private TextView tv, tv1;
    private EditText joinee, email, contact, designation , referenceid;
    RadioGroup joinAs;
    RadioButton vol, part;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        tv = (TextView) findViewById(R.id.event);
        tv1 = (TextView) findViewById(R.id.ngo);
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        ngoName = bundle.getString("ngoName");
        System.out.println("Event Name and details!");
        System.out.println(eventName);
        System.out.println(ngoName);
        tv.setText(eventName);
        tv1.setText(ngoName);


        joinee = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.contact);
        designation = (EditText) findViewById(R.id.designation);
        joinAs = (RadioGroup) findViewById(R.id.joinAs);
        vol = (RadioButton) findViewById(R.id.volunteer);
        part = (RadioButton) findViewById(R.id.participant);
        join = (Button) findViewById(R.id.join);
        referenceid = (EditText) findViewById(R.id.referenceid);


        accnum = (TextView) findViewById(R.id.accnumber);
        accname = (TextView) findViewById(R.id.accname);
        ifsc = (TextView) findViewById(R.id.joinaccifsccode);
        branch = (TextView) findViewById(R.id.joinaccbranch);
        cost= (TextView) findViewById(R.id.entryamount);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        accnum.setText(sharedPreferences.getString("accnum" , null));
        accname.setText(sharedPreferences.getString("accname" , null));
        ifsc.setText(sharedPreferences.getString("ifcs" , null));
        branch.setText(sharedPreferences.getString("branch" , null));
        cost.setText(sharedPreferences.getString("amount" , null));
        System.out.println(sharedPreferences.getString("amount" , null));

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Join.JoinForEvent joinerDetails = new Join.JoinForEvent();
                joinerDetails.execute("");
                sendMail();
            }
        });
    }

    public void sendMail() {
        final ProgressDialog dialog = new ProgressDialog(Join.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("team.unitedly@gmail.com", "unitedly.team5");
                    sender.sendMail("Thank you for joining us!",
                            joinee.getText().toString() + ", thank you for joining the event " + eventName + ".Your " + referenceid.getText().toString() + " Has been sent to the NGO for verification. Looking forward to see you on the big day!",
                            "team.unitedly@gmail.com",
                             email.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    public boolean validate() {
        boolean valid = true;

        if (ev_joinee.isEmpty()) {
            joinee.setError("Please enter your full name!");
            valid = false;
        }

        if(ev_email.isEmpty()) {
            email.setError("Enter your email!");
            valid = false;
        }

        if(ev_contact.isEmpty() || contact.length() < 10 ) {
            contact.setError("Please enter a valid number");
            valid = false;
        }

        if(ev_designation.isEmpty() ) {
            designation.setError("Please enter designation!");
            valid = false;
        }

        return valid;
    }

    public class JoinForEvent extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        Boolean isSuccess = false;
        String eventJoined = tv.getText().toString().trim();
        String ngoJoined = tv1.getText().toString().trim();
        String joineeName = joinee.getText().toString().trim();
        String emailid = email.getText().toString().trim();
        String contactno = contact.getText().toString().trim();
        String desig = designation.getText().toString().trim();
        String value = ((RadioButton) findViewById(joinAs.getCheckedRadioButtonId())).getText().toString();
        String refid = referenceid.getText().toString().trim();


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Join.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected void onPostExecute(String r) {
            Toast.makeText(Join.this, r, LENGTH_SHORT).show();

            if (isSuccess) {
                Intent i = new Intent(Join.this, UserHome.class);
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
                String query1 = "select * from Joinee where phone = '" + contactno + "' and email = '" + emailid + "'";
                String query = "insert into Joinee(joinee_name, email, phone , designation, join_as , event_name , ngo_name , referenceid) values( '" + joineeName + "','" + emailid + "','" + contactno + "','" + desig + "','" + value + "' , '" + eventJoined +"' , '"+ ngoJoined +"' , '"+ refid +"' )";
                System.out.println(query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query1);
                if (rs.next()) {
                    Toast.makeText(getBaseContext(), "Already registered!", LENGTH_LONG).show();
                } else {
                    int flag = stmt.executeUpdate(query);
                    System.out.println(flag);
                    System.out.println(contactno);
                    isSuccess = true;
                    System.out.println("User joined!!");
                    Toast.makeText(getBaseContext(), "Successfully Registered for the event!!", LENGTH_LONG).show();
                    try {
                        GMailSender sender = new GMailSender("team.unitedly@gmail.com", "unitedly.team5");
                        sender.sendMail("Thank you for joining us!",
                                joinee.getText().toString() + ", thank you for joining the event " + eventName + ". Looking forward to see you on the big day!",
                                "team.unitedly@gmail.com",
                                email.getText().toString());
                    } catch (Exception e) {
                        Log.e("mylog", "Error: " + e.getMessage());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }

    }


}
