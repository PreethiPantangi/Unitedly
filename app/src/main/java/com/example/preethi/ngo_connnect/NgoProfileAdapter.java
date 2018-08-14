package com.example.preethi.ngo_connnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.preethi.NGO_Connect.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Preethi on 12-08-2018.
 */

public class NgoProfileAdapter extends BaseAdapter implements ListAdapter {
    Context context;
    ArrayList<String> UserName;
    ArrayList<String> Description;
    ArrayList<String> Address;
    ArrayList<String> Email;
    ArrayList<String> Contact;
    ArrayList<String> Experience;
    ArrayList<String> Website;
    ArrayList<String> Poster;
    Holder holder;


    public NgoProfileAdapter(
            Context context2,
            ArrayList<String> userName,
            ArrayList<String> description ,
            ArrayList<String> address,
            ArrayList<String> email,
            ArrayList<String> contact,
            ArrayList<String> experience ,
            ArrayList<String> website ,
            ArrayList<String> poster

    )
    {


        this.context = context2;
        this.UserName = userName;
        this.Description = description;
        this.Address = address;
        this.Email = email;
        this.Contact = contact;
        this.Experience = experience;
        this.Website = website;
        this.Poster = poster;

    }
    public int getCount() {
        // TODO Auto-generated method stub
        return UserName.size();
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.profile, null);

            holder = new Holder();

            holder.username = (TextView) child.findViewById(R.id.user_profile_name);
            holder.description = (TextView) child.findViewById(R.id.user_profile_short_bio);
            holder.address = (TextView) child.findViewById(R.id.address_profile);
            holder.email = (TextView) child.findViewById(R.id.email_profile);
            holder.contact = (TextView) child.findViewById(R.id.contact_profile);
            holder.experience = (TextView) child.findViewById(R.id.experience_profile);
            holder.website = (TextView) child.findViewById(R.id.website_profile);
            holder.poster = (ImageView) child.findViewById(R.id.header_cover_image);
            child.setTag(holder);

        } else {

            holder = (NgoProfileAdapter.Holder) child.getTag();
        }
        holder.username.setText(UserName.get(position));
        holder.description.setText(Description.get(position));
        holder.address.setText(Address.get(position));
        holder.email.setText(Email.get(position));
        holder.contact.setText(Contact.get(position));
        holder.experience.setText(Experience.get(position));
        holder.website.setText(Website.get(position));

        try {

            final String imageName = Poster.get(position);
            final ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            final Handler handler = new Handler();
            Thread th = new Thread(new Runnable() {
                public void run() {
                    try {
                        long imageLength = 0;
                        ImageManager.GetImage(imageName , imageStream, imageLength);
                        handler.post(new Runnable() {
                            public void run() {
                                byte[] buffer = imageStream.toByteArray();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                                holder.poster.setImageBitmap(bitmap);
                            }
                        });
                    }
                    catch(Exception ex) {
                        System.out.println("Exception");
                    }
                }});
            th.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return child;
    }
    public class Holder {
        TextView username;
        TextView description;
        TextView address;
        TextView email;
        TextView contact;
        TextView experience;
        TextView website;
        ImageView poster;
    }
}