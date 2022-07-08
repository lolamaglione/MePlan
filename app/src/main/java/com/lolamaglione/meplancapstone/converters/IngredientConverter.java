package com.lolamaglione.meplancapstone.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lolamaglione.meplancapstone.models.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IngredientConverter {

    private static Gson gson = new Gson();
    @TypeConverter
    public static List<Ingredient> fromJSONtoIngredient(String jsonList){
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        List<Ingredient> listOfStrings = gson.fromJson(jsonList, type);
        return listOfStrings;
    }

    @TypeConverter
    public static String fromIngredientToJSON(List<Ingredient> stringList){
        return gson.toJson(stringList);
    }
}
