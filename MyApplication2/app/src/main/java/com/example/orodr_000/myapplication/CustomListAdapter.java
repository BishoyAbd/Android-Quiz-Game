package com.example.orodr_000.myapplication;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private Activity context;
    private List<MenuItem> items;

    static class ViewHolder{
        public ImageView image;
        public TextView title;
        public  TextView points;
    }

    public CustomListAdapter(Activity context, List<MenuItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount(){
        return  items.size();
    }

    @Override
    public Object getItem(int location){
        return items.get(location);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View rowView=convertView;
        if(rowView==null){
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.list_row,null);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.title=(TextView)rowView.findViewById(R.id.quiz_title);
            viewHolder.image=(ImageView)rowView.findViewById(R.id.quiz_image);
            viewHolder.points=(TextView)rowView.findViewById(R.id.points);
            rowView.setTag(viewHolder);
        }
        MenuItem b=items.get(position);
        ViewHolder holder=(ViewHolder) rowView.getTag();
        holder.title.setText(b.getName());
        holder.title.setHint(b.getTheme());
        holder.points.setText(b.getPoints());
        int resID = rowView.getResources().getIdentifier("@drawable/"+b.getImage(), "drawable",rowView.getContext().getPackageName());
        holder.image.setImageResource(resID);
        holder.image.setTag(b.getImage());
        //holder.image.setId(vibrant);
        return rowView;
    }



}
