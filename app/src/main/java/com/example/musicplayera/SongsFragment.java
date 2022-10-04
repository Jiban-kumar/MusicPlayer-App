package com.example.musicplayera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class SongsFragment extends Fragment {

    public SongsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_songs, null, true);
        recyclerView=view.findViewById(R.id.listview);




        adapter=new RecylerSongAdapter(getContext(),list,(MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    public ArrayList<AudioModel> getAllSongsFromDevice(){
        ArrayList<AudioModel> list=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE,MediaStore.Audio.AudioColumns.ARTIST,MediaStore.Audio.AudioColumns.ALBUM};
        Cursor c=getContext().getContentResolver().query(uri,null,null,null,null);
        if(c!=null){
            if(!c.moveToNext()){
                Toast.makeText(getContext(), "There is no Audio in file", Toast.LENGTH_SHORT).show();
            }
            else {
                while (c.moveToNext()){

                    String path=c.getString(1);
                    String name=c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
                    String album=c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                    String artist=c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                    //list.add(new AudioModel(path,name,artist,album));
                }
            }

            c.close();
        }
        else{
            Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
    //These are from mainactivity for changing their data

    MainActivity activity;
    ArrayList<AudioModel> list;
    RecyclerView recyclerView;
    int position;
    RecylerSongAdapter adapter;
    public void reciveData(MainActivity activity,ArrayList<AudioModel> list,int position) {

        this.activity=activity;
        this.list=list;
        this.position=position;

    }
    public void changeSom(int position){
        if(adapter!=null){
            adapter.changeSelectedItem(position);

        }
    }
}