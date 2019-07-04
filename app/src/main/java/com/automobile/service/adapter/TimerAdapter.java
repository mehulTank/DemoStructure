package com.automobile.service.adapter;


import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.MyViewHolder>{

    private ArrayList<String> al_data;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_timer;
        CountDownTimer timer;

        public MyViewHolder (View view){
            super(view);
            tv_timer = (TextView)view.findViewById(R.id.tv_timer);

        }


    }

    public Adapter(ArrayList<String> al_data) {
        this.al_data = al_data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tv_timer.setText(al_data.get(position));

        if (holder.timer != null) {
            holder.timer.cancel();
        }
         long timer = Long.parseLong(al_data.get(position));

        timer = timer*1000;

        holder.timer = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
//              holder.tv_timer.setText("" + millisUntilFinished/1000 + " Sec");


                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                String newtime = hours + ":" + minutes + ":" + seconds;

                if (newtime.equals("0:0:0")) {
                    holder.tv_timer.setText("00:00:00");
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    holder.tv_timer.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                    holder.tv_timer.setText("0" + hours + ":0" + minutes + ":" + seconds);
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    holder.tv_timer.setText("0" + hours + ":" + minutes + ":0" + seconds);
                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    holder.tv_timer.setText(hours + ":0" + minutes + ":0" + seconds);
                } else if (String.valueOf(hours).length() == 1) {
                    holder.tv_timer.setText("0" + hours + ":" + minutes + ":" + seconds);
                } else if (String.valueOf(minutes).length() == 1) {
                    holder.tv_timer.setText(hours + ":0" + minutes + ":" + seconds);
                } else if (String.valueOf(seconds).length() == 1) {
                    holder.tv_timer.setText(hours + ":" + minutes + ":0" + seconds);
                } else {
                    holder.tv_timer.setText(hours + ":" + minutes + ":" + seconds);
                }

            }

            public void onFinish() {
                holder.tv_timer.setText("00:00:00");
            }
        }.start();


    }

    @Override
    public int getItemCount() {
        return al_data.size();
    }



}
