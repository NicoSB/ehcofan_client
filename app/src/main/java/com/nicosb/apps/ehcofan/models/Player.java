package com.nicosb.apps.ehcofan.models;

import android.graphics.Bitmap;

/**
 * Created by Nico on 25.07.2016.
 */
public class Player {
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
    private String birthdate;
    private Bitmap playerImage;

    public Player(int id, String name, String surname, String position, String contract, String nationality, int number, int weight, int height, int ep_id, String birthdate) {
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
        this.birthdate = birthdate;
    }

    public Player(int id, String name, String surname, String position, String contract, String nationality, int number, int weight, int height, int ep_id, String birthdate, Bitmap playerImage) {
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
        this.birthdate = birthdate;
        this.playerImage = playerImage;
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

    public Bitmap getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(Bitmap playerImage) {
        this.playerImage = playerImage;
    }

    public String getFullName(){
        return name + " " + surname;
    }

}
