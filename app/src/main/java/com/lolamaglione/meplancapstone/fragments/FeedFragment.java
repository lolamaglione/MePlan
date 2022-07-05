package com.lolamaglione.meplancapstone.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.lolamaglione.meplancapstone.EdamamClient;
import com.lolamaglione.meplancapstone.EndlessRecyclerViewScrollListener;
import com.lolamaglione.meplancapstone.ParseRecipe;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.RecipeAdapter;
import com.lolamaglione.meplancapstone.applications.ParseApplication;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.models.RecipeDao;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragment shows the recipe feed in the home button frgament
 * connects to recipe detail fragment and suggesting recipes with similar ingredients
 */
public class FeedFragment extends Fragment {

    private RecyclerView rvRecipes;
    private List<Recipe> allRecipes;
    private RecipeAdapter recipeAdapter;
    public static final String TAG = "Feed Fragment";
    private EdamamClient client;
    private String default_query = "chicken";
    private ParseRecipe parse;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String next_page = "";
    private String current_query = default_query;

    // implementing database
    private RecipeDao recipeDao;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parse = new ParseRecipe();
        rvRecipes = view.findViewById(R.id.rvRecipes);
        //searchBar = (Toolbar) view.findViewById(R.id.tbSearch);
        allRecipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getContext(), allRecipes);
        ParseUser.getCurrentUser().put("last_query", default_query);
        client = new EdamamClient();
        rvRecipes.setAdapter(recipeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvRecipes.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvRecipes.addOnScrollListener(scrollListener);

        // implementing database
        recipeDao = ((ParseApplication) getActivity().getApplicationContext()).getMyDatabase().recipeDao();

        //query for existing recipes in the DB:
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "fetching data from database");
                List<Recipe> recipesFromDB = recipeDao.recentItems(current_query);
                recipeAdapter.clear();
                recipeAdapter.addAll(recipesFromDB);
            }
        });

        populateRecipe(default_query, 0, "");
    }

    private void loadNextDataFromApi(int page) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        populateRecipe(current_query, page, next_page);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.setActionView(searchView);
        searchView.setSubmitButtonEnabled(true);
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.autofill_item, INGREDIENTS);
        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchString = parent.getItemAtPosition(position).toString();
                searchAutoComplete.setText("" + searchString);

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ParseUser.getCurrentUser().put("last_query", query);
                // perform query here
                populateRecipe(query, 0, "");
                current_query = query;
                INGREDIENTS = ingredientListKey.toArray(new String[ingredientListKey.size()]);
                adapter.notifyDataSetChanged();
                System.out.println("new ingredients: " + INGREDIENTS);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                //TODO: fidn a way to clear the search bar once the user has submitted
                searchView.clearFocus();
                searchView.clearAnimation();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private HashMap<String, Integer> ingredientList = new HashMap<>();

    //TODO: make this into a text file and make sure I can add to it.
    private String[] INGREDIENTS = new String[] {
            "chicken", "beef", "shrimp", "canola oil" , "extra-virgin olive oil" , "toasted sesame oil", "balsamic vinegar", "distilled white vinegar", "red wine vinegar", "rice vinegar",
            "ketchup", "mayonnaise", "dijon mustard", "soy sauce", "chili paste", "hot sauce", "worcestershire", "kosher salt", "salt", "black peppers", "bay leaves",
            "cayenne pepper", "crushed red pepper", "cumin", "ground coriander", "oregano", "paprika", "rosemary", "thyme leaves", "cinnamon", "cloves", "allspice", "ginger",
            "nutmeg", "chili powder", "curry powder", "italian seasoning", "vanilla extract", "black beans", "cannellini", "chickpeas", "kidney beans", "capers", "olives", "peanut butter",
            "jelly", "chicken stock", "chicken broth", "canned tomatoes", "tomatoes", "tomato paste", "salsa", "tuna", "salmon", "panko breadcrumbs", "breadcrumbs", "dried lentils",
            "pasta", "whole wheat pasta", "white rice", "rice", "whole wheat rice", "jasmine rice", "barley", "millet", "quinoa", "honey", "sugar", "roasted beef", "raisins", "apples"};

    private ArrayList<String> ingredientListKey = new ArrayList<>();
//    private String[] INGREDIENTS = null;



    private void populateRecipe(String query, int page, String nextPage) {
        client.getRecipeFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = null;
                try {
                    getNextPage(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    Log.i(TAG, json.jsonObject.getJSONArray("hits").toString());
                    if (page == 0){
                        recipeAdapter.clear();
                    }
                    final List<Recipe> recipesFromNetwork = parse.fromJsonArray(jsonArray, query);
                    allRecipes.addAll(recipesFromNetwork);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "saving data into the database");
                            recipeDao.insertModel(recipesFromNetwork.toArray(new Recipe[0]));
                        }
                    });
                    for (Recipe recipe : parse.fromJsonArray(jsonArray, query)){
                        List<String> recipeIngredients = recipe.getGeneralIngredients();
                        for (String ingredient : recipeIngredients){
                            if(!ingredientList.keySet().contains(ingredient)){
                                ingredientList.put(ingredient, 0);
                                ingredientListKey.add(ingredient);
                            }
                            ingredientList.put(ingredient, ingredientList.get(ingredient) + 1);
                        }
                    }
                    recipeAdapter.notifyDataSetChanged();
                    INGREDIENTS = ingredientListKey.toArray(new String[ingredientListKey.size()]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "error: " + throwable);
            }
        }, query, page, nextPage);
    }

    private void getNextPage(JsonHttpResponseHandler.JSON json) throws JSONException {
        String next = json.jsonObject.getJSONObject("_links").getJSONObject("next").getString("href");
        next_page = next;
        System.out.println("Happening: " + next_page);
    }


}