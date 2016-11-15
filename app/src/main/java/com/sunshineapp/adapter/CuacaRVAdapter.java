package com.sunshineapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.sunshineapp.R;
import com.sunshineapp.pojo.List;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by hendrysetiadi on 13/11/2016.
 */

public class CuacaRVAdapter extends RecyclerView.Adapter<CuacaRVAdapter.ViewHolder>{
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    java.util.List<List> lists;
    public CuacaRVAdapter (java.util.List<List> list){
        this.lists = list;
    }
    public void updateList(java.util.List<List> list){
        this.lists = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            view = layoutInflater.inflate(R.layout.header_cuaca, parent, false);
        }
        else { // this is TYPE_ITEM
            view = layoutInflater.inflate(R.layout.item_cuaca, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.ivCuaca.getContext();

        List list = lists.get(position);

        Date date = new Date(Long.parseLong(list.getDt() +"000"));
        holder.tvDate.setText(date.toString());
        holder.tvLow.setText(String.format(
                context.getString(R.string.xx_percent),
                list.getTemp().getMin() ) );
        holder.tvHigh.setText(String.format(
                context.getString(R.string.xx_percent),
                list.getTemp().getMax()));
        holder.tvCuacaDesc.setText(list.getWeather().get(0).getDescription());
        // https://openweathermap.org/weather-conditions

        // http://openweathermap.org/img/w/10d.png
        Picasso.with(context).load("http://openweathermap.org/img/w/" +
            list.getWeather().get(0).getIcon() + ".png").into(holder.ivCuaca);
    }

    @Override
    public int getItemCount() {
        if (null!= lists) {
            return lists.size();
        }
        return 0;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        TextView tvLow;
        TextView tvHigh;
        TextView tvCuacaDesc;
        ImageView ivCuaca;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCuaca = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvLow = (TextView) itemView.findViewById(R.id.tv_low);
            tvHigh = (TextView) itemView.findViewById(R.id.tv_high);
            tvCuacaDesc = (TextView) itemView.findViewById(R.id.tv_icon_desc);
        }
    }

}
