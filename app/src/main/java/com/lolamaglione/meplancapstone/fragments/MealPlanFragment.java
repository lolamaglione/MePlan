package com.lolamaglione.meplancapstone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.DaysAdapter;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment shows the recipes that the user has added for each day, implements the DayAdapter
 */
public class MealPlanFragment extends Fragment {

    private RecyclerView rvDays;
    private HashMap<Integer, List<RecipeController>> allAddedRecipes;
    private DaysAdapter dayAdapter;
    HashMap<Integer, String> intToDay = new HashMap<Integer, String>(){{put(0, "Monday"); put(1, "Tuesday"); put(2, "Wednesday"); put(3, "Thursday");
        put(4, "Friday");put(5, "Saturday"); put(6, "Sunday");}};;
    public static final String TAG = "Meal Plan Fragment";

    public MealPlanFragment() {
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
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDays = view.findViewById(R.id.rvDays);
        List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.days_array));
        allAddedRecipes = new HashMap<>();
        // Add a arraylist and a day to the added recipe hashmap
        initializeAllAddedRecipesMap(daysOfWeek);
        // create a new adapter to see the recipes for each specific day
        dayAdapter = new DaysAdapter(getContext(), intToDay, allAddedRecipes);
        rvDays.setAdapter(dayAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvDays.setLayoutManager(linearLayoutManager);
        queryUserRecipes();
    }

    private void initializeAllAddedRecipesMap(List<String> daysOfWeek) {
        for(int i = 0; i < daysOfWeek.size(); i++){
            allAddedRecipes.putIfAbsent(i, new ArrayList<>());
        }
    }

    public void queryUserRecipes(){
        // specify what type of data we want to query - UserRecipe.class
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
                    dayAdapter.notifyItemChanged(recipe.getDayOfWeek());
                }
            }
        });

    }
}