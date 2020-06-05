package com.beru007.promoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.promoapp.R;

import java.util.ArrayList;

public class RetroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView> dataModelArrayList;

    public RetroAdapter(Context context, ArrayList<ModelListView> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null, true);

          //  holder.iv = (ImageView) convertView.findViewById(R.id.pid);
            holder.pid = (TextView) convertView.findViewById(R.id.pid);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.links = (TextView) convertView.findViewById(R.id.link);
            holder.end_skidka = (TextView) convertView.findViewById(R.id.desriptionsidka);
            holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.skidka = (TextView) convertView.findViewById(R.id.skid);
            holder.promocode = (TextView) convertView.findViewById(R.id.promocode);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

       // Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.pid.setText(dataModelArrayList.get(position).getId());
        holder.name.setText(dataModelArrayList.get(position).getTitle());
        holder.links.setText(dataModelArrayList.get(position).getLinks());
        holder.end_skidka.setText(dataModelArrayList.get(position).getEnd_skidka());
        holder.rating.setText(dataModelArrayList.get(position).getRating());
        holder.skidka.setText(dataModelArrayList.get(position).getSkidka());
        holder.promocode.setText(dataModelArrayList.get(position).getDecsript());
     //   holder.tvcity.setText(dataModelArrayList.get(position).getEnd_skidka());

        return convertView;
    }

    private class ViewHolder {

        protected TextView pid, name, links,end_skidka,rating,skidka,promocode;
        protected ImageView iv;
    }

}
