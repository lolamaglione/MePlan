package com.lolamaglione.meplancapstone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.facebook.stetho.common.ArrayListAccumulator;
import com.lolamaglione.meplancapstone.EdamamClient;
import com.lolamaglione.meplancapstone.EndlessRecyclerViewScrollListener;
import com.lolamaglione.meplancapstone.ParseRecipe;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.RecipeAdapter;
import com.lolamaglione.meplancapstone.adapters.SuggestRecipeAdapter;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
        queryRecipes(mQuery + ", " + mList_ing.get(1), 0, nextPage);
        queryRecipes(mQuery, 1, nextPage);
        queryRecipes(mQuery, 2, nextPage);

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
        List<Recipe> queriedRecipes = new ArrayList<>();
        client.getRecipeFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    getNextPage(json);
                    queriedRecipes.addAll(parse.fromJsonArray(jsonArray));
                    if(page == 0) {
                        adapter.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, query, page, nextPage);

        client.getRecipeFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    getNextPage(json);
                    queriedRecipes.addAll(parse.fromJsonArray(jsonArray));
                    if(page == 0) {
                        adapter.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, query, page, this.nextPage);

        client.getRecipeFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    getNextPage(json);
                    queriedRecipes.addAll(parse.fromJsonArray(jsonArray));
                    if(page == 0) {
                        adapter.clear();
                    }
                    fillPercentageMap(queriedRecipes);
                    if(percentageIngredients.keySet().size() != 0){
                        addToFinalRecipeList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, query, page, this.nextPage);


    }

    private void addToFinalRecipeList() {
        int position = 0;
        for (int key : percentageIngredients.keySet()){
            List<Recipe> recipeList = percentageIngredients.get(key);
            for (Recipe recipe : recipeList) {
                recipe.setPercentageMatch(key);
                if (!addedRecipesTitle.contains(recipe.getTitle())){
                    addedRecipesTitle.add(recipe.getTitle());
                    finalList.add(recipe);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void fillPercentageMap(List<Recipe> queriedRecipes) {

        for(Recipe recipe : queriedRecipes){
            List<String> ingredients = recipe.getGeneralIngredients();
            int match = compareIngredients(ingredients);
            percentageIngredients.putIfAbsent(match, new ArrayList<>());
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

    private void getNextPage(JsonHttpResponseHandler.JSON json) throws JSONException {
        String next = json.jsonObject.getJSONObject("_links").getJSONObject("next").getString("href");
        nextPage = next;
        System.out.println("Happening: " + nextPage);
    }

    // query all the recipes
    // check if the recipes have the same ingredients
        // compare the ingresient list of the original recipe to each the ingredient list of each recipe
        // figure out how many ingredients they have in common (give a percentage)
        // add this percentage to a map, Key == percentage, value == recipe.
}