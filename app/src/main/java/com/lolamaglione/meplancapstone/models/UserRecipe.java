package com.lolamaglione.meplancapstone.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@ParseClassName("Recipe")
// extend recipe class
public class UserRecipe extends ParseObject{
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_DAY_OF_WEEK = "dayOfTheWeek";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";

    private class myUserRecipe extends Recipe {

    }



    public List<String> getIngredients(){
        return (List<String>) get(KEY_INGREDIENTS);
    }

    public void setIngredientsArray(List<String> ingredients){
        put(KEY_INGREDIENTS, ingredients);
    }

    public String getKeyDayOfWeek(){
        return getString(KEY_DAY_OF_WEEK);
    }

    public void setKeyDayOfWeek(String dayOfWeek){
        put(KEY_DAY_OF_WEEK, dayOfWeek);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public String getKeyUrl(){
        return getString(KEY_URL);
    }

    public void setKeyUrl(String url){
        put(KEY_URL, url);
    }

    public void setKeyTitle(String title) { put(KEY_TITLE, title); }

}
