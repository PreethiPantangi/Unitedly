package com.example.preethi.ngo_connnect;

import android.content.Context;
import android.content.Intent;
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

public class NgoEventDataAdapter extends BaseAdapter implements ListAdapter {

    Holder holder;
    Bitmap bitmap;

    Context context;
    ArrayList<String> Event;
    ArrayList<String> Poster;
    ArrayList<String> Data;
    public NgoEventDataAdapter(
            Context context2,
            ArrayList<String> event,
            ArrayList<String> poster,
            ArrayList<String> data
    )
    {

        this.context = context2;
        this.Event = event;
        this.Poster = poster;
        this.Data = data;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Event.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Event.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public View getView(final int position, View child, ViewGroup parent) {


        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.ngoeventdata, null);

            holder = new Holder();

            holder.eventname = (TextView) child.findViewById(R.id.eventdataeventname);
            holder.poster = (ImageView) child.findViewById(R.id.eventdataimage);
            holder.joinee = (TextView) child.findViewById(R.id.joineecount);
            holder.sponsor = (TextView) child.findViewById(R.id.sponsorcount);
            child.setTag(holder);

        } else {
            holder = (Holder) child.getTag();
        }
        holder.eventname.setText(Event.get(position));
        holder.joinee.setText(Data.get(0));
        holder.sponsor.setText(Data.get(1));

        final String imageName = Poster.get(position);

        try {
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
                                bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                                holder.poster.setImageBitmap(bitmap);
                            }
                        });
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
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
        ImageView poster;
        TextView joinee;
        TextView sponsor;
    }
}