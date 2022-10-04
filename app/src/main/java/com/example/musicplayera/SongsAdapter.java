package com.example.musicplayera;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SongsAdapter extends ArrayAdapter {
    Context context;
    ArrayList<AudioModel> list;

    public SongsAdapter(@NonNull Context context, ArrayList<AudioModel> list) {
        super(context, R.layout.list_row_model);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_row_model,null,true);
        TextView name=view.findViewById(R.id.sname);
        TextView singer=view.findViewById(R.id.ssinger);
        name.setText(list.get(position).getName());
        singer.setText(list.get(position).getArtist()+","+list.get(position).getAlbum());
        ImageView image=view.findViewById(R.id.simage);
        if(list.get(position).getImage()!=null){
            Glide.with(getContext()).asBitmap().load(list.get(position).getImage()).into(image);
            //image.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getImage()));
        }
        else {
            Glide.with(getContext()).asBitmap().load(R.drawable.animemusic).into(image);
            //image.setImageResource(R.drawable.animemusic);
        }
        return view;
    }
}
