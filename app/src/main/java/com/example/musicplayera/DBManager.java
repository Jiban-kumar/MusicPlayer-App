package com.example.musicplayera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {


    public static final int DB_VERSION=1;
    public static final String DB_NAME="songsdb";
    public static final String TABLE_USER="fav";
    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";
    public static final String KEY_PATH="path";
    public static final String KEY_ARTIST="artist";
    public static final String KEY_ALBUM="album";
    Context context;
    public DBManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+TABLE_USER+"("
                +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_NAME+" TEXT,"
                +KEY_PATH+" TEXT,"+KEY_ARTIST+" TEXT,"+KEY_ALBUM+" TEXT"+")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        //create table again
        onCreate(db);
    }
    //for insert
    public void insertData(String name,String path,String artist,String album){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_NAME,name);
        values.put(KEY_PATH,path);
        values.put(KEY_ARTIST,artist);
        values.put(KEY_ALBUM,album);
        long newrowid=database.insert(TABLE_USER,null,values);
        values.put(KEY_ID,newrowid);
        database.close();
    }
    //getData
    public ArrayList<AudioModel> getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<AudioModel> list=new ArrayList<>();
        String query="SELECT name, path, artist, album FROM "+TABLE_USER;
        Cursor cursor=db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String path = cursor.getString(cursor.getColumnIndex(KEY_PATH));
            String artist = cursor.getString(cursor.getColumnIndex(KEY_ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(KEY_ALBUM));


//            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            byte[] rawArt;
//            Bitmap art = null;
//            BitmapFactory.Options bfo=new BitmapFactory.Options();
//
//            mmr.setDataSource(context, uri);
//            rawArt = mmr.getEmbeddedPicture();
//
//            // if rawArt is null then no cover art is embedded in the file or is not
//            // recognized as such.
//            if (null != rawArt)
//                art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
//            else


            list.add(new AudioModel(path, name, artist, album, null, true));
        }
        try {

            }
            catch (Exception ee){
                Toast.makeText(context, ee+"", Toast.LENGTH_SHORT).show();
            }

        return list;
    }
    //Delete Data
    public void deleteData(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_USER,KEY_NAME+"=?",new String[]{name});
        db.close();
    }

}
