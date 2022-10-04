package com.example.musicplayera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listname;
    ArrayList<ArrayList<AudioModel>> songlist;
    MainActivity activity;
    public RecyclerviewAdapter(Context context, ArrayList<String> listname, ArrayList<ArrayList<AudioModel>> songlist, MainActivity activity) {
        this.context = context;
        this.listname=listname;
        this.songlist=songlist;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.playlist_row_recylr,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    int selectedIndex=-1;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(listname.get(position));
        if(songlist.get(position).get(0).getImage()!=null){
            Glide.with(context).asBitmap().load(songlist.get(position).get(0).getImage()).into(holder.image);
        }
        else {
            Glide.with(context).asBitmap().load(R.drawable.mikusong).into(holder.image);
        }
        holder.playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.fromSongFrag(songlist.get(position),0);
            }
        });
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex=position;
                ArrayList<AudioModel> newlist=songlist.get(position);
                activity.showplaylistpage(newlist,listname.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listname.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        RelativeLayout rel;
        FloatingActionButton playbtn;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
            rel=itemView.findViewById(R.id.rel);
            playbtn=itemView.findViewById(R.id.playbtn);
            image=itemView.findViewById(R.id.image);
        }
    }
}
