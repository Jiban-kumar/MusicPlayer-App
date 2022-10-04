package com.example.musicplayera;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AddlistAdapter extends RecyclerView.Adapter<AddlistAdapter.ViewHolder> {
    ArrayList<AudioModel> list;
    Context context;
    AddPlaylistActivity activity;
    ArrayList<AudioModel> mysonglist=new ArrayList<>();
    public AddlistAdapter(ArrayList<AudioModel> list, Context context,AddPlaylistActivity activity) {
        this.list = list;
        this.context = context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_row_addlistmodel,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    ArrayList<String> pol=new ArrayList<>();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(list.get(position).getImage()!=null){
            Glide.with(context).asBitmap().load(list.get(position).getImage()).into(holder.image);
        }
        else {
            Glide.with(context).asBitmap().load(R.drawable.mikusong).into(holder.image);
        }
        holder.sname.setText(list.get(position).getName());
        holder.sartist.setText(list.get(position).getArtist());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pol.contains(position+"")){
                    pol.remove(position+"");
                    mysonglist.remove(list.get(position));
                    notifyItemChanged(position);
                }
                else {
                    pol.add(position+"");
                    mysonglist.add(list.get(position));
                }
                for (int i = 0; i < pol.size(); i++) {
                    notifyItemChanged((Integer.parseInt(pol.get(i))));
                }
                activity.sendData(mysonglist);
            }
        });
        if(pol.contains(position+"")){

            holder.checkBox.setChecked(true);
            holder.rel.setBackgroundColor(Color.LTGRAY);
        }
        else {

            holder.checkBox.setChecked(false);
            holder.rel.setBackgroundColor(Color.WHITE);
        }
//        if(!holder.checkBox.isChecked()){
//            holder.checkBox.setChecked(false);
//            holder.rel.setBackgroundColor(Color.WHITE);
//        }
//        else {
//            holder.checkBox.setChecked(true);
//            holder.rel.setBackgroundColor(Color.LTGRAY);
//        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        ImageView image;
        TextView sname;
        TextView sartist;
        RelativeLayout rel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox);
            image=itemView.findViewById(R.id.image);
            sname=itemView.findViewById(R.id.sname);
            sartist=itemView.findViewById(R.id.sartist);
            rel=itemView.findViewById(R.id.myrel);
        }
    }
}
