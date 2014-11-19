package com.example.orodr_000.myapplication;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by orodr_000 on 15.10.2014.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity context;
    private List<Quiz_Button> items;

    static class ViewHolder{
        public ImageView image;
        public TextView title;
        public  TextView points;
    }

    public CustomListAdapter(Activity context, List<Quiz_Button> items) {
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

    public static abstract class OnClickListener implements
            View.OnClickListener {

        private ViewHolder mViewHolder;

        /**
         * @param holder The holder of the clickable item
         */
        public OnClickListener(ViewHolder holder) {
            mViewHolder = holder;
        }

        // delegates the click event
        public void onClick(View v) {
            onClick(v, mViewHolder);
        }

        /**
         * Implement your click behavior here
         * @param v  The clicked view.
         */
        public abstract void onClick(View v, ViewHolder viewHolder);
    };

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View rowView=convertView;
        if(rowView==null){
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.list_row,null);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.title=(TextView)rowView.findViewById(R.id.quiz_title);
            viewHolder.image=(ImageView)rowView.findViewById(R.id.quiz_image);
            viewHolder.points=(TextView)rowView.findViewById(R.id.quiz_points);
            rowView.setTag(viewHolder);
        }
        Quiz_Button b=items.get(position);
        ViewHolder holder=(ViewHolder) rowView.getTag();
        holder.title.setText(b.getTitle());
        holder.title.setHint(b.getTheme());
        int resID = rowView.getResources().getIdentifier("@drawable/"+b.getThumbnailUrl(), "drawable",rowView.getContext().getPackageName());
        holder.image.setImageResource(resID);
        return rowView;
    }



}
