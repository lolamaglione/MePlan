package com.lolamaglione.meplancapstone.controllers;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Ingredient")
public class IngredientController extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_MEASURE = "measure";
    public static final String KEY_DAY = "day";
    public static final String KEY_RECIPE = "recipe";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public int getAmount(){
        return getInt(KEY_AMOUNT);
    }

    public void setAmount(int amount){
        put(KEY_AMOUNT, amount);
    }

    public String getMeasure(){
        return getString(KEY_MEASURE);
    }

    public void setMeasure(String measure){
        put(KEY_MEASURE, measure);
    }

    public int getDay(){
        return getInt(KEY_DAY);
    }

    public void setDay(int day){
        put(KEY_DAY, day);
    }

    public ParseObject getRecipe(){
        return getParseObject(KEY_RECIPE);
    }

    public void setRecipe(ParseObject recipe){
        put(KEY_RECIPE, recipe);
    }

}
