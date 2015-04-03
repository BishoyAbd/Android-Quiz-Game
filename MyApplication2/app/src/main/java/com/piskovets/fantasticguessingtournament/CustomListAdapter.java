package com.piskovets.fantasticguessingtournament;


import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;


public class CustomListAdapter extends BaseAdapter {
    private Activity context;
    private List<MenuItem> items;
    private String color="#f44336,#E91E63,#9C27B0,#673AB7,#3F51B5,#2196F3,#03A9F4,#00BCD4,#009688,#4CAF50,#8BC34A,#CDDC39,#FFEB3B,#FFC107,#FF9800,#FF5722";
    //private String[] colors={"#d50000","#C51162","#AA00FF","#6200EA","#304FFE","#0091EA","#00B8D4","#00BFA5","#00C853","#64DD17","#AEEA00","#FFD600","#FFAB00","#FF6D00","#DD2C00"};

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

    public String getRandomColor(){
        String[] colors=color.split(",");
        return colors[new Random().nextInt(colors.length)];
        //return colors[position];
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
        holder.image.setBackgroundColor(Color.parseColor(getRandomColor()));
        holder.image.setTag(b.getImage());
        //holder.image.setId(vibrant);
        return rowView;
    }



}
