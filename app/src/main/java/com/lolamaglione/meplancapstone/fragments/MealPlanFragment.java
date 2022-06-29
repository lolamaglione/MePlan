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
import com.lolamaglione.meplancapstone.adapters.DaysAdapter;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
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
 */
public class MealPlanFragment extends Fragment {

    private RecyclerView rvDays;
    private HashMap<Integer, List<RecipeController>> allAddedRecipes;
    private DaysAdapter dayAdapter;
    HashMap<Integer, String> intToDay = new HashMap<>();
    public static final String TAG = "Meal Plan Fragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MealPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealPlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealPlanFragment newInstance(String param1, String param2) {
        MealPlanFragment fragment = new MealPlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        //TODO: change this
        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        allAddedRecipes = new HashMap<>();
        fillHashMap(daysOfWeek);
        dayAdapter = new DaysAdapter(getContext(), intToDay, allAddedRecipes);
        rvDays.setAdapter(dayAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvDays.setLayoutManager(linearLayoutManager);
        queryUserRecipes();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clear, menu);
        MenuItem clear = menu.findItem(R.id.action_clear);

        clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ParseQuery<ScheduleController> query = ParseQuery.getQuery(ScheduleController.class);
                query.whereEqualTo(ParseUser.getCurrentUser().getObjectId(), ScheduleController.KEY_USER);

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void fillHashMap(List<String> daysOfWeek) {

        for(int i = 0; i < daysOfWeek.size(); i++){
            intToDay.putIfAbsent(i, daysOfWeek.get(i));
            allAddedRecipes.putIfAbsent(i, new ArrayList<>());
        }
    }

    public void queryUserRecipes(){
        // specify what type of data we want to query - UserRecipe.class
        ParseQuery<ScheduleController> query = ParseQuery.getQuery(ScheduleController.class);

        // include data referred by user key
        query.include(ScheduleController.KEY_USER);
        query.include(ScheduleController.KEY_RECIPE);
        query.setLimit(20);
        query.whereEqualTo(ScheduleController.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ScheduleController>() {
            @Override
            public void done(List<ScheduleController> recipes, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue getting recipes", e);
                    return;
                }

                for (ScheduleController recipe: recipes){
                    System.out.println(recipe.getRecipe());
                    allAddedRecipes.putIfAbsent(recipe.getDayOfWeek(), new ArrayList<>());
                    allAddedRecipes.get(recipe.getDayOfWeek()).add((RecipeController) recipe.getRecipe());
                    dayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}