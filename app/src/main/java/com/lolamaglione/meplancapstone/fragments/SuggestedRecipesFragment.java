package com.lolamaglione.meplancapstone.fragments;

import android.os.AsyncTask;
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

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.lolamaglione.meplancapstone.Constants;
import com.lolamaglione.meplancapstone.EdamamClient;
import com.lolamaglione.meplancapstone.EndlessRecyclerViewScrollListener;
import com.lolamaglione.meplancapstone.ParseRecipe;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.RecipeAdapter;
import com.lolamaglione.meplancapstone.applications.ParseApplication;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.models.RecipeDao;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuggestedRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 *
 * This fragment suggest recipes based on the ingredients of the recipe you have already chosen
 */
public class SuggestedRecipesFragment extends Fragment {

    private static final String CURR_ING = "current_ingredients";
    private static final String QUERY = "query";
    private static final String TITLE = "title";
    private static final String TAG = "suggest recipe";
    private List<String> mList_ing;
    private String mQuery;
    private String mTitle;
    private EdamamClient client;
    private SortedMap<Integer, List<Recipe>> percentageIngredients;
    private List<Recipe> finalList;
    private List<String> addedRecipesTitle;
    private RecyclerView rvRecipeSuggested;
    private RecipeAdapter adapter;
    private ParseRecipe parse;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String nextPage = "";
    private RecipeDao recipeDao;
    ParseApplication context;
    private static final int numberOfPages = 5;

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
    public static SuggestedRecipesFragment newInstance(ArrayList<String> param1, String param2, String param3) {
        SuggestedRecipesFragment fragment = new SuggestedRecipesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CURR_ING, param1);
        args.putString(QUERY, param2);
        args.putString(TITLE, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mList_ing = getArguments().getStringArrayList(CURR_ING);
            mQuery = getArguments().getString(QUERY);
            mTitle = getArguments().getString(TITLE);
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

        context = ((ParseApplication) getActivity().getApplicationContext());
        //query for existing recipes in the DB:
        try {
            queryRecipes(linearLayoutManager);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    // create cache in the context of the application so that it doesn't get destroyed every time
    private void queryRecipes(LinearLayoutManager linearLayoutManager) throws ExecutionException {

        // look up code in in memory cache
        Log.i(TAG, "fetching from cache");
        percentageIngredients = context.getGuavaCache().get(mTitle, () -> queryRecipesFromDBorAPI(linearLayoutManager));
        if(percentageIngredients.keySet().size() != 0){
            adapter.clear();
            addToFinalRecipeList();
            adapter.addAll(finalList);
        }
    }

    // get the recipes from the database
    private SortedMap<Integer, List<Recipe>> queryRecipesFromDBorAPI(LinearLayoutManager linearLayoutManager) {
        List<Recipe> recipesFromDB = recipeDao.sortedSuggestions(mQuery);
        if (recipesFromDB.size() != 0 || recipesFromDB != null){
            Log.i(TAG, "fetching data from database");
            fillPercentageMap(recipesFromDB);
            return percentageIngredients;
        } else {
            queryRecipesFromAPI(mQuery, 0, nextPage);
            Log.i(TAG, "fetching from API");
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
        }
        return null;
    }

    // get more pages from the API
    private void loadNextDataFromApi(int page) {
        queryRecipesFromAPI(mQuery, page, nextPage);
    }

    // query x amount of pages from the API
    public void queryRecipesFromAPI(String query, int page, String nextPage){
        final List<Recipe> queriedRecipes = new ArrayList<>();
        final String[] nextPageFinal = {""};
        for(int i = 0; i < numberOfPages; i++){
            client.getRecipeFeed(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = json.jsonObject.getJSONArray("hits");
                        nextPageFinal[0] = getNextPage(json);
                        final List<Recipe> recipesFromNetwork = parse.fromJsonArray(jsonArray, query);
                        queriedRecipes.addAll(recipesFromNetwork);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "saving data into the database");
                                recipeDao.insertModel(queriedRecipes.toArray(new Recipe[0]));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG,"issue getting recipes");
                }
            }, query, page+(i+1), nextPageFinal[0]);
        }
        fillPercentageMap(queriedRecipes);
        if(percentageIngredients.keySet().size() != 0){
            addToFinalRecipeList();
        }
    }

    // add to the adapter list sorted
    private void addToFinalRecipeList() {
        addedRecipesTitle.add(mTitle);
        for (int key : percentageIngredients.keySet()){
            List<Recipe> recipeList = percentageIngredients.get(key);
            for (Recipe recipe : recipeList) {
                if (!addedRecipesTitle.contains(recipe.getTitle())){
                    addedRecipesTitle.add(recipe.getTitle());
                    finalList.add(recipe);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        adapter.addAll(finalList);
    }

    // set the percentage match and added to the hashmap
    private void fillPercentageMap(List<Recipe> queriedRecipes) {
        percentageIngredients = new TreeMap<>(Collections.reverseOrder());
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
        int weight = mList_ing.size();
        for (String ingredient : mList_ing){
            if (ingredients.contains(ingredient)){
                match = match + weight;
            }
            weight--;
        }
        return (int) (match/mList_ing.size()*100);
    }

    private String getNextPage(JsonHttpResponseHandler.JSON json) throws JSONException {
        String next = json.jsonObject.getJSONObject(Constants.KEY_LINKS).getJSONObject(Constants.KEY_NEXT_PAGE).getString(Constants.KEY_HREF);
        this.nextPage = next;
        return next;
    }
}