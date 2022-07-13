package com.lolamaglione.meplancapstone.controllers;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Ingredient")
public class IngredientController extends ParseObject {

    public static final String KEY_INGREDIENT_NAME = "name";
    public static final String KEY_USER = "user";
    public static final String KEY_DAY = "day";
    public static final String KEY_RECIPE_ID= "recipeID";
    public static final String KEY_CHECKED = "checked";

    public String getName(){
        return (String) get(KEY_INGREDIENT_NAME);
    }

    public void setName(String name){
        put(KEY_INGREDIENT_NAME, name);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser userID){
        put(KEY_USER, userID);
    }

    public int getDay(){
        return (int) get(KEY_DAY);
    }

    public void setDay(int day){
        put(KEY_DAY, day);
    }

    public String getRecipeID(){
        return (String) get(KEY_RECIPE_ID);
    }

    public void setRecipeID(String recipeID){
        put(KEY_RECIPE_ID, recipeID);
    }

    public boolean getIsChecked(){
        return (boolean) get(KEY_CHECKED);
    }

    public void setIsChecked(boolean isChecked){
        put(KEY_CHECKED, isChecked);
    }
}
