package com.example.musicplayera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;


public class PlaylistFragment extends Fragment {


    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
        addbtn=view.findViewById(R.id.addbtn);
        showallplaylist();
        //hey here null is provided change it
        RecyclerviewAdapter adapter=new RecyclerviewAdapter(getContext(),listname,mainlist,activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddPlaylistActivity.class);
                intent.putExtra("list",(Serializable) songlist);
                getActivity().startActivityForResult(intent,2);

//                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
//                LayoutInflater inflater1=requireActivity().getLayoutInflater();
//                builder.setView(inflater.inflate(R.layout.popup_addnew_playlist,null))
//                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent=new Intent(getContext(),AddPlaylistActivity.class);
//                                intent.putExtra("text","Some thing");
//                                intent.putExtra("list",(Serializable) songlist);
//                                getActivity().startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();
            }
        });

        return view;
    }
    RecyclerView recyclerView;
    MainActivity activity;
    ArrayList<AudioModel> songlist;
    ArrayList<AudioModel> favlist;
    FloatingActionButton addbtn;
    public void RecieveData(MainActivity activity, ArrayList<AudioModel> songlist,ArrayList<AudioModel> favlist){
        this.activity=activity;
        this.songlist=songlist;
        this.favlist=favlist;
//        showallplaylist();
//        //hey here null is provided change it
//        RecyclerviewAdapter adapter=new RecyclerviewAdapter(getContext(),listname,mainlist,playinglist,player,position);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
//
//        addbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(activity, "You have some work to do", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    public void addtoplaylist(String name,ArrayList<AudioModel> slist){

        File file=new File(getContext().getFilesDir()+"/"+name);
        if(file==null){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            File f=new File(getContext().getFilesDir()+"/playlist");
            if(f==null){
                f.createNewFile();
            }
            Writer writer=new BufferedWriter(new FileWriter(f,true));
            writer.append(name+"\n");
            writer.close();

            //for song add
            Writer writer1=new BufferedWriter(new FileWriter(file,true));
            for (int i = 0; i < slist.size(); i++) {
                writer1.append(slist.get(i).getName()+"\n");
            }

            writer1.close();

            mainlist.add(slist);
            listname.add(name);

            RecyclerviewAdapter adapter=new RecyclerviewAdapter(getContext(),listname,mainlist,activity);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getPlaylist(){
        ArrayList<String> list=new ArrayList<>();
        try {
            File file=new File(getContext().getFilesDir()+"/playlist");
            if(file.exists()){
                FileInputStream stream=getContext().openFileInput("playlist");
                StringBuffer buffer=new StringBuffer();
                int a;
                while ((a=stream.read())!=-1){
                    buffer.append((char) a);
                }
                String[] array=buffer.toString().split("\n");
                for(int i=0;i<array.length;i++){
                    list.add(array[i]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    ArrayList<ArrayList<AudioModel>> mainlist=new ArrayList<>();
    ArrayList<String> listname=new ArrayList<>();
    public void showallplaylist(){
        ArrayList<AudioModel> list=new ArrayList<>();
        mainlist.add(songlist);
        listname.add("Last Added");

        //Check for fav

        if (favlist!=null)
        if(favlist.size()>0){
            mainlist.add(favlist);
            listname.add("Favourites");
        }
        //i have to add for most played list
        ArrayList<String> jk=getPlaylist();
        if(jk.size()>0){
            Toast.makeText(activity, jk.size()+"", Toast.LENGTH_SHORT).show();
            for (int i=0;i<jk.size();i++){
                ArrayList<AudioModel> hlist=getfavlist(jk.get(i));
                if(hlist.size()>0){
                    mainlist.add(hlist);
                    listname.add(jk.get(i));
                }

            }
        }

    }

    public ArrayList<AudioModel> getfavlist(String playlistname){
        ArrayList<AudioModel> list=new ArrayList<>();
        try {
            File file=new File(getContext().getFilesDir()+"/"+playlistname);
            if(file.exists()){
                FileInputStream stream=getContext().openFileInput(playlistname);
                StringBuffer buffer=new StringBuffer();
                int a;
                while ((a=stream.read())!=-1){
                    buffer.append((char)a);
                }
                String[] array=buffer.toString().split("\n");

                if(array!=null){
                    for(int j=0;j<songlist.size();j++){
                        for (int i=0;i<array.length;i++){
                            if(songlist.get(j).getName().equals(array[i])){
                                list.add(songlist.get(j));
                            }
                        }
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}