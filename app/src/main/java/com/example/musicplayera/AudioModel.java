package com.example.musicplayera;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Comparator;

public class AudioModel implements Serializable {
    String path;
    String name;
    String artist;
    String album;
    String image;
    boolean isfav;

    public AudioModel(String path, String name, String artist, String album, String image,boolean isfav) {
        this.path = path;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.image = image;
        this.isfav=isfav;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIsfav() {
        return isfav;
    }

    public void setIsfav(boolean isfav) {
        this.isfav = isfav;
    }

    public  static Comparator<AudioModel> ArtistComparatoe=new Comparator<AudioModel>() {
        @Override
        public int compare(AudioModel o1, AudioModel o2) {
            String artist1=o1.getArtist().toUpperCase();
            String artist2=o2.getArtist().toUpperCase();
            return artist1.compareTo(artist2);
        }
    };
    public static Comparator<AudioModel> AlbumComparator=new Comparator<AudioModel>() {
        @Override
        public int compare(AudioModel o1, AudioModel o2) {
            String album1=o1.getAlbum().toLowerCase();
            String album2=o2.getAlbum().toLowerCase();
            return album1.compareTo(album2);
        }
    };
}
