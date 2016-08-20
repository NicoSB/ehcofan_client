package com.nicosb.apps.ehcofan.models;

import android.graphics.Bitmap;

/**
 * Created by Nico on 23.07.2016.
 */
public class PlayerWrapper {
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
    private String player_image_file_name;
    private String updated_at;

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

    public String getPlayer_image_file_name() {
        return player_image_file_name;
    }

    public void setPlayer_image_file_name(String player_image_file_name) {
        this.player_image_file_name = player_image_file_name;
    }

    public Player toPlayer(){
        return new Player(id, name, surname, position, contract, nationality, number, weight, height, ep_id, birthdate);
    }

    public Player toPlayer(Bitmap bitmap){
        return new Player(id, name, surname, position, contract, nationality, number, weight, height, ep_id, birthdate, bitmap);
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
