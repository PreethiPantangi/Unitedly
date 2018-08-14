package com.example.preethi.ngo_connnect;

/**
 * Created by Preethi on 13-08-2018.
 */

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.preethi.NGO_Connect.R;

    public class MailOperation extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

//            String email = getIntent().getStringExtra("Email");
//            String event = getIntent().getStringExtra("Event");
//            String name = getIntent().getStringExtra("Name");

            try {
                GMailSender sender = new GMailSender("team.unitedly@gmail.com", "unitedly.team5");
                sender.sendMail("Hearty Welcome!",
                        "You have successfully joined the event! Will see you on the big day!", "team.unitedly@gmail.com",
                        "pantangisaipreethi@gmail.com");
                //                   "mahathivavilala97@gmail.com, chasesaphira@gmail.com, anualek123@gmail.com, team.unitedly@gmail.com");
            } catch (Exception e) {
                Log.e("error", e.getMessage(), e);
                return "Email Not Sent";
            }
            return "Email Sent";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("MailOperation", result + "");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
