package com.lolamaglione.meplancapstone.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListConverter {
    private static Gson gson = new Gson();
    @TypeConverter
    public static List<String> fromJSONtoList(String jsonList){
       Type type = new TypeToken<ArrayList<String>>() {}.getType();
       List<String> listOfStrings = gson.fromJson(jsonList, type);
       return listOfStrings;
    }

    @TypeConverter
    public static String fromListToJSON(List<String> stringList){
        return gson.toJson(stringList);
    }
}
