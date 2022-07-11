package com.lolamaglione.meplancapstone;

import com.lolamaglione.meplancapstone.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseRecipe {

    public ParseRecipe(){

    }

    public static Recipe fromJson(JSONObject jsonObject, String query) throws JSONException {
        Recipe recipe = new Recipe();
        JSONObject jsonRecipe = (JSONObject) jsonObject.get("recipe");
        recipe.setTitle(jsonRecipe.getString("label"));
        recipe.setSpecificIngredients(getSpecificIngredientList(jsonRecipe.getJSONArray("ingredientLines")));
        recipe.setUrl(jsonRecipe.getString("url"));
        recipe.setImageUrl(jsonRecipe.getString("image"));
        recipe.setGeneralIngredients(getGeneralIngredientList(jsonRecipe.getJSONArray("ingredients")));
        recipe.setRecipeIngredientMap(getGeneralIngredientMap(jsonRecipe.getJSONArray("ingredients")));
        recipe.setCookTime(jsonRecipe.getInt("totalTime"));
        recipe.setQuery(query);
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

    private static HashMap<String, HashMap<String, String>> getGeneralIngredientMap(JSONArray ingredients) throws JSONException {
        HashMap<String, HashMap<String, String>> ingredientMap = new HashMap<>();
        for (int i = 0; i < ingredients.length(); i++){
            String ingredient = ingredients.getJSONObject(i).getString("food");
            int amount = ingredients.getJSONObject(i).getInt("quantity");
            String measure = ingredients.getJSONObject(i).getString("measure");
            HashMap<String, String> map = new HashMap<>();
            map.putIfAbsent("amount", String.valueOf(amount));
            map.putIfAbsent("measure", measure);
            ingredientMap.putIfAbsent(ingredient, map);
        }
        return ingredientMap;
    }

    public static List<Recipe> fromJsonArray(JSONArray jsonArray, String query) throws JSONException{
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            recipes.add(fromJson(jsonArray.getJSONObject(i), query));
        }
        return recipes;
    }
}
