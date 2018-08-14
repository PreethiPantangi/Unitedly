package com.example.preethi.ngo_connnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static droidninja.filepicker.FilePickerConst.REQUEST_CODE;

public class AddEvent extends AppCompatActivity {

    Button addevent , uploadpicture;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String name, amount, venue, date, time, description , contact;
    private EditText ev_name, ev_amount, ev_venue, ev_date, ev_time, ev_description , ev_contact;
    ImageView imagev;
    ImageView ev_poster;
    String email , imageName;
    private Button uploadImageButton;
    private ImageView imageView;
    Uri imageUri;
    private static final int SELECT_IMAGE = 100;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        System.out.println("In Add Event Class");

        Button selectImageButton = (Button) findViewById(R.id.selectposter);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageFromGallery();
            }
        });

        this.uploadImageButton = (Button) findViewById(R.id.uploadposter);
        this.uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
        this.uploadImageButton.setEnabled(false);
        this.imageView = (ImageView) findViewById(R.id.image);



        imagev = (ImageView) findViewById(R.id.image);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        addevent = (Button) findViewById(R.id.addevent);
        ev_name = (EditText) findViewById(R.id.event_name);
        ev_amount = (EditText) findViewById(R.id.amount);
        ev_venue = (EditText) findViewById(R.id.venue);
        ev_date = (EditText) findViewById(R.id.date);
        ev_time = (EditText) findViewById(R.id.time);
        ev_description = (EditText) findViewById(R.id.description);
        ev_poster = (ImageView) findViewById(R.id.image);
        ev_contact = (EditText) findViewById(R.id.event_contact);

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent.DoAddEvent addevent = new AddEvent.DoAddEvent();
                addevent.execute("");

            }
        });

    }

    private void SelectImageFromGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    private void UploadImage()
    {
        try {
            final InputStream imageStream = getContentResolver().openInputStream(this.imageUri);
            final int imageLength = imageStream.available();

            final Handler handler = new Handler();

            Thread th = new Thread(new Runnable() {
                public void run() {

                    try {

                        imageName = ImageManager.UploadImage(imageStream, imageLength);

                        handler.post(new Runnable() {

                            public void run() {
                                Toast.makeText(AddEvent.this, "Image Uploaded Successfully. Name = " + imageName, Toast.LENGTH_SHORT).show();
                                System.out.println("Name = " + imageName );
                            }
                        });
                    }
                    catch(Exception ex) {
                        final String exceptionMessage = ex.getMessage();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(AddEvent.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }});
            th.start();
        }
        catch(Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    this.imageUri = imageReturnedIntent.getData();
                    this.imageView.setImageURI(this.imageUri);
                    this.uploadImageButton.setEnabled(true);
                }
        }
    }


    public boolean validate() {
        boolean valid = true;

        if(name.isEmpty() ) {
            ev_name.setError("Please Enter valid name");
            valid = false;
        }

        if(venue.isEmpty()) {
            ev_venue.setError("Please enter the venue");
            valid = false;
        }

        if(date.isEmpty()) {
            ev_date.setError("Please enter the date ");
            valid = false;
        }

        if(time.isEmpty()) {
            ev_time.setError("Please Enter time");
            valid = false;
        }

        if(description.isEmpty()) {
            ev_description.setError("Please enter your domain");
            valid = false;
        }
        System.out.print("In validate method");

        return valid;
    }


    public class DoAddEvent extends AsyncTask<String,Void,String> {

        String z = "";
        Boolean isSuccess = false;
        String name = ev_name.getText().toString().trim();
        String amount = ev_amount.getText().toString().trim();
        String venue = ev_venue.getText().toString().trim();
        String date = ev_date.getText().toString().trim();
        String description = ev_description.getText().toString().trim();
        String contact = ev_contact.getText().toString().trim();
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AddEvent.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(AddEvent.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Intent i = new Intent(AddEvent.this, NgoHome.class);
                startActivity(i);
                finish();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                java.sql.Connection con = DatabaseConnection.getConnection();
                System.out.println("In Thread");
                System.out.println("Connection Established to database");
                if (con == null) {
                    z = "Error in connection with SQL server";
                    System.out.println("Connection Error!!");
                } else {
                    String query = "select * from Events where event_name LIKE '" + name + "'";
                    System.out.println(query);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        z = "Event Already Exists";
                        isSuccess = false;
                    } else {
                            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            String email = sharedPreferences.getString("Email","");
                            System.out.println(email);
                            String organisation = sharedPreferences.getString("organisation" , "");
                            System.out.println(organisation);

                            int flag = stmt.executeUpdate("insert into Events (event_name, org , cost , loc , event_desc , phone , event_time , event_date , poster) values('" + name + "' , '" + organisation + "' , '" + amount + "' , '" + venue + "' , '" + description + "'  , '"+ contact +"' , '"+ time +"' , '"+ date +"' , '" + imageName + "');");
                            System.out.println(flag);
                            z = "Event Added Succesfully";
                            isSuccess = true;
                            System.out.println("Added user!!");
                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
                Log.e("ERROR", ex.getMessage());

            }

            return z;
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
        } else  if(id == R.id.nav_addevent) {
            Intent intent1 = new Intent(getApplicationContext(), AddEvent.class);
            startActivity(intent1);
        } else if (id == R.id.nav_yourevent) {
            Intent intent1 = new Intent(getApplicationContext(), YourEvents.class);
            startActivity(intent1);
        } else if (id == R.id.nav_profile) {
            Intent intent1 = new Intent(getApplicationContext(), NgoProfile.class);
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }
}
