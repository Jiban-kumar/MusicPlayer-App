package com.example.musicplayera;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecylerSongAdapter extends RecyclerView.Adapter<RecylerSongAdapter.ViewHolder> {

    Context context;
    ArrayList<AudioModel> list;
    MainActivity activity;
    public RecylerSongAdapter(Context context, ArrayList<AudioModel> list,MainActivity activity) {
        this.context = context;
        this.list = list;
        this.activity=activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_row_model,null,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    int selectedIndex=-1;
    int lastclicked=-1;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(list.get(holder.getAdapterPosition()).getName());
        holder.singer.setText(list.get(holder.getAdapterPosition()).getArtist()+","+list.get(holder.getAdapterPosition()).getAlbum());
        if(list.get(holder.getAdapterPosition()).isfav){
            holder.fav.setVisibility(View.VISIBLE);
        }
        else {
            holder.fav.setVisibility(View.INVISIBLE);
        }

        holder.linrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedIndex!=-1){
                    lastclicked=selectedIndex;
                    notifyItemChanged(lastclicked);
                }
                selectedIndex=holder.getAdapterPosition();
                notifyItemChanged(holder.getAdapterPosition());
                activity.fromSongFrag(list,holder.getAdapterPosition());
            }
        });
        if(selectedIndex==position){
            Glide.with(context).asGif().load(R.drawable.songgif).into(holder.image);
        }
        else {
            if(list.get(position).getImage()!=null){
                Glide.with(context).asDrawable().load(list.get(position).image).into(holder.image);
            }
            else {
                Glide.with(context).asBitmap().load(R.drawable.mikusong).into(holder.image);
            }

        }
//        if(list.get(position).getImage()!=null){
//            Glide.with(context).asDrawable().load(list.get(position).image).into(holder.image);
//        }
//        else {
//            Glide.with(context).asBitmap().load(R.drawable.mikusong).into(holder.image);
//        }
        //myholder=holder;

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "need some work", Toast.LENGTH_SHORT).show();
            }
        });
    }
    int cou=0;
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void changeSelectedItem(int position){
        this.selectedIndex=position;
        notifyItemChanged(selectedIndex);
        //.with(context).asGif().load(R.drawable.songgif).into(myholder.image);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView singer;
        ImageView image;
        ImageView more;
        ImageView fav;
        RelativeLayout linrow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.sname);
            singer=itemView.findViewById(R.id.ssinger);
            image=itemView.findViewById(R.id.simage);
            more=itemView.findViewById(R.id.more);
            fav=itemView.findViewById(R.id.fav);
            linrow=itemView.findViewById(R.id.linrow);
        }
    }
}
