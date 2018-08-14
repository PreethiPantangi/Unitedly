package com.example.preethi.ngo_connnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preethi.NGO_Connect.R;
import com.sun.mail.util.BASE64DecoderStream;

import android.app.ProgressDialog;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preethi on 09-08-2018.
 */

public class EventDetailsAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<String> EventName;
    ArrayList<String> Organisation;
    ArrayList<String> Amount;
    ArrayList<String> Venue;
    ArrayList<String> Date;
    ArrayList<String> Time;
    ArrayList<String> Descripion;
    ArrayList<String> Poster;
    Holder holder;


    public EventDetailsAdapter(
            Context context2,
            ArrayList<String> event_name,
            ArrayList<String> organisation ,
            ArrayList<String> amount,
            ArrayList<String> venue ,
            ArrayList<String> date ,
            ArrayList<String> time ,
            ArrayList<String> description,
            ArrayList<String> poster

    )
    {
        this.context = context2;
        this.EventName = event_name;
        this.Organisation = organisation;
        this.Amount = amount;
        this.Venue = venue;
        this.Date = date;
        this.Time = time;
        this.Descripion = description;
        this.Poster = poster;

    }
    public int getCount() {
        // TODO Auto-generated method stub
        return EventName.size();
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
            child = layoutInflater.inflate(R.layout.eventdetailslist, null);

            holder = new Holder();

            holder.eventname = (TextView) child.findViewById(R.id.event_name);
            holder.ngoname = (TextView) child.findViewById(R.id.ngo_name);
            holder.amount = (TextView) child.findViewById(R.id.amount);
            holder.venue = (TextView) child.findViewById(R.id.venue);
            holder.eventdate = (TextView) child.findViewById(R.id.eventdate);
            holder.eventtime = (TextView) child.findViewById(R.id.eventtime);
            holder.description = (TextView) child.findViewById(R.id.description);
            holder.poster = (ImageView) child.findViewById(R.id.poster);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.eventname.setText(EventName.get(position));
        holder.ngoname.setText(Organisation.get(position));
        holder.amount.setText(Amount.get(position));
        holder.venue.setText(Venue.get(position));
        holder.eventdate.setText(Date.get(position));
        holder.eventtime.setText(Time.get(position));
        holder.description.setText(Descripion.get(position));

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
        TextView eventname;
        TextView ngoname;
        TextView amount;
        TextView venue;
        TextView eventdate;
        TextView eventtime;
        TextView description;
        ImageView poster;
    }
}