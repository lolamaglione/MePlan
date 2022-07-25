package com.lolamaglione.meplancapstone.models;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lolamaglione.meplancapstone.models.Recipe;

import java.util.List;

/**
 * this is the data access object to save and access data to and from the room database
 */
@Dao
public interface RecipeDao {

    @Query("SELECT Recipe.* FROM Recipe WHERE Recipe.`query` = :this_query AND Recipe.cuisine = :this_cuisine LIMIT 100")
    List<Recipe> recentItems(String this_query, String this_cuisine);

    @Query("SELECT Recipe.* FROM Recipe WHERE Recipe.`query` LIKE '%' || :this_query || '%'")
    List<Recipe> recentItemsNoQuery(String this_query);

    @Query("SELECT Recipe.* FROM Recipe WHERE Recipe.`query` = :this_query LIMIT 100")
    List<Recipe> sortedSuggestions(String this_query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Recipe[] recipes);

}
