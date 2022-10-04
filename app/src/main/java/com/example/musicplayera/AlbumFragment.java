package com.example.musicplayera;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class AlbumFragment extends Fragment {

    public AlbumFragment() {
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
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        ArrayList<String> listname = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listname.add(list.get(i).get(0).getAlbum());
        }
        RecyclerviewAdapter adapter = new RecyclerviewAdapter(context, listname, list,(MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return view;
    }

    ArrayList<ArrayList<AudioModel>> list;
    Context context;
    RecyclerView recyclerView;
    public void getData(Context context, ArrayList<ArrayList<AudioModel>> list) {
        this.context = context;
        this.list = list;

    }
}