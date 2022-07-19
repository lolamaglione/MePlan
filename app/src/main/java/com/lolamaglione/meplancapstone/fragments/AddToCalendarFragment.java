package com.lolamaglione.meplancapstone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.controllers.IngredientController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.models.Ingredient;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This Fragment incorporates modular activity in order to be able to add a recipe to the meal plan
 */
public class AddToCalendarFragment extends DialogFragment {

    private static final String TAG = "addActivity";
    private Spinner dropDown;
    private Button btnConfirm;
    private String day;
    public HashMap<String, Integer> dayToInt;
    private CircularProgressIndicator progressBarConfirm;

    public AddToCalendarFragment() {
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
        return inflater.inflate(R.layout.fragment_add_to_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayToInt = new HashMap<>();
        fillHashMap(dayToInt);
        dropDown = view.findViewById(R.id.dropDown);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        progressBarConfirm = view.findViewById(R.id.linearProgressBarConfirm);
        progressBarConfirm.setVisibility(CircularProgressIndicator.GONE);
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.days_array, android.R.layout.simple_spinner_item);
        dropDown.setAdapter(adapter);
                dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                day = "";
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarConfirm.setShowAnimationBehavior(CircularProgressIndicator.SHOW_NONE);
                progressBarConfirm.setVisibility(CircularProgressIndicator.VISIBLE);
                RecipeController recipeController = null;
                int day_int = dayToInt.get(day);
                try {
                    List<Ingredient> updateIngredients = new ArrayList<>();
                    recipeController = createDBRecipe(day_int, updateIngredients);
                    List<String> generalIngredients = recipeController.getGeneralIngredients();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ScheduleController scheduleController = new ScheduleController();
                scheduleController.setUser(ParseUser.getCurrentUser());
                scheduleController.setRecipe(recipeController);
                scheduleController.setDayOfWeek(day_int);
                scheduleController.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!= null){
                            Log.e("addActivity", "error: " + e);
                        }
                        Log.i("addActivity", "success!");
                        progressBarConfirm.setVisibility(CircularProgressIndicator.GONE);
                        dismiss();
                    }
                });
                Toast.makeText(getContext(), "Saved Recipe!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillHashMap(HashMap<String, Integer> dayToInt){
        List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.days_array));
        for (int i = 0; i < daysOfWeek.size(); i++){
            dayToInt.putIfAbsent(daysOfWeek.get(i), i);
        }
    }

    public RecipeController createDBRecipe(int day, List<Ingredient> updateIngredients) throws ParseException {
        // recipe we want to add
        Recipe recipe = Parcels.unwrap(getArguments().getParcelable(Recipe.class.getSimpleName()));
        String title = recipe.getTitle();
        // querying all the recipes that are already in the DataBase
        ParseQuery<RecipeController> query = ParseQuery.getQuery(RecipeController.class);
        query.whereEqualTo("title", title);
        query.include(RecipeController.KEY_TITLE);

        List<RecipeController> recipesWithSameTitle = query.find();
        Log.i(TAG, "this is the recipe with the same titles: " + recipesWithSameTitle);
        if (recipesWithSameTitle.size() == 0){
            RecipeController recipeController = new RecipeController();
            recipeController.setSpecificIngredientsArray(recipe.getSpecificIngredients());
            recipeController.setUrl(recipe.getURL());
            List<String> generalIngredientArray = recipe.getGeneralIngredients();
            recipeController.setGeneralIngredientsArray(generalIngredientArray);
            recipeController.setTitle(recipe.getTitle());
            recipeController.setImage(recipe.getImageURL());
            recipeController.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if ( e != null){
                        Log.e(TAG,"error saving recipe");
                    }
                    createIngredientInDB(generalIngredientArray, recipeController.getObjectId(), day, updateIngredients);
                }
            });
            return recipeController;
        }
        // is they are the recipe with the same title, I don't want to save it to the parse database
        // twice but I do want to add it to the schedule
        RecipeController recipeController = recipesWithSameTitle.get(0);
        return recipeController;
    }

    // TODO: you cannot save in the background, make UI so that the user knows it is waiting
    private void createIngredientInDB(List<String> generalIngredientArray, String recipeID, int day, List<Ingredient> updateIngredients) {
        for (String ingredient : generalIngredientArray){
            IngredientController newIngredient = new IngredientController();
            newIngredient.setName(ingredient);
            newIngredient.setRecipeID(recipeID);
            newIngredient.setUser(ParseUser.getCurrentUser());
            newIngredient.setDay(day);
            newIngredient.setIsChecked(false);
            newIngredient.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null){
                        Log.e(TAG, "error saving ingredient " + e);
                    } else {
                        Log.i(TAG, "savedIngredients");
                        // new to create an ingredient object in order to update the trie
                        Ingredient ingredientObject = new Ingredient();
                        ingredientObject.setIngredientID(newIngredient.getObjectId());
                        ingredientObject.setIngredientName(newIngredient.getName());
                        ingredientObject.setChecked(newIngredient.getIsChecked());
                        ingredientObject.setUserId(newIngredient.getUser().getObjectId());
                        ingredientObject.setRecipeId(newIngredient.getRecipeID());
                        ingredientObject.setDay(newIngredient.getDay());
                        updateIngredients.add(ingredientObject);
                        GroceryListFragment.updateTrieOne(ingredientObject, day);
                    }
                }
            });


        }
    }
}