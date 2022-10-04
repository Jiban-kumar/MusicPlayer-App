package com.example.musicplayera;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navview;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TabLayout tabLayout;
    RelativeLayout bottomsheet;
    BottomSheetBehavior sheetBehavior;

    MediaSessionCompat sessionCompat;
    NotificationCompat.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawerlayout);

        setSupportActionBar(toolbar);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.tablayot);
        frameLayout=findViewById(R.id.framelayout);
        fragment=new SongsFragment();
        //((SongsFragment)fragment).reciveData(MainActivity.this,list,position);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

        //for bottom sheet
        bottomsheet=findViewById(R.id.bottosheet);
        sheetBehavior=BottomSheetBehavior.from(bottomsheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //Initialization
        navview=findViewById(R.id.navview);
        sname=findViewById(R.id.sname);
        ssinger=findViewById(R.id.ssinger);
        stime=findViewById(R.id.stime);
        etime=findViewById(R.id.etime);
        seekBar=findViewById(R.id.seekbar);
        seekBar2=findViewById(R.id.seekbar2);
        play=findViewById(R.id.play);
        play2=findViewById(R.id.play2);
        forword=findViewById(R.id.forword);
        next2=findViewById(R.id.next2);
        backword=findViewById(R.id.backword);
        linsheet=findViewById(R.id.linsheet);
        showlist=findViewById(R.id.showlist);
        smore=findViewById(R.id.smore);
        centerimage=findViewById(R.id.centerimage);
        simage=findViewById(R.id.simage);
        slistview=findViewById(R.id.slistview);
        fav=findViewById(R.id.favourite);
        vol=findViewById(R.id.volume);
        loop=findViewById(R.id.loop);
        shuffle=findViewById(R.id.suffle);

        maincontainer=findViewById(R.id.maincontainer);
        ppage=findViewById(R.id.ppage);
        scroll=findViewById(R.id.scroll);
        pimage=findViewById(R.id.pimage);
        ptext=findViewById(R.id.ptext);
        pmorebtn=findViewById(R.id.pmorebtn);
        plistview=findViewById(R.id.plistview);
        backbtn=findViewById(R.id.backbtn);
        opmenu=findViewById(R.id.opmenu);

        instance=this;
        sessionCompat=new MediaSessionCompat(this,"tag");

        //create songlist
        //request permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        list=getFulllist();
        firstsetData();
        playinglist=list;
        suffleIsOn();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    fragment=new SongsFragment();
//                    list=getFulllist();
                    ((SongsFragment)fragment).reciveData(MainActivity.this,list,position);
                }
                else if(tab.getPosition()==1){
                    fragment=new PlaylistFragment();
                    ((PlaylistFragment)fragment).RecieveData(MainActivity.this,list,getfavlist("fav"));

                }
                else if(tab.getPosition()==2){
                    ArrayList<AudioModel> dub=list;
                    ArrayList<ArrayList<AudioModel>> klist=forartist(dub);
                    fragment=new ArtistFragment();
                    ((ArtistFragment)fragment).getData(MainActivity.this,klist);
                    Toast.makeText(MainActivity.this, list.size()+"     "+klist.size(), Toast.LENGTH_SHORT).show();

                }
                else if(tab.getPosition()==3){
                    ArrayList<AudioModel> dub=list;
                    ArrayList<ArrayList<AudioModel>> klist=forartist(dub);
                    fragment=new AlbumFragment();
                    ((AlbumFragment)fragment).getData(MainActivity.this,klist);
                    Toast.makeText(MainActivity.this, list.size()+"     "+klist.size(), Toast.LENGTH_SHORT).show();
                }
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.framelayout,fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        next2.setOnClickListener(new View.OnClickListener() {
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
                RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) showlist.getLayoutParams();
                if(newState==BottomSheetBehavior.STATE_EXPANDED){
                    play2.setVisibility(View.GONE);
                    next2.setVisibility(View.GONE);
                    smore.setVisibility(View.VISIBLE);
                    seekBar2.setVisibility(View.GONE);
                    params.rightMargin=80;
                    showlist.setLayoutParams(params);
                    sheetBehavior.setDraggable(false);

                    maincontainer.setVisibility(View.INVISIBLE);
                }
                else {
                    play2.setVisibility(View.VISIBLE);
                    next2.setVisibility(View.VISIBLE);
                    smore.setVisibility(View.GONE);
                    seekBar2.setVisibility(View.VISIBLE);
                    params.rightMargin=0;
                    showlist.setLayoutParams(params);
                    sheetBehavior.setDraggable(true);
//                    if(ppage.getVisibility()==View.VISIBLE){
//
//                    }
                    maincontainer.setVisibility(View.VISIBLE);
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
//                    Handler handler=new Handler();
//                    Runnable runnable=new Runnable() {
//                        @Override
//                        public void run() {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter=new RecylerSongAdapter(MainActivity.this,playinglist,MainActivity.this);
//                                    slistview.setAdapter(adapter);
//                                    slistview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                                }
//                            });
//                        }
//                    };
//                    Thread thread=new Thread(runnable);
//                    thread.start();
                    adapter=new RecylerSongAdapter(MainActivity.this,playinglist,MainActivity.this);
                    slistview.setAdapter(adapter);
                    slistview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    if(sheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    slistview.setVisibility(View.VISIBLE);
                    centerimage.setVisibility(View.GONE);
                    vol.setVisibility(View.INVISIBLE);
                    fav.setVisibility(View.INVISIBLE);
                    //set icon color
                    showlist.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN);
                    islistOpen=true;
                }
                else {
                    slistview.setVisibility(View.GONE);
                    centerimage.setVisibility(View.VISIBLE);
                    vol.setVisibility(View.VISIBLE);
                    fav.setVisibility(View.VISIBLE);

                    showlist.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    islistOpen=false;
                }

            }
        });
//        slistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getData(list,position,true);
//            }
//        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).isfav){
                    list.get(position).setIsfav(false);
                    fav.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.white));
                    removefromfavlist("fav",list.get(position).name);
                }
                else {
                    fav.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.icon_color));
                    list.get(position).setIsfav(true);
                    addtoplaylist("fav",list.get(position).name);
                }
                }
        });
//        plistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getData(playinglist,position,true);
//                SongsAdapter adapter=new SongsAdapter(MainActivity.this,playinglist);
//                slistview.setAdapter(adapter);
//                Toast.makeText(MainActivity.this, playinglist.size()+"  "+list.size(), Toast.LENGTH_SHORT).show();
//            }
//        });
        RelativeLayout reltoolbar=findViewById(R.id.reltoolbar);
        scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>500&&scrollY<600){
                    backbtn.setVisibility(View.GONE);
                    opmenu.setVisibility(View.GONE);
                    reltoolbar.setBackgroundColor(getResources().getColor(R.color.transparnt));
                }
                else if(oldScrollY>scrollY){

                    backbtn.setVisibility(View.VISIBLE);
                    opmenu.setVisibility(View.VISIBLE);
                    reltoolbar.setBackgroundColor(getResources().getColor(R.color.icon_color));
                }
                else {
                    backbtn.setVisibility(View.GONE);
                    opmenu.setVisibility(View.GONE);

                    reltoolbar.setBackgroundColor(getResources().getColor(R.color.transparnt));
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ppage.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                maincontainer.setVisibility(View.VISIBLE);
            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!willsuffle){
                    shuffle.setColorFilter(getResources().getColor(R.color.icon_color));
                    willsuffle=true;
                }
                else {
                    shuffle.setColorFilter(getResources().getColor(R.color.black));
                    willsuffle=false;
                }
            }
        });
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!willloop){
                    loop.setColorFilter(getResources().getColor(R.color.icon_color));
                    willloop=true;
                }
                else {
                    loop.setColorFilter(getResources().getColor(R.color.black));
                    willloop=false;
                }
            }
        });
        navview.setCheckedItem(R.id.library);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){

                }
                else if(item.getItemId()==R.id.recent) {
                    Intent intent=new Intent(MainActivity.this,OnlineActivity.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(777);
    }

    static MainActivity instance;
    public static MainActivity getInstance(Context context){

        return instance;
    }
    public void km(){
        getData(playinglist,position,false);
    }
    public void fromNotification(String text){
        if(text=="back"){
            if(position==0){
                position=list.size()-1;
            }
            else {
                position--;
            }
            getData(playinglist,position,true);
        }
        else if (text == "pause") {
            getData(playinglist,position,false);

        }
        else if(text=="ford"){
            if(position==list.size()-1){
                position=0;
            }
            else {
                position++;
            }
            getData(playinglist,position,true);
        }
    }

    public void notif(boolean isPlaying,int playid){
        //Notification
//        Intent intent=new Intent(this,MainActivity.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
//
        RemoteViews views=new RemoteViews(getPackageName(),R.layout.notification_layout);
//
//
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.drawable.ic_fav);
//        //builder.setContent(views);
//        builder.setContentText("My name is Jiban");
//        builder.setPriority(Notification.PRIORITY_DEFAULT);
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager managerCompat=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        managerCompat.notify(1,builder.build());
        Bitmap songimage;
        if(playinglist.get(position).getImage()!=null){
            songimage=BitmapFactory.decodeFile(playinglist.get(position).getImage());
        }
        else {
            songimage=BitmapFactory.decodeResource(getResources(),R.drawable.mikusong);
        }


        Intent contentintent=new Intent(this,MainActivity.class);
        PendingIntent pcontent=PendingIntent.getActivity(this,0,contentintent,0);
        Intent intentback=new Intent(this,NotificationReciver.class);
        intentback.setAction("back");
        PendingIntent pback=PendingIntent.getBroadcast(this,0,intentback,0);
        Intent intentplay=new Intent(this,NotificationReciver.class);
        intentplay.setAction("pause");
        PendingIntent pplay=PendingIntent.getBroadcast(this,0,intentplay,0);
        Intent intentford=new Intent(this,NotificationReciver.class);
        intentford.setAction("ford");
        PendingIntent pford=PendingIntent.getBroadcast(this,0,intentford,0);


        builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_fav)
                .setAutoCancel(false)
                .setContentTitle(playinglist.get(position).getName())
                .setContentText(playinglist.get(position).getArtist())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(R.drawable.ic_baseline_skip_previous_24,"back",pback)
                .addAction(playid,"pause",pplay)
                .addAction(R.drawable.ic_baseline_skip_next_24,"forword",pford)
                .setContentIntent(pcontent)
                .setLargeIcon(songimage)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0,1,2)
                    .setMediaSession(sessionCompat.getSessionToken()));
//        Intent nintent=new Intent(this,NotificationReciver.class);
////        nintent.putExtra("from",1);
////        nintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        nintent.putExtra("message","This is a Notification Message");
////        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,nintent,
////                PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,nintent,
//                0);

//        builder.setContentIntent(pendingIntent);
//        views.setImageViewResource(R.id.imageButton2,R.drawable.ic_pause2);
//        views.setOnClickPendingIntent(R.id.imageButton2,pendingIntent);

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_LOW);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0});
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        Notification notification=builder.build();
        notification.flags |=Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;

        notificationManager.notify(777,notification);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle","On Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle","On Pause");
    }

    @Override
    public void onBackPressed() {

        if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            //below code is for exit app
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1 && requestCode==2){
            ArrayList<AudioModel> mysongslist=(ArrayList<AudioModel>) data.getSerializableExtra("list");
            String listname=data.getExtras().getString("name");
            ((PlaylistFragment)fragment).addtoplaylist(listname,mysongslist);

        }
    }

    //exp
    Fragment fragment;
    FrameLayout frameLayout;
    TextView sname;
    TextView ssinger;
    TextView stime;
    TextView etime;
    SeekBar seekBar;
    ProgressBar seekBar2;
    ImageButton play;
    ImageButton play2;
    ImageButton forword;
    ImageButton next2;
    ImageButton backword;
    LinearLayout linsheet;
    MediaPlayer player;
    ImageButton showlist;
    ImageButton smore;
    ImageButton fav;
    ImageButton vol;
    ImageView centerimage;
    ImageView simage;
    ImageButton loop;
    ImageButton shuffle;
    RecyclerView slistview;
    boolean isPlaying=false;
    boolean islistOpen=false;
    ArrayList<AudioModel> list;
    ArrayList<AudioModel> playinglist;
    ArrayList<AudioModel> mainlist;
    int position;
    int count=0;
    boolean willsuffle=false;
    boolean willloop=false;

    LinearLayout maincontainer;
    RelativeLayout ppage;
    NestedScrollView scroll;
    ImageView pimage;
    TextView ptext;
    ImageButton pmorebtn;
    RecyclerView plistview;
    ImageButton backbtn;
    ImageButton opmenu;

    RecylerSongAdapter adapter;
    RecylerSongAdapter adapter1;

    public void addtonewthread(ArrayList<AudioModel> dlist,int position,boolean newsong){
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getData(dlist,position,newsong);
                    }
                });
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    public void getData(ArrayList<AudioModel> dlist,int position,boolean newsong){


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
                //player.stop();
                player.reset();
                player.release();
                //player=null;
                seekbarUpdateHandler.removeCallbacks(runable);
            }
            uri=Uri.fromFile(new File(dlist.get(position).getPath()));
            player=MediaPlayer.create(MainActivity.this,uri);
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
        seekBar2.setMax(player.getDuration());
        etime.setText(getTime(player.getDuration()));
        sname.setText(dlist.get(position).getName());
        ssinger.setText(dlist.get(position).getArtist()+","+dlist.get(position).getArtist());


        if(list.get(position).getImage()!=null){
            centerimage.setImageBitmap(BitmapFactory.decodeFile(dlist.get(position).getImage()));
            simage.setImageBitmap(BitmapFactory.decodeFile(dlist.get(position).getImage()));
        }
        else {
            centerimage.setImageResource(R.drawable.animemusic);
            simage.setImageResource(R.drawable.animemusic);
        }

        if(dlist.get(position).isfav){
            fav.setColorFilter(ContextCompat.getColor(this,R.color.icon_color));
        }
        else {
            fav.setColorFilter(ContextCompat.getColor(this,R.color.white));
        }

        if(isPlaying){
            player.start();
            seekbarUpdateHandler.postDelayed(runable,0);
            play.setImageResource(R.drawable.ic_pause);
            play2.setImageResource(R.drawable.ic_pause2);
            notif(isPlaying,R.drawable.ic_pause2);
        }
        else {
            player.pause();
            seekbarUpdateHandler.removeCallbacks(runable);
            play.setImageResource(R.drawable.ic_play);
            play2.setImageResource(R.drawable.ic_play2);
            notif(isPlaying,R.drawable.ic_play2);
        }
        if(willloop){
            Toast.makeText(this, "kl", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendSelectedItem(){
        if(adapter!=null)
            adapter.changeSelectedItem(position);
        if(adapter1!=null){
            adapter1.changeSelectedItem(position);
        }
        if(tabLayout.getSelectedTabPosition()==0){
            ((SongsFragment)fragment).changeSom(position);
        }
    }
    Handler seekbarUpdateHandler=new Handler();
    Runnable runable=new Runnable() {

        @Override
        public void run() {
            seekBar.setProgress(player.getCurrentPosition());
            seekBar2.setProgress(player.getCurrentPosition());
            stime.setText(getTime(player.getCurrentPosition()));
            seekbarUpdateHandler.postDelayed(this,0);
            autonextplay();
            sendSelectedItem();
        }
    };
    public void autonextplay(){
        if(stime.getText().toString().equals(etime.getText().toString())){
            int x=position;

            if(position==playinglist.size()-1){
                position=0;
            }
            else {
                position++;
            }
            if(willsuffle){
                position=plist.get(position);
            }
            if(willloop){
                position=x;
            }
            getData(playinglist,position,true);
        }
    }

    ArrayList<Integer> plist=new ArrayList<>();
    public void suffleIsOn(){
        plist=new ArrayList<>();
        for (int i = 0; i < playinglist.size() ; i++) {
            plist.add(i);
        }
        Collections.shuffle(plist);
    }
    public String getTime(int duration){
        String c=String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        return c;
    }
    public void firstsetData(){

        int position=findposition();
        Uri uri=Uri.fromFile(new File(list.get(position).getPath()));
        player=MediaPlayer.create(MainActivity.this,uri);

        seekBar.setMax(player.getDuration());
        seekBar2.setMax(player.getDuration());
        etime.setText(getTime(player.getDuration()));
        sname.setText(list.get(position).getName());
        ssinger.setText(list.get(position).getArtist()+","+list.get(position).getArtist());
        if(list.get(position).getImage()!=null){
            centerimage.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getImage()));
            simage.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getImage()));
        }
        else {
            centerimage.setImageResource(R.drawable.animemusic);
            simage.setImageResource(R.drawable.animemusic);
        }
        if(list.get(position).isfav){
            fav.setColorFilter(ContextCompat.getColor(this,R.color.icon_color));
        }
//        Handler handler=new Handler();
//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((SongsFragment)fragment).reciveData(MainActivity.this,list,position);
//                    }
//                });
//            }
//        };
//
//        Thread thread=new Thread(runnable);
//        thread.start();
        ((SongsFragment)fragment).reciveData(MainActivity.this,list,position);
    }

    private int findposition() {
        return position;
    }

    public ArrayList<AudioModel> getAllSongsFromDevice(){
        ArrayList<AudioModel> list=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE,MediaStore.Audio.AudioColumns.ARTIST,MediaStore.Audio.AudioColumns.ALBUM};
        Cursor c=MainActivity.this.getContentResolver().query(uri,null,null,null,null);




        if(c!=null){
            if(!c.moveToNext()){
                Toast.makeText(MainActivity.this, "There is no Audio in file", Toast.LENGTH_SHORT).show();
            }
            else {
                while (c.moveToNext()){

                    String path=c.getString(1);
                    String name=c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
                    String album=c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));
                    String artist=c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST));

//
//                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//                    byte[] rawArt;
//                    Bitmap art = null;
//                    BitmapFactory.Options bfo=new BitmapFactory.Options();
//
//                    //mmr.setDataSource(this, uri);
//                    mmr.setDataSource(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
//
//                    rawArt = mmr.getEmbeddedPicture();
//
//                    // if rawArt is null then no cover art is embedded in the file or is not
//                    // recognized as such.
//                    if (null != rawArt)
//                        art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
//                    else
//                    String albumart=c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));



                    list.add(new AudioModel(path,name,artist,album,null,false));
                }
            }

            c.close();
        }
        else{
            Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
    public ArrayList<ArrayList<String>> getSomething(){

        ArrayList<ArrayList<String>> jk=new ArrayList<>();
        Cursor cursor=MainActivity.this.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,null);
        if (!cursor.moveToFirst()) {

            // do whatever you need to do
        }
        else {
            while (cursor.moveToNext()){
                String image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
                ArrayList<String> mlist=new ArrayList<>();
                mlist.add(path);
                mlist.add(image);
                jk.add(mlist);
            }
        }
        return jk;
    }
    public ArrayList<AudioModel> getFulllist(){
        list=getAllSongsFromDevice();
        ArrayList<ArrayList<String>> mlist=getSomething();
        ArrayList<AudioModel> flist=getfavlist("fav");
        for (int i=0;i<list.size();i++){
            for (int j=0;j<mlist.size();j++){

                if(list.get(i).getArtist().equals(mlist.get(j).get(0))){
                    list.get(i).setImage(mlist.get(j).get(1));
                }
            }
            for(int j=0;j<flist.size();j++){
                if(list.get(i).getName().equals(flist.get(j).name)){
                    list.get(i).setIsfav(true);
                }
            }
        }
        return list;
    }
    public ArrayList<AudioModel> getfavlist(String playlistname){
        ArrayList<AudioModel> mlist=new ArrayList<>();
        try {
            FileInputStream stream=openFileInput(playlistname);
            StringBuffer buffer=new StringBuffer();
            int a;
            while ((a=stream.read())!=-1){
                buffer.append((char)a);
            }
            String[] array=buffer.toString().split("\n");

            if(array!=null){
                for(int j=0;j<list.size();j++){
                    for (int i=0;i<array.length;i++){
                        if(list.get(j).getName().equals(array[i])){
                            mlist.add(list.get(j));
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mlist;
    }
    public ArrayList<ArrayList<AudioModel>> forartist(ArrayList<AudioModel> mylist){
        ArrayList<ArrayList<AudioModel>> ulist=new ArrayList<>();
        Collections.sort(mylist,AudioModel.ArtistComparatoe);
        for(int i=0;i<mylist.size();i++){
            ArrayList<AudioModel> flist=new ArrayList<>();
            for(int j=i;j<mylist.size();j++){
                if(mylist.get(i).getArtist().equals(mylist.get(j).getArtist())){
                    flist.add(mylist.get(j));
                }
            }
            if(flist.size()>0){
                ulist.add(flist);
                i=i+flist.size()-1;
            }
        }
        return ulist;
    }
    public ArrayList<ArrayList<AudioModel>> foralbum(ArrayList<AudioModel> mylist){
        ArrayList<ArrayList<AudioModel>> ulist=new ArrayList<>();
        Collections.sort(mylist,AudioModel.AlbumComparator);
        for(int i=0;i<mylist.size();i++){
            ArrayList<AudioModel> flist=new ArrayList<>();
            for(int j=i;j<mylist.size();j++){
                if(mylist.get(i).getAlbum().equals(mylist.get(j).getAlbum())){
                    flist.add(mylist.get(j));
                }
            }
            if(flist.size()>0){
                ulist.add(flist);
                i=i+flist.size()-1;
            }
        }
        return ulist;
    }

    public void addtoplaylist(String name,String sname){
        File file=new File(this.getFilesDir()+"/"+name);
        if(file==null){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //for song add
            Writer writer=new BufferedWriter(new FileWriter(file,true));
            writer.append(sname+"\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removefromfavlist(String name,String sname){
        ArrayList<AudioModel> slist=getfavlist("fav");
        File file=new File(this.getFilesDir()+"/"+name);
        try {
            file.delete();
            file.createNewFile();
            Writer writer=new BufferedWriter(new FileWriter(file,true));
            for (int i=0;i<slist.size();i++){
                if(!sname.equals(slist.get(i))){
                    writer.append(sname+"\n");
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromSongFrag(ArrayList<AudioModel> playinglist,int position){
        this.playinglist=playinglist;
        suffleIsOn();
        this.position=position;
        addtonewthread(playinglist,position,true);
    }
    public void showplaylistpage(ArrayList<AudioModel> playinglist,String playlistName){
        ppage.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        maincontainer.setVisibility(View.INVISIBLE);
        this.playinglist=playinglist;
        suffleIsOn();
        if(playinglist.get(0).getImage()!=null){
            pimage.setImageBitmap(BitmapFactory.decodeFile(playinglist.get(0).getImage()));
        }
        else {
            pimage.setImageResource(R.drawable.mikusong);
        }
        ptext.setText(playlistName);
        //SongsAdapter adapter=new SongsAdapter(this,playinglist);

        //test.post(jaka);

        adapter1=new RecylerSongAdapter(this,playinglist,this);
        plistview.setAdapter(adapter1);
        plistview.setLayoutManager(new LinearLayoutManager(this));


        //plistview.setNestedScrollingEnabled(false);
        //plistview.suppressLayout(true);
    }
    //for nonscrolling recyclerview
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this){
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    };
    Handler test=new Handler();
    Runnable jaka=new Runnable() {
        @Override
        public void run() {
            adapter1=new RecylerSongAdapter(MainActivity.this,playinglist,MainActivity.this);
            plistview.setAdapter(adapter1);
            plistview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }
    };
}