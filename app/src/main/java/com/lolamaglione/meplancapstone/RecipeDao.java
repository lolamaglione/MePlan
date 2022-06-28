package com.lolamaglione.meplancapstone;

import androidx.room.Dao;
import androidx.room.Query;

import com.lolamaglione.meplancapstone.models.Recipe;

@Dao
public class RecipeDao {

    @Query("SELECT * FROM Recipe where title := :title")
    public Recipe getByTitle(String title) {
        return null;
    }



}
