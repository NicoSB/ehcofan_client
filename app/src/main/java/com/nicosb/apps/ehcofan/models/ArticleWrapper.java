package com.nicosb.apps.ehcofan.models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nico on 30.06.2016.
 */
public class ArticleWrapper implements Parcelable {
    public static final Creator<ArticleWrapper> CREATOR = new Creator<ArticleWrapper>() {
        @Override
        public ArticleWrapper createFromParcel(Parcel in) {
            return new ArticleWrapper(in);
        }

        @Override
        public ArticleWrapper[] newArray(int size) {
            return new ArticleWrapper[size];
        }
    };
    private int id;
    private String title;
    private String text;
    private String url;
    private String date;
    private String news_image_file_name;

    private ArticleWrapper(Parcel in) {
        id = in.readInt();
        title = in.readString();
        text = in.readString();
        url = in.readString();
        date = in.readString();
        news_image_file_name = in.readString();
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

    public int getId() {
        return id;
    }

    public String getNews_image_file_name() {
        return news_image_file_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeString(url);
        parcel.writeString(date);
        parcel.writeString(news_image_file_name);
    }

    public Article toNoPicArticle(){
        return new Article(title, text, url, date, null);
    }
}
