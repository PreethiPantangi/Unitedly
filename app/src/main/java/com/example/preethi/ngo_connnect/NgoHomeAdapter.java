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

public class NgoHomeAdapter extends BaseAdapter implements ListAdapter {

    Holder holder;

    Context context;
    ArrayList<String> Events;
    ArrayList<String> Category;
    ArrayList<String> Posters;
    ArrayList<String> EventDate;
    Bitmap bitmap;
    public NgoHomeAdapter(
            Context context2,
            ArrayList<String> event_name,
            ArrayList<String> category,
            ArrayList<String> poster,
            ArrayList<String> eventdate
    )
    {

        this.context = context2;
        this.Events = event_name;
        this.Category = category;
        this.Posters = poster;
        this.EventDate = eventdate;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Events.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Events.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public View getView(final int position, View child, ViewGroup parent) {


        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.ngohomepage, null);

            holder = new Holder();

            holder.eventname = (TextView) child.findViewById(R.id.ngoeventname);
            holder.category = (TextView) child.findViewById(R.id.ngongoname);
            holder.eventdate = (TextView) child.findViewById(R.id.ngoeventdate);
            holder.poster = (ImageView) child.findViewById(R.id.ngongoposter);
            child.setTag(holder);

        } else {
            holder = (Holder) child.getTag();
        }
        holder.eventname.setText(Events.get(position));
        holder.category.setText(Category.get(position));
        holder.eventdate.setText(EventDate.get(position));

        final String imageName = Posters.get(position);

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
                            }
                        });
                        holder.poster.setImageBitmap(bitmap);
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }});
            th.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Button knowmore = (Button) child.findViewById(R.id.ngoeventknowmore);

        final String pos = Events.get(position);
        System.out.println("In data adapter : " + pos);


        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("In Click Method - Event : " + Events.get(position));
                System.out.println("In Click Method - NGO :" + Category.get(position));
                Intent intent = new Intent(view.getContext() , NgoEventDetails.class);
                intent.putExtra("eventName" , Events.get(position));
                intent.putExtra("ngoName" , Category.get(position));
                context.startActivity(intent);
            }
        });
        return child;
    }

    public class Holder {
        TextView eventname;
        TextView category;
        TextView eventdate;
        ImageView poster;
    }
}