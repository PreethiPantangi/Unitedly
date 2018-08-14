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

public class NgoAdapter extends BaseAdapter implements ListAdapter {

    Holder holder;
    Bitmap bitmap;

    Context context;
    ArrayList<String> Ngos;
    ArrayList<String> Domains;
    ArrayList<String> Poster;
    public NgoAdapter(
            Context context2,
            ArrayList<String> ngo_name,
            ArrayList<String> domains,
            ArrayList<String> poster
    )
    {

        this.context = context2;
        this.Ngos = ngo_name;
        this.Domains = domains;
        this.Poster = poster;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Ngos.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Ngos.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public View getView(final int position, View child, ViewGroup parent) {


        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.userngoslist ,  null);

            holder = new Holder();

            holder.eventname = (TextView) child.findViewById(R.id.userngoname);
            holder.domain = (TextView) child.findViewById(R.id.userdomainname);
            holder.poster = (ImageView) child.findViewById(R.id.ngoposter);

            child.setTag(holder);

        } else {
            holder = (Holder) child.getTag();
        }

        holder.eventname.setText(Ngos.get(position));
        holder.domain.setText(Domains.get(position));

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

        final Button ngoknowmore = (Button) child.findViewById(R.id.userknowmore);


        ngoknowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("In Click Method - Event : " + Ngos.get(position));
                Intent intent = new Intent(view.getContext() , NgoDetailsProfile.class);
                intent.putExtra("org" , Ngos.get(position));
                context.startActivity(intent);
            }
        });

        return child;
    }

    public class Holder {
        TextView eventname;
        TextView domain;
        ImageView poster;
    }
}