package com.example.preethi.ngo_connnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.preethi.NGO_Connect.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Ngo extends AppCompatActivity {

    ListView LISTVIEW;
    TextView ngo_name , ngo_domain , ngo;
    NgoAdapter ngoAdapter;
    ArrayList<String> ngonames = new ArrayList<>();
    ArrayList<String> ngodomains = new ArrayList<>();
    ArrayList<String> poster = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        LISTVIEW = (ListView) findViewById(R.id.userngolistview);
        Ngo.DoGetNgos doGetNgos = new Ngo.DoGetNgos();
        doGetNgos.execute("");

        ngo_name = (TextView) findViewById(R.id.userngoname);
        ngo_domain = (TextView) findViewById(R.id.userdomainname);
    }


    public class DoGetNgos extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        protected void onPostExecute(String r) {
            LISTVIEW.setAdapter(ngoAdapter);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("Connection Established");
                String query = "select ngo_name , domain , picture from NGO";
                System.out.println(query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    ngonames.add(rs.getString("ngo_name"));
                    ngodomains.add(rs.getString("domain"));
                    poster.add(rs.getString("picture"));
                }

                System.out.println( ngodomains );

                ngoAdapter = new NgoAdapter(Ngo.this,
                        ngonames,
                        ngodomains,
                        poster
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
