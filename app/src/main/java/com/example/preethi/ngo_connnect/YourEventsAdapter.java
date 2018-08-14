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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preethi on 09-08-2018.
 */

public class YourEventsAdapter extends BaseAdapter implements ListAdapter {

    Holder holder;

    Context context;
    ArrayList<String> YourEvents;
    ArrayList<String> YourPosters;
    ArrayList<String> Data;
    Bitmap bitmap;
    public YourEventsAdapter(
            Context context2,
            ArrayList<String> your_event_name,
            ArrayList<String> your_event_poster,
            ArrayList<String> data
    )
    {

        this.context = context2;
        this.YourEvents = your_event_name;
        this.YourPosters = your_event_poster;
        this.Data = data;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return YourEvents.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return YourEvents.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public View getView(final int position, View child, ViewGroup parent) {


        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.yourevents, null);

            holder = new Holder();

            holder.eventname = (TextView) child.findViewById(R.id.youreventname);
            holder.poster = (ImageView) child.findViewById(R.id.youreventimage);
            child.setTag(holder);

        } else {
            holder = (Holder) child.getTag();
        }

        if(YourEvents.size() == 0) {
            holder.eventname.setText("You do not have any previous or current events been hosted");
        }

        holder.eventname.setText(YourEvents.get(position));

        final String imageName = YourPosters.get(position);

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

        final Button knowmore = (Button) child.findViewById(R.id.yourknowmore);

        final String pos = YourEvents.get(position);
        System.out.println("In data adapter : " + pos);


        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("In Click Method - Event : " + YourEvents.get(position));
                Intent intent = new Intent(view.getContext() , NgoEventData.class);
                intent.putExtra("yourEventName" , YourEvents.get(position));
                context.startActivity(intent);
            }
        });

        return child;
    }

    public class Holder {
        TextView eventname;
        ImageView poster;
        TextView joineecount;
        TextView sponsorcount;
    }
}