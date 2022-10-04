package com.example.musicplayera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class AddPlaylistActivity extends AppCompatActivity {

    EditText pname;
    RecyclerView recyclerView;
    Button createbtn,cancelbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

        pname=findViewById(R.id.pname);
        recyclerView=findViewById(R.id.recyclerview);
        createbtn=findViewById(R.id.createbtn);
        cancelbtn=findViewById(R.id.cancelbtn);

        AddlistAdapter adapter=new AddlistAdapter((ArrayList<AudioModel>)getIntent().getSerializableExtra("list"),this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pname.getText().toString().equals("")){
                    Toast.makeText(AddPlaylistActivity.this, " Enter Playlist name", Toast.LENGTH_SHORT).show();
                }
                else if(mysonglist.size()==0){
                    Toast.makeText(AddPlaylistActivity.this, "Select Some Songs ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent();
                    intent.putExtra("name",pname.getText().toString());
                    intent.putExtra("list",(Serializable) mysonglist);
                    setResult(1,intent);
                    finish();
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    ArrayList<AudioModel> mysonglist;
    public void sendData(ArrayList<AudioModel> mysonglist) {
        this.mysonglist=mysonglist;
    }
}