package com.nicosb.apps.ehcofan.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nico on 15.07.2016.
 */
public class Article implements Parcelable {
    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    String title;
    String text;
    String url;
    String date;
    Bitmap news_image;

    public Article(String title, String text, String url, String date, Bitmap news_image) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.date = date;
        this.news_image = news_image;
    }

    protected Article(Parcel in) {
        title = in.readString();
        text = in.readString();
        url = in.readString();
        date = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getNews_image() {
        return news_image;
    }

    public String getDisplayDate() {
        String day = date.substring(8, 10);
        String month = date.substring(5, 7);

        return day + "." + month;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeString(url);
        parcel.writeString(date);
    }
}
