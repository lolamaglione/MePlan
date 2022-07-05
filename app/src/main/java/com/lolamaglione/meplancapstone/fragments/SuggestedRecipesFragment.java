package com.lolamaglione.meplancapstone.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.lolamaglione.meplancapstone.EdamamClient;
import com.lolamaglione.meplancapstone.EndlessRecyclerViewScrollListener;
import com.lolamaglione.meplancapstone.ParseRecipe;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.RecipeAdapter;
import com.lolamaglione.meplancapstone.applications.EdamamApplication;
import com.lolamaglione.meplancapstone.applications.ParseApplication;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.models.RecipeDao;
import com.parse.Parse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuggestedRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragment suggest recipes based on the ingredients of the recipe you have already chosen
 */
public class SuggestedRecipesFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CURR_ING = "current_ingredients";
    private static final String QUERY = "query";
    private static final String TAG = "suggest recipe";
    private List<String> mList_ing;
    private String mQuery;
    private EdamamClient client;
    private SortedMap<Integer, List<Recipe>> percentageIngredients;
    private List<Recipe> finalList;
    private List<String> addedRecipesTitle;
    private RecyclerView rvRecipeSuggested;
    private RecipeAdapter adapter;
    private ParseRecipe parse;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String nextPage = "";
    private boolean dataBaseWasCalled = true;
    private RecipeDao recipeDao;

   // private static InMemoryCacheWithDelayQueue cache = new InMemoryCacheWithDelayQueue();


    public SuggestedRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SuggestedRecipesFragment.
     */
    public static SuggestedRecipesFragment newInstance(ArrayList<String> param1, String param2) {
        SuggestedRecipesFragment fragment = new SuggestedRecipesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CURR_ING, param1);
        args.putString(QUERY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mList_ing = getArguments().getStringArrayList(CURR_ING);
            mQuery = getArguments().getString(QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggested_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = new EdamamClient();
        percentageIngredients = new TreeMap<>(Collections.reverseOrder());
        finalList = new ArrayList<>();
        addedRecipesTitle = new ArrayList<>();
        rvRecipeSuggested = view.findViewById(R.id.rvSuggestedRecipes);
        parse = new ParseRecipe();
        adapter = new RecipeAdapter(getContext(), finalList);
        rvRecipeSuggested.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvRecipeSuggested.setLayoutManager(linearLayoutManager);

        // implementing database
        recipeDao = ((ParseApplication) getActivity().getApplicationContext()).getMyDatabase().recipeDao();

        //query for existing recipes in the DB:
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "fetching data from database");
                List<Recipe> recipesFromDB = recipeDao.sortedSuggestions(mQuery);
                if (recipesFromDB != null){
                    dataBaseWasCalled = true;
                } else{
                    dataBaseWasCalled = false;
                }
                adapter.clear();
                adapter.addAll(recipesFromDB);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvRecipeSuggested.addOnScrollListener(scrollListener);
        queryRecipes(mQuery, 0, nextPage);


    }

    private void loadNextDataFromApi(int page) {
        queryRecipes(mQuery, page, nextPage);
    }

//    public void queryRecipes(String query, int page, String nextPage){
//        List<Recipe> queriedRecipes = new ArrayList<>();
//        client.getRecipeFeed(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Headers headers, JSON json) {
//                JSONArray jsonArray = null;
//                try {
//                    jsonArray = json.jsonObject.getJSONArray("hits");
//                    if(page == 0) {
//                        adapter.clear();
//                    }
//                    getNextPage(json);
//                    queriedRecipes.addAll(parse.fromJsonArray(jsonArray));
//                    fillPercentageMap(queriedRecipes);
//                    if(percentageIngredients.keySet().size() != 0){
//                        addToFinalRecipeList();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//
//            }
//        }, query, page, nextPage);
//    }

    public void queryRecipes(String query, int page, String nextPage){
        ArrayList<String> next_page_array = new ArrayList<>();
        final List<Recipe> queriedRecipes = new ArrayList<>();
        next_page_array.add(nextPage);
        client.getRecipeFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "heppning");
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    String nextPage1 = getNextPage(json);
                    next_page_array.add(nextPage1);
                    final List<Recipe> recipesFromNetwork = parse.fromJsonArray(jsonArray, query);
                    queriedRecipes.addAll(recipesFromNetwork);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "saving data into the database");
                            recipeDao.insertModel(recipesFromNetwork.toArray(new Recipe[0]));
                        }
                    });
                    client.getRecipeFeed(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = json.jsonObject.getJSONArray("hits");
                                String nextPage1 = getNextPage(json);
                                next_page_array.add(nextPage1);
                                final List<Recipe> recipesFromNetwork = parse.fromJsonArray(jsonArray, query);
                                queriedRecipes.addAll(recipesFromNetwork);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i(TAG, "saving data into the database");
                                        recipeDao.insertModel(recipesFromNetwork.toArray(new Recipe[0]));
                                    }
                                });
                                client.getRecipeFeed(new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                                        JSONArray jsonArray = null;
                                        try {
                                            jsonArray = json.jsonObject.getJSONArray("hits");
                                            String next_page1 = getNextPage(json);
                                            next_page_array.add(next_page1);
                                            final List<Recipe> recipesFromNetwork = parse.fromJsonArray(jsonArray, query);
                                            queriedRecipes.addAll(recipesFromNetwork);
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.i(TAG, "saving data into the database");
                                                    recipeDao.insertModel(recipesFromNetwork.toArray(new Recipe[0]));
                                                }
                                            });
                                            fillPercentageMap(queriedRecipes);
                                            if(percentageIngredients.keySet().size() != 0){
                                                addToFinalRecipeList(query);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                                    }
                                }, query, page+2, next_page_array.get(2));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                        }
                    }, query, page+1, next_page_array.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"issue getting recipes");
            }
        }, query, page, next_page_array.get(0));


//
//
//

    }

    private List<Recipe> getRecipesQueried(int page){
        List<Recipe> retList = new ArrayList<>();
        client.getRecipeFeed(new JsonHttpResponseHandler() {
            JSONArray jsonArray = null;
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    getNextPage(json);
                    retList.addAll(parse.fromJsonArray(jsonArray, mQuery));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "issue getting recipes");
            }
        }, mQuery, page, this.nextPage);

        return retList;
    }

    private void addToFinalRecipeList(String query) {
        long hour = 60*60*1000;
        int position = 0;
        for (int key : percentageIngredients.keySet()){
            List<Recipe> recipeList = percentageIngredients.get(key);
            for (Recipe recipe : recipeList) {
                if (!addedRecipesTitle.contains(recipe.getTitle())){
                    addedRecipesTitle.add(recipe.getTitle());
                    finalList.add(recipe);
                   // cache.add(query, recipe, hour);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        int size = finalList.size();
        //cache.add(query, finalList, hour);
    }

    private void fillPercentageMap(List<Recipe> queriedRecipes) {

        for(Recipe recipe : queriedRecipes){
            List<String> ingredients = recipe.getGeneralIngredients();
            int match = compareIngredients(ingredients);
            percentageIngredients.putIfAbsent(match, new ArrayList<>());
            recipe.setPercentageMatch(match);
            percentageIngredients.get(match).add(recipe);
        }
    }

    private int compareIngredients(List<String> ingredients) {
        double match = 0;
        for (String ingredient : mList_ing){
            if (ingredients.contains(ingredient)){
                match++;
            }
        }
        return (int) (match/mList_ing.size()*100);
    }

    private String getNextPage(JsonHttpResponseHandler.JSON json) throws JSONException {
        String next = json.jsonObject.getJSONObject("_links").getJSONObject("next").getString("href");
        this.nextPage = next;
        System.out.println("Happening: " + nextPage);
        return next;
    }

    // query all the recipes
    // check if the recipes have the same ingredients
        // compare the ingresient list of the original recipe to each the ingredient list of each recipe
        // figure out how many ingredients they have in common (give a percentage)
        // add this percentage to a map, Key == percentage, value == recipe.
}