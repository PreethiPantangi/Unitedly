package com.example.preethi.ngo_connnect;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;
import com.example.preethi.ngo_connnect.NgoProfile;
import com.example.preethi.ngo_connnect.NgoRegister;

import java.sql.ResultSet;
import java.sql.Statement;

public class NgoLogin extends AppCompatActivity {

    TextView registerngo;
    Button ngologin;
    SharedPreferences sharedPreferences;
    EditText email , password;
    Button login;
    String email_id;
    Boolean check = false;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public  static final String CUSTOM_KEY = "emailid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_login);

        System.out.println("In NGO Login Class");

        email = (EditText) findViewById(R.id.ngoid);
        password = (EditText)findViewById(R.id.ngopass);

        registerngo = findViewById(R.id.ngoreg);
        registerngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNGO();
            }
        });

        ngologin = findViewById(R.id.ngologin);
        ngologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute();
            }
        });

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }


    public void ngoProfile() {
        Intent intent = new Intent(getBaseContext(), NgoProfile.class);
        startActivity(intent);
    }

    public void registerNGO() {
        Intent intent = new Intent(getBaseContext(), NgoRegister.class);
        startActivity(intent);
    }

    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        String role, cd;
        Boolean isSuccess = false;
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(NgoLogin.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent intent = new Intent(getApplicationContext() , NgoHome.class);
                startActivity(intent);
            }
        }


        @Override
        protected String doInBackground(String... params) {
            if(str_password.trim().equals("") ||str_email.trim().equals("")){
                z = "Please fill all the fields";
            }else{
                try {
                    java.sql.Connection con = DatabaseConnection.getConnection();
                    System.out.println(con);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    }else{
                        System.out.println("Connection : " + con);
                        String query = "select * from NGO where email LIKE '"+str_email+"' and pwd LIKE '"+str_password+"'";
                        System.out.println(query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next()){
                            z ="Login Successful!";
                            System.out.println("Done!");
                            isSuccess = true;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("EmailID", rs.getString("email"));
                            editor.putString("organisation" , rs.getString("ngo_name"));
                            editor.apply();
                        }else{
                            z = "Invalid Credentials";
                            System.out.println("Invalid!");
                            isSuccess = false;
                        }
                    }
                }catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("ERROR", ex.getMessage());

                }
            }
            return z;
        }
    }
}