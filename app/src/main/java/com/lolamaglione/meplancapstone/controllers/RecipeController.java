package com.lolamaglione.meplancapstone.controllers;

import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Recipe")
// extend recipe class
public class RecipeController extends ParseObject{
    public static final String KEY_SPECIFC_INGREDIENTS = "specificIngredients";
    public static final String KEY_IMAGE = "imageURL";
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_GENERAL_INGREDIENTS = "generalIngredients";

    public List<String> getSpecificIngredients(){
        return (List<String>) get(KEY_SPECIFC_INGREDIENTS);
    }

    public void setSpecificIngredientsArray(List<String> ingredients){
        put(KEY_SPECIFC_INGREDIENTS, ingredients);
    }

    public List<String> getGeneralIngredients(){
        return (List<String>) get(KEY_GENERAL_INGREDIENTS);
    }

    public void setGeneralIngredientsArray(List<String> ingredients){
        put(KEY_GENERAL_INGREDIENTS, ingredients);
    }

    public String getImageURL(){
        return getString(KEY_IMAGE);
    }

    public void setImage(String imageURL){
        put(KEY_IMAGE, imageURL);
    }

    public String getUrl(){
        return getString(KEY_URL);
    }

    public void setUrl(String url){
        put(KEY_URL, url);
    }

    public void setTitle(String title) { put(KEY_TITLE, title); }

    public String getTitle () {
        return get(KEY_TITLE).toString();
    }

}
