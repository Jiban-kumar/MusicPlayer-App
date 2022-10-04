package com.example.musicplayera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBManagerforplaylist extends SQLiteOpenHelper {

    public static String tname;

    public static final int DB_VERSION=1;
    public static final String DB_NAME="songsdb";
    public static final String TABLE_USER="playlist";
    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";

    public DBManagerforplaylist(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+TABLE_USER+"("
                +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_NAME+" TEXT"
                +")";
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
    public void insertData(String name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_NAME,name);
        long newrowid=database.insert(TABLE_USER,null,values);
        values.put(KEY_ID,newrowid);
        database.close();
    }
    //getData
    public ArrayList<String> getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<String> list=new ArrayList<>();
        String query="SELECT name FROM "+TABLE_USER;
        Cursor cursor=db.rawQuery(query,null);
        while (cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex(KEY_NAME));
            list.add(name);
        }
        return list;
    }
    //Delete Data
    public void deleteData(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_USER,KEY_NAME+"=?",new String[]{name});
        db.close();
    }

    public void tablename(String tname) {
        this.tname=tname;
    }
}
