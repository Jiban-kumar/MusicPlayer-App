package com.example.musicplayera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ScrollView scrollView=findViewById(R.id.scroll);
        NonScrollListview listView=findViewById(R.id.listview);
        TextView textView=findViewById(R.id.text);

        list= (ArrayList<AudioModel>) getIntent().getSerializableExtra("list");
        String listname=getIntent().getExtras().getString("listname");
        textView.setText(listname);

        playinglist=(ArrayList<AudioModel>) getIntent().getSerializableExtra("playinglist");
        position=getIntent().getExtras().getInt("position");
        //player=(MediaPlayer) getIntent().getSerializableExtra("player");
        //getData(playinglist,position,false);

//        SongsAdapter adapter=new SongsAdapter(this,list);
//        listView.setAdapter(adapter);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

            //int mLastFirstVisibleItem = 0;
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(v.getId()==scrollView.getId()){
//                    final int currentFirstVisibleItem = listView.getFirstVisiblePosition();
//
//                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//                        getSupportActionBar().hide();
//                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//                        getSupportActionBar().show();
//                    }
//
//                    mLastFirstVisibleItem = currentFirstVisibleItem;
//                }
                if(oldScrollY<scrollY){
                    getSupportActionBar().hide();
                }
                else {
                    //this is one way
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(oldScrollY>scrollY)
                                    getSupportActionBar().show();
                                }
                            });
                        }
                    },2000);

                    //another way
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getSupportActionBar().show();
//                        }
//                    },3000);
                }
            }
        });

        //for bottomsheet
        RelativeLayout bottomsheet=findViewById(R.id.bottosheet);
        BottomSheetBehavior sheetBehavior=BottomSheetBehavior.from(bottomsheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        sname=findViewById(R.id.sname);
        ssinger=findViewById(R.id.ssinger);
        stime=findViewById(R.id.stime);
        etime=findViewById(R.id.etime);
        seekBar=findViewById(R.id.seekbar);
        play=findViewById(R.id.play);
        play2=findViewById(R.id.play2);
        forword=findViewById(R.id.forword);
        backword=findViewById(R.id.backword);
        linsheet=findViewById(R.id.linsheet);
        showlist=findViewById(R.id.showlist);
        smore=findViewById(R.id.smore);
        centerimage=findViewById(R.id.centerimage);
        slistview=findViewById(R.id.slistview);
        fav=findViewById(R.id.favourite);
        play2.setVisibility(View.VISIBLE);
        image=findViewById(R.id.image);

        if(list.get(0).getImage()!=null){
            image.setImageBitmap(BitmapFactory.decodeFile(list.get(0).getImage()));

        }
        else {
            image.setImageResource(R.drawable.mikusong);
        }

        //btn party
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(list,position,false);
            }
        });
        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(list,position,false);
            }
        });
        forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==list.size()-1){
                    position=0;
                }
                else {
                    position++;
                }
                getData(list,position,true);
            }
        });
        backword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    position=list.size()-1;
                }
                else {
                    position--;
                }
                getData(list,position,true);
            }
        });

        linsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    player.seekTo(progress);
                    stime.setText(getTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_EXPANDED){
                    play2.setVisibility(View.GONE);
                    showlist.setVisibility(View.VISIBLE);
                    smore.setVisibility(View.VISIBLE);
                    sheetBehavior.setDraggable(false);
                }
                else {
                    play2.setVisibility(View.VISIBLE);
                    showlist.setVisibility(View.GONE);
                    smore.setVisibility(View.GONE);
                    sheetBehavior.setDraggable(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        showlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!islistOpen){
//                    SongsAdapter adapter=new SongsAdapter(PlaylistActivity.this,list);
//                    slistview.setAdapter(adapter);
                    slistview.setVisibility(View.VISIBLE);
                    centerimage.setVisibility(View.GONE);
                    showlist.setBackgroundResource(R.color.teal_200);
                    islistOpen=true;
                }
                else {
                    slistview.setVisibility(View.GONE);
                    centerimage.setVisibility(View.VISIBLE);
                    showlist.setBackgroundResource(R.color.teal_200);
                    islistOpen=false;
                }

            }
        });
        slistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getData(list,position,true);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.setBackgroundResource(R.color.purple_500);
            }
        });
    }
    TextView sname;
    TextView ssinger;
    TextView stime;
    TextView etime;
    SeekBar seekBar;
    ImageButton play;
    ImageButton play2;
    ImageButton forword;
    ImageButton backword;
    LinearLayout linsheet;
    ImageButton showlist;
    ImageButton smore;
    ImageButton fav;
    ImageView centerimage;
    ListView slistview;
    int position=0;
    boolean islistOpen=false;
    ArrayList<AudioModel> list;
    ArrayList<AudioModel> playinglist;
    int count=0;
    boolean isPlaying=false;
    MediaPlayer player;
    ImageView image;
    public void getData(ArrayList<AudioModel> list,int position,boolean newsong){

        this.list=list;
        this.position=position;
        count++;
        if(count==1){
            isPlaying=true;
            newsong=true;
        }
        Uri uri;
        if(newsong){
            isPlaying=true;
            if(player!=null){
                player.stop();
                player.release();
                player=null;
                seekbarUpdateHandler.removeCallbacks(runable);
            }
            uri=Uri.parse(list.get(position).getPath());
            player= MediaPlayer.create(PlaylistActivity.this,uri);
        }
        else {
            if(isPlaying){//this means song is playing and you clicked pause
                isPlaying=false;
            }
            else {//this means song is not playing and you click play
                isPlaying=true;

            }
        }

        seekBar.setMax(player.getDuration());
        etime.setText(getTime(player.getDuration()));
        sname.setText(list.get(position).getName());
        ssinger.setText(list.get(position).getArtist()+","+list.get(position).getArtist());


        if(isPlaying){
            player.start();
            seekbarUpdateHandler.postDelayed(runable,0);
            play.setImageResource(R.drawable.ic_pause);
            play2.setImageResource(R.drawable.ic_pause);
        }
        else {
            player.pause();
            seekbarUpdateHandler.removeCallbacks(runable);
            play.setImageResource(R.drawable.ic_play);
            play2.setImageResource(R.drawable.ic_play);
        }

    }
    Handler seekbarUpdateHandler=new Handler();
    Runnable runable=new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(player.getCurrentPosition());
            stime.setText(getTime(player.getCurrentPosition()));
            seekbarUpdateHandler.postDelayed(this,0);

        }
    };
    public String getTime(int duration){
        String c=String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        return c;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}