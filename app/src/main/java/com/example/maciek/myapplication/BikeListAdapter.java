package com.example.maciek.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maciek on 09/02/2017.
 */

public class BikeListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Bike> bikelist;

    public BikeListAdapter(Context context, int layout, ArrayList<Bike> bikelist) {
        this.context = context;
        this.layout = layout;
        this.bikelist = bikelist;
    }

    @Override
    public int getCount() {

        return bikelist.size();
    }

    @Override
    public Object getItem(int position) {
        return bikelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView bike_name, bike_make, bike_model , bike_frame , bike_des;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.bike_name = (TextView) row.findViewById(R.id.bikename);
            holder.bike_make = (TextView) row.findViewById(R.id.make);
            holder.bike_model = (TextView) row.findViewById(R.id.model);
            holder.bike_frame = (TextView) row.findViewById(R.id.frameno);
            holder.bike_des = (TextView) row.findViewById(R.id.description);
            holder.imageView = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }


        Bike bike = bikelist.get(position);

        holder.bike_name.setText(bike.getName());
        holder.bike_make.setText(bike.getMake());
        holder.bike_model.setText(bike.getModel());
        holder.bike_frame.setText(bike.getFrame_no());
        holder.bike_des.setText(bike.getDesc());

        byte [] bikeImage = bike.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage,0,bikeImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}
