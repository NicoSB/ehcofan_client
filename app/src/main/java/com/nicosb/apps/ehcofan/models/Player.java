package com.nicosb.apps.ehcofan.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.nicosb.apps.ehcofan.CacheDBHelper;

/**
 * Created by Nico on 25.07.2016.
 */
public class Player implements Parcelable {
    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    private int id;
    private String name;
    private String surname;
    private String position;
    private String contract;
    private String nationality;
    private int number;
    private int weight;
    private int height;
    private int ep_id;
    private int games;
    private int goals;
    private int assists;
    private int pim;
    private String birthdate;
    private Bitmap playerImage;

    public Player(int id, String name, String surname, String position, String contract, String nationality, int number, int weight, int height, int ep_id, int games, int goals, int assists, int pim, String birthdate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.contract = contract;
        this.nationality = nationality;
        this.number = number;
        this.weight = weight;
        this.height = height;
        this.ep_id = ep_id;
        this.games = games;
        this.goals = goals;
        this.assists = assists;
        this.pim = pim;
        this.birthdate = birthdate;
    }

    public Player(int id, String name, String surname, String position, String contract, String nationality, int number, int weight, int height, int ep_id, int games, int goals, int assists, int pim, String birthdate, Bitmap playerImage) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.contract = contract;
        this.nationality = nationality;
        this.number = number;
        this.weight = weight;
        this.height = height;
        this.ep_id = ep_id;
        this.games = games;
        this.goals = goals;
        this.assists = assists;
        this.pim = pim;
        this.birthdate = birthdate;
        this.playerImage = playerImage;
    }

    protected Player(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        position = in.readString();
        contract = in.readString();
        nationality = in.readString();
        number = in.readInt();
        weight = in.readInt();
        height = in.readInt();
        ep_id = in.readInt();
        games = in.readInt();
        goals = in.readInt();
        assists = in.readInt();
        pim = in.readInt();
        birthdate = in.readString();
        playerImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static Player populatePlayer(Cursor c) {
        return new Player(c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_ID)),
                c.getString(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_NAME)),
                c.getString(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_SURNAME)),
                c.getString(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_POSITION)),
                c.getString(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_CONTRACT)),
                c.getString(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_NATIONALITY)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_NUMBER)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_WEIGHT)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_HEIGHT)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_EP_ID)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_GAMES)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_GOALS)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_ASSISTS)),
                c.getInt(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_PIM)),
                c.getString(c.getColumnIndexOrThrow(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_BIRTHDATE))
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getEp_id() {
        return ep_id;
    }

    public void setEp_id(int ep_id) {
        this.ep_id = ep_id;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGermanBirthdate() {
        return birthdate.substring(8, 10) + "." + birthdate.substring(5, 7) + "." + birthdate.substring(0, 4);
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getPim() {
        return pim;
    }

    public void setPim(int pim) {
        this.pim = pim;
    }

    public Bitmap getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(Bitmap playerImage) {
        this.playerImage = playerImage;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(number);
        parcel.writeInt(weight);
        parcel.writeInt(height);
        parcel.writeInt(ep_id);
        parcel.writeInt(games);
        parcel.writeInt(goals);
        parcel.writeInt(assists);
        parcel.writeInt(pim);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(position);
        parcel.writeString(contract);
        parcel.writeString(nationality);
        parcel.writeString(birthdate);
        playerImage.writeToParcel(parcel, 0);
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_ID, id);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_NAME, name);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_SURNAME, surname);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_POSITION, position);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_CONTRACT, contract);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_NATIONALITY, nationality);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_BIRTHDATE, birthdate);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_NUMBER, number);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_WEIGHT, weight);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_HEIGHT, height);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_EP_ID, ep_id);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_GAMES, games);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_GOALS, goals);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_ASSISTS, assists);
        contentValues.put(CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_PIM, pim);

        return contentValues;
    }
}
