package com.example.musicplayera;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class OnlineActivity extends AppCompatActivity {

    private TextView mTextView,name1,name2;
    Button button;
    StorageReference storageReference;
    MediaPlayer player;
    ImageView image,image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        mTextView = (TextView) findViewById(R.id.text);
        button=findViewById(R.id.play);
        name1=findViewById(R.id.song1);
        name2=findViewById(R.id.song2);
        image=findViewById(R.id.image);
        image2=findViewById(R.id.image2);

        storageReference= FirebaseStorage.getInstance().getReference().child("MyMusic");
        //storageReference= FirebaseStorage.getInstance().getReference().child("MyMusic/Ayzha Nyree- HOLY CLASS (Official).mp3");
        //storageReference=FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/musicplayer-55a7f.appspot.com/o/MyMusic%2FAyzha%20Nyree-%20HOLY%20CLASS%20(Official).mp3?alt=media&token=9ce756cd-1608-4759-8f37-117b5ba69557");

        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                int cou=0;
                for(StorageReference result:listResult.getItems()){
                    cou++;
                    if(cou==1){
                        name1.setText(result.getName());
                    }
                    else {
                        name2.setText(result.getName());
                    }
                }
//                mTextView.setText(a);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        StorageReference ref2=FirebaseStorage.getInstance().getReference().child("MyMusicPic");
        ref2.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                int cou=0;
                for(StorageReference reference:listResult.getItems()){
                    cou++;
                    if(cou==1){
                        String a="https://firebasestorage.googleapis.com/v0/b/musicplayer-55a7f.appspot.com/o/MyMusicPic%2F22.jpg?alt=media&token=d3b55c88-9c56-4968-8133-d682b5034d42";
                        Glide.with(OnlineActivity.this).asBitmap().load(a).into(image);


                    }
                    else {
                        StorageReference sr=FirebaseStorage.getInstance().getReference();
                        Glide.with(OnlineActivity.this).load(sr).into(image2);
                        mTextView.setText(reference.toString());

                        //reference.

                    }
                }
            }
        });
//        try {
//            File file=File.createTempFile("Ayzha Nyree- HOLY CLASS (Official)",".mp3");
//            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Uri uri=Uri.fromFile(file);
//                    player=MediaPlayer.create(OnlineActivity.this,uri);
//                    //player.setDataSource(taskSnapshot.toString());
//                    //player.prepare();
//                    player.start();
//
//                    Uri uri1=Uri.fromFile(file);
//                    Cursor cursor=OnlineActivity.this.getContentResolver().query(uri1,null,null,null,null);
//                    int count=0;
//                    String i="";
//                    if(cursor!=null){
////                        String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
////                        Toast.makeText(OnlineActivity.this, name+"", Toast.LENGTH_SHORT).show();
//                        while (cursor.moveToNext()){
//                            count++;
//                           String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
//                           i=i+name+",";
//                            Toast.makeText(OnlineActivity.this, name+"", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//                        Toast.makeText(OnlineActivity.this, "null", Toast.LENGTH_SHORT).show();
//                    }
//                    //Toast.makeText(OnlineActivity.this, i+"", Toast.LENGTH_SHORT).show();
//                    mTextView.setText(i);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(OnlineActivity.this,"Failed",Toast.LENGTH_SHORT).show();
//                }
//            });
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        player.prepare();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    player.start();
//                    if(file==null){
//                        Toast.makeText(OnlineActivity.this, "File not creted", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}