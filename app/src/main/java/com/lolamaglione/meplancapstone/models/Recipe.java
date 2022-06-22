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

    private String title;
    private String url;
    private List<String> specificIngredients;
    private List<String> instructions;
    private String imageURL;
    private List<String> generalIngredients;
    private int percentageMatch;

    public Recipe(){}

    public static Recipe fromJson(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();
        JSONObject jsonRecipe = (JSONObject) jsonObject.get("recipe");
        recipe.title = jsonRecipe.getString("label");
        recipe.specificIngredients = getSpecificIngredientList(jsonRecipe.getJSONArray("ingredientLines"));
        recipe.url = jsonRecipe.getString("url");
        recipe.imageURL = jsonRecipe.getString("image");
        recipe.generalIngredients = getGeneralIngredientList(jsonRecipe.getJSONArray("ingredients"));
        return recipe;
    }

    private static List<String> getSpecificIngredientList(JSONArray ingredientLines) throws JSONException {
        List<String> ingredientList = new ArrayList<>();
        for (int i = 0; i < ingredientLines.length(); i++){
            ingredientList.add(ingredientLines.getString(i));
        }

        return ingredientList;
    }

    private static List<String> getGeneralIngredientList(JSONArray ingredients) throws JSONException {
        List<String> ingredientList = new ArrayList<>();
        for (int i = 0; i < ingredients.length(); i++){
            String ingredient = ingredients.getJSONObject(i).getString("food");
            ingredientList.add(ingredient);
        }
        return  ingredientList;
    }

    public int getPercentageMatch(){
        return percentageMatch;
    }

    public void setPercentageMatch(int percentage) {
        percentageMatch = percentage;
    }

    public String getURL(){
        return url;
    }

    public String getTitle(){
        return title;
    }

    public String getImageURL() {return imageURL;}

    public static List<Recipe> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            recipes.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return recipes;
    }

    public List<String> getSpecificIngredients() {
        return specificIngredients;
    }

    public List<String> getGeneralIngredients() { return  generalIngredients; }
}
