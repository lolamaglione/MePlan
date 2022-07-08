package com.lolamaglione.meplancapstone.models;

import com.parse.ParseClassName;

import org.parceler.Parcel;

@Parcel
public class Ingredient {
    private String title;
    private int amount;
    private String measure;
    private int day;

    public Ingredient(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMeasure(String measure){
        this.measure = measure;
    }

    public String getMeasure(){
        return measure;
    }



}
