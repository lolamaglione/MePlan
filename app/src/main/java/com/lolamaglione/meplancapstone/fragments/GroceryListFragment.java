package com.lolamaglione.meplancapstone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.RecipeSuggestions;
import com.lolamaglione.meplancapstone.adapters.DaysListAdapter;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.models.Ingredient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 *
 * This fragment shows the list of ingredients needed for each day, implements the daylistAdpater
 */
public class GroceryListFragment extends Fragment {

    private RecyclerView rvDaysList;
    private HashMap<Integer, List<RecipeController>> allAddedRecipes;
    private DaysListAdapter listAdapter;
    private HashMap<Integer, String> intToDay = new HashMap<Integer, String>(){{put(0, "Monday"); put(1, "Tuesday"); put(2, "Wednesday"); put(3, "Thursday");
    put(4, "Friday"); put(5, "Saturday"); put(6, "Sunday");}
    };
    public static final String TAG = "grocery list fragment";
    private static RecipeSuggestions.Trie trie = new RecipeSuggestions.Trie();

    public GroceryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grocery_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvDaysList = view.findViewById(R.id.rvDaysList);
        List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.days_array));
        allAddedRecipes = new HashMap<>();
        fillHashMap(daysOfWeek);

        // setting up adapter and layout manager
        listAdapter = new DaysListAdapter(getContext(), intToDay, allAddedRecipes, trie);
        rvDaysList.setAdapter(listAdapter);
        rvDaysList.setLayoutManager(new LinearLayoutManager(getContext()));
        queryUserRecipes();
    }

    private void fillHashMap(List<String> daysOfWeek) {
        for(int i = 0; i < daysOfWeek.size(); i++){
            allAddedRecipes.putIfAbsent(i, new ArrayList<>());
        }
    }
    public static void updateTrieOne(Ingredient ingredient, int position){
        trie.insertIngredient(ingredient, position);
    }

    public static void removeTrie(List<String> ingredientsToRemove, int position){
        if (!trie.isNull()){
            for(String ingredient : ingredientsToRemove){
                trie.removeIngredient("" + position + ingredient);
            }
        }
    }

    public void queryUserRecipes(){

        ParseQuery<ScheduleController> query = ParseQuery.getQuery(ScheduleController.class);
        // include data referred by user key
        query.include(ScheduleController.KEY_USER);
        query.include(ScheduleController.KEY_RECIPE);
        query.whereEqualTo(ScheduleController.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ScheduleController>() {
            @Override
            public void done(List<ScheduleController> recipes, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue getting recipes", e);
                    return;
                }

                for (ScheduleController recipe: recipes){
                    allAddedRecipes.putIfAbsent(recipe.getDayOfWeek(), new ArrayList<>());
                    allAddedRecipes.get(recipe.getDayOfWeek()).add((RecipeController) recipe.getRecipe());
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}