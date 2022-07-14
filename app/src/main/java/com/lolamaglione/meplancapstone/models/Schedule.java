package com.lolamaglione.meplancapstone.models;


import androidx.room.Entity;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * this is the schedule model, it mirrors the scheudleController and creates schedule java objects
 */
@Entity
public class Schedule {

    private String userID;
    private String recipeID;
    private int dayOfTheWeek;

    public Schedule() {
    }

   public String getUserID(){
        return userID;
   }

   public void setUserID(String userID){
        this.userID = userID;
   }

   public String getRecipeID(){
        return recipeID;
   }

   public void setRecipeID(String recipeID){
        this.recipeID = recipeID;
   }

   public int getDayOfTheWeek(){
        return dayOfTheWeek;
   }

   public void setDayOfTheWeek(int dayOfTheWeek){
        this.dayOfTheWeek = dayOfTheWeek;
   }

}
