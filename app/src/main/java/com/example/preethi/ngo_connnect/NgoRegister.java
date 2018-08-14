package com.example.preethi.ngo_connnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.preethi.ngo_connnect.ImageManager.ListImages;
import static com.example.preethi.ngo_connnect.NgoLogin.MyPREFERENCES;
import static droidninja.filepicker.FilePickerConst.REQUEST_CODE;
import static java.lang.Integer.parseInt;

public class NgoRegister extends AppCompatActivity {Button register, nc_certificate, nc_pictures , nc_selectpic;
    SharedPreferences sharedpreferences;
    private EditText nc_name, nc_address, nc_experience, nc_phone, nc_email, nc_password, nc_confirm_password, nc_weblink,  nc_domain, nc_description , nc_accnum , nc_accname , nc_ifsccode , nc_branch;
    private String name, address, experience, phone, email, password, confirm_password, weblink, facebook, linkedin, domain, description , poster , accnum , accname , ifsccode , branch;
    private static final int PICK_IMAGE = 5;
    String imageName;
    ImageView iv;
    private Uri imageUri;
    private ImageView imageView;
    ImageView nc_poster;
    private Button uploadImageButton;
    Uri ImageUri;
    private static final int SELECT_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_register);


        System.out.println("In class ");

        Button selectImageButton = (Button) findViewById(R.id.selectpic);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageFromGallery();
            }
        });

        this.uploadImageButton = (Button) findViewById(R.id.uploadpic);
        this.uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
        this.uploadImageButton.setEnabled(false);
        this.imageView = (ImageView) findViewById(R.id.imageView2);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        register = (Button) findViewById(R.id.register);
        nc_name = (EditText) findViewById(R.id.Name);
        nc_address = (EditText) findViewById(R.id.postalAddress);
        nc_experience = (EditText) findViewById(R.id.experience);
        nc_phone = (EditText) findViewById(R.id.contact);
        nc_email = (EditText) findViewById(R.id.emailid);
        nc_password = (EditText) findViewById(R.id.password);
        nc_confirm_password = (EditText) findViewById(R.id.conpass);
        nc_domain = (EditText) findViewById(R.id.domain);
        nc_description = (EditText) findViewById(R.id.description);
        nc_selectpic = (Button) findViewById(R.id.selectpic);
        nc_pictures = (Button) findViewById(R.id.uploadpic);
        nc_weblink = (EditText) findViewById(R.id.website);
        nc_poster = (ImageView) findViewById(R.id.imageView2);
        nc_accnum = (EditText) findViewById(R.id.currentaccnum);
        nc_accname = (EditText) findViewById(R.id.accholdername);
        nc_ifsccode = (EditText) findViewById(R.id.ifsccode);
        nc_branch = (EditText) findViewById(R.id.branchname);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoSignup doSignup = new DoSignup();
                doSignup.execute();

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
                                Toast.makeText(NgoRegister.this, "Image Uploaded Successfully. Name = " + imageName, Toast.LENGTH_SHORT).show();
                                System.out.println("Name = " + imageName );
                            }
                        });
                    }
                    catch(Exception ex) {
                        final String exceptionMessage = ex.getMessage();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(NgoRegister.this, exceptionMessage, Toast.LENGTH_SHORT).show();
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



    public void done(View v) {
        register();
        DoSignup doSignup = new DoSignup();
        doSignup.execute();
        System.out.print("In done method");


    }

    public void register() {
        intialize();
        if(!validate()){
            Toast.makeText(this, "Signup has Failed", Toast.LENGTH_SHORT).show();
            System.out.println("In register method");

        }
    }

    public boolean validate() {
        boolean valid = true;

        if(name.isEmpty() ) {
            nc_name.setError("Please Enter valid name");
            valid = false;
        }

        if(address.isEmpty() || address.length() < 10) {
            nc_address.setError("Please enter full address");
            valid = false;
        }

        if(experience.isEmpty()) {
            nc_experience.setError("Please enter your experience");
            valid = false;
        }

        if(phone.isEmpty() || phone.length() < 10 ) {
            nc_phone.setError("Please enter a valid number");
            valid = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            nc_email.setError("Please Enter valid Email Address");
            valid = false;
        }

        if(domain.isEmpty()) {
            nc_domain.setError("Please enter your domain");
            valid = false;
        }

        if(description.isEmpty()) {
            nc_description.setError("Please describe your NGO");
            valid = false;
        }

        if(password.isEmpty() || password.length() < 8) {
            nc_password.setError("Please Enter valid password");
            valid = false;
        }

        if(confirm_password.isEmpty() || !(confirm_password.equals(password))) {
            nc_confirm_password.setError("Please Enter same as password");
            valid = false;
        }

        if(accnum.isEmpty()) {
            nc_accnum.setError("Please provide your website");
            valid = false;
        }

        if(accname.isEmpty()) {
            nc_accname.setError("Please provide your website");
            valid = false;
        }

        if(ifsccode.isEmpty()) {
            nc_ifsccode.setError("Please provide your website");
            valid = false;
        }

        if(branch.isEmpty()) {
            nc_branch.setError("Please provide your website");
            valid = false;
        }

        if(weblink.isEmpty()) {
            nc_certificate.setError("Please provide your website");
            valid = false;
        }

        System.out.print("In validate method");

        return valid;
    }

    public void intialize() {

        name = nc_name.getText().toString().trim();
        address = nc_address.getText().toString().trim();
        experience = nc_experience.getText().toString().trim();
        phone = nc_phone.getText().toString().trim();
        email = nc_email.getText().toString().trim();
        password = nc_password.getText().toString().trim();
        confirm_password = nc_confirm_password.getText().toString().trim();
        domain = nc_domain.getText().toString().trim();
        description = nc_description.getText().toString().trim();
        weblink = nc_weblink.getText().toString().trim();
        accname = nc_accname.getText().toString().trim();
        accnum = nc_accnum.getText().toString().trim();
        ifsccode = nc_ifsccode.getText().toString().trim();
        branch = nc_branch.getText().toString().trim();

        System.out.print("In initalize method");

    }


    public class DoSignup extends AsyncTask<String,Void,String> {
        String z = "";
        Boolean isSuccess = false;
        String name = nc_name.getText().toString().trim();
        String address = nc_address.getText().toString().trim();
        String experience = nc_experience.getText().toString().trim();
        String phone = nc_phone.getText().toString().trim();
        String email = nc_email.getText().toString().trim();
        String password = nc_password.getText().toString().trim();
        String domain = nc_domain.getText().toString().trim();
        String weblink = nc_weblink.getText().toString().trim();
        String accname = nc_accname.getText().toString().trim();
        String accnum = nc_accnum.getText().toString().trim();
        String ifsccode = nc_ifsccode.getText().toString().trim();
        String branch = nc_branch.getText().toString().trim();
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(NgoRegister.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(NgoRegister.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Intent i = new Intent(NgoRegister.this, NgoLogin.class);
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
                System.out.println("Connection Established to database");
                if (con == null) {
                    z = "Error in connection with SQL server";
                    System.out.println("Connection Error!!");
                } else {

                    String query = "select * from NGO where ngo_name LIKE '" + name + "' and phone LIKE '"+ phone +"'";
                    System.out.println(query);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        z = "User Already Exists";
                        isSuccess = false;

                    } else {
                        int flag = stmt.executeUpdate("insert into NGO (ngo_name, ngo_address , exp , phone , email ,  pwd , weblink , domain , picture , account_num , accholdername , ifsccode , branch) values('" + name + "' , '" + address + "' , '" + experience + "' , '"+ phone +"' , '" + email + "' , '" + password + "'  , '" + weblink + "'  , '" + domain + "' , '" + imageName + "' , '"+ accnum +"' , '"+ accname +"' , '"+ ifsccode +"' , '"+ branch +"' );");
                        System.out.println(flag);
                        z = "Succesfully Registered";
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
}
