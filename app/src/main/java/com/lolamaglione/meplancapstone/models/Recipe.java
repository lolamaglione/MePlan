package com.lolamaglione.meplancapstone.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Recipe {

    private String title;
    private String url;
    private List<String> ingredients;
    private List<String> instructions;
    private String imageURL;

    public Recipe(){}

    public static Recipe fromJson(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();
        JSONObject jsonRecipe = (JSONObject) jsonObject.get("recipe");
        recipe.title = jsonRecipe.getString("label");
        recipe.ingredients = getIngredientList(jsonRecipe.getJSONArray("ingredientLines"));
        recipe.url = jsonRecipe.getString("url");
        recipe.imageURL = jsonRecipe.getString("image");

        return recipe;
    }

    private static List<String> getIngredientList(JSONArray ingredientLines) throws JSONException {
        List<String> ingredientList = new ArrayList<>();
        for (int i = 0; i < ingredientLines.length(); i++){
            ingredientList.add(ingredientLines.getString(i));
        }

        return ingredientList;
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

    public List<String> getIngredients() {
        return ingredients;
    }
}
