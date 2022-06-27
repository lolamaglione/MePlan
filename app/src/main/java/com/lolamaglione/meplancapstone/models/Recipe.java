package com.lolamaglione.meplancapstone.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * this gives us all the information of the recipe to display in the recipe feed
 */
@Parcel
public class Recipe {

    public String title;
    public String url;
    public List<String> specificIngredients;
    public String imageURL;
    private List<String> generalIngredients;
    private int percentageMatch;
    private boolean hasBeenSaved;

    public Recipe(String title, String url, List<String> specificIngredients, String imageURL,
                  List<String> generalIngredients, boolean hasBeenSaved){
        this.title = title;
        this.url = url;
        this.specificIngredients = specificIngredients;
        this.imageURL = imageURL;
        this.generalIngredients = generalIngredients;
        this.hasBeenSaved = hasBeenSaved;
    }

    public Recipe(){

    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL(){
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSpecificIngredients() {
        return specificIngredients;
    }

    public void setSpecificIngredients(List<String> specificIngredients){
        this.specificIngredients = specificIngredients;
    }

    public String getImageURL() {return imageURL;}

    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }

    public List<String> getGeneralIngredients() { return  generalIngredients; }

    public void setGeneralIngredients(List<String> generalIngredients){
        this.generalIngredients = generalIngredients;
    }

    public int getPercentageMatch(){
        return percentageMatch;
    }

    public void setPercentageMatch(int percentage) {
        percentageMatch = percentage;
    }

    public boolean getHasBeenSaved() { return hasBeenSaved;}

    public void setHasBeenSaved(boolean saved) {hasBeenSaved = saved;}



}
