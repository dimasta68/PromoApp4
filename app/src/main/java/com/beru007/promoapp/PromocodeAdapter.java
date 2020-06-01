package com.beru007.promoapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.promoapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class PromocodeAdapter extends ArrayAdapter<HashMap<String, String>> {
    Activity activity;
    private ArrayList<HashMap<String, String>> productsList;
    private LayoutInflater layoutInflater;

    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "title";
    private static final String TAG_END = "end_skidka";
    private static final String TAG_SKIDKA = "skidka";
    private static final String TAG_RATING = "rating";
    private static final String TAG_LINK = "links";
    private static final String TAG_PROMO = "decsript";

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    public PromocodeAdapter(Activity activity, ArrayList<HashMap<String, String>> productsList) {
        super(activity,R.layout.list_item,productsList);
        this.activity=activity;
        this.productsList = productsList;


    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return productsList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> map = productsList.get(position);
        ItemViewHolder viewHolder;

        View view = convertView;
        if (view == null) {
            viewHolder = new ItemViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item, null, true);
            viewHolder.pid = (TextView) convertView.findViewById(R.id.pid);
            viewHolder.namae = (TextView) convertView.findViewById(R.id.name);
            viewHolder.skid = (TextView) convertView.findViewById(R.id.skid);
            viewHolder.desriptionsidka = (TextView) convertView.findViewById(R.id.desriptionsidka);
            viewHolder.link = (TextView) convertView.findViewById(R.id.link);
            viewHolder.promocode = (TextView) convertView.findViewById(R.id.promocode);
            viewHolder.rating = (TextView) convertView.findViewById(R.id.rating);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        viewHolder.pid.setText(map.get(TAG_PID));
        viewHolder.namae.setText(map.get(TAG_NAME));
        viewHolder.skid.setText(map.get(TAG_SKIDKA));
        viewHolder.desriptionsidka.setText(map.get(TAG_END));
        viewHolder.rating.setText(map.get(TAG_RATING));
        viewHolder.link.setText(map.get(TAG_LINK));
        viewHolder.promocode.setText(map.get(TAG_PROMO));


        return convertView;
    }

    public class ItemViewHolder {
        TextView pid;
        TextView namae;
        TextView skid;
        TextView desriptionsidka;
        TextView link;
        TextView promocode;
        TextView rating;
    }
}
