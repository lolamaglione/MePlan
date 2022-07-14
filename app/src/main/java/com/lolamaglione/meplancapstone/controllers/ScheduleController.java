package com.lolamaglione.meplancapstone.controllers;

import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * This is the schedule controller used to communicate with the schedule class in the Parse database
 */
@ParseClassName("Schedule")
public class ScheduleController extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_RECIPE = "recipe";
    public static final String KEY_DAY = "dayOfTheWeek";


    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseObject getRecipe(){
        return getParseObject(KEY_RECIPE);
    }

    public void setRecipe(ParseObject recipe){
        put(KEY_RECIPE, recipe);
    }

    public int getDayOfWeek(){
        return (int) get(KEY_DAY);
    }

    public void setDayOfWeek(int day){
        put(KEY_DAY, day);
    }
}
