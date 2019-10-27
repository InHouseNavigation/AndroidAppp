package com.syntaxerror.naviapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by User on 3/14/2017.
 */

public class ListenAdapter extends ArrayAdapter<ListenKategories> {

    private static final String TAG = "ListenAdapter";

    private Context mContext;
    private int mResource;

    /**
     * Holds variables in a View
     */


    public ListenAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListenKategories> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        int drawable = getItem(position).getImgURL();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tVName = (TextView) convertView.findViewById(R.id.txtTitle);
        ImageView imageView = convertView.findViewById(R.id.imgIcon);

        imageView.setImageDrawable(mContext.getDrawable(drawable));
        tVName.setText(name);

        return convertView;
    }
}