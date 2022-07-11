package com.lolamaglione.meplancapstone.controllers;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Ingredient")
public class IngredientController extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_MEASURE = "measure";

    public String getName(){
        return (String) get(KEY_NAME);
    }

    public void setName(String name){
        put(KEY_NAME, name);
    }

    public int getAmount(){
        return (int) get(KEY_AMOUNT);
    }

    public void setAmount(String amount){
        put(KEY_AMOUNT, Integer.valueOf(amount));
    }

    public String getMeasure(){
        return (String) get(KEY_MEASURE);
    }

    public void setMeasure(String measure){
        put(KEY_MEASURE, measure);
    }
}
