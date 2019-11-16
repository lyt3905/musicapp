package com.example.runtimepermissiontest;


import java.io.Serializable;

public class MediaInfo implements Serializable {

    private long _id;
    private String uri;// 路径
    private String title;
    private String artist;// 艺术家

    public MediaInfo(long _id, String uri, String title, String artist) {
        this._id = _id;
        this.uri = uri;
        this.title = title;
        this.artist = artist;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MediaInfo{" +
                "_id=" + _id +
                ", uri='" + uri + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}