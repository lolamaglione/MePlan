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
import com.lolamaglione.meplancapstone.EdamamClient;
import com.lolamaglione.meplancapstone.ParseRecipe;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.SuggestRecipeAdapter;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
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
    private RecyclerView rvRecipeSuggested;
    private SuggestRecipeAdapter adapter;
    private ParseRecipe parse;

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
        percentageIngredients = new TreeMap<>();
        finalList = new ArrayList<>();
        rvRecipeSuggested = view.findViewById(R.id.rvSuggestedRecipes);
        parse = new ParseRecipe();

        adapter = new SuggestRecipeAdapter(getContext(), finalList);
        rvRecipeSuggested.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvRecipeSuggested.setLayoutManager(linearLayoutManager);
        queryRecipes(mQuery + ", " + mList_ing.get(1), 0);

    }

    public void queryRecipes(String query, int page){
        List<Recipe> queriedRecipes = new ArrayList<>();
        client.getRecipeFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("hits");
                    queriedRecipes.addAll(parse.fromJsonArray(jsonArray));
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
        }, query, page, "");
    }

    private void addToFinalRecipeList() {
        int position = 0;
        for (int key : percentageIngredients.keySet()){
            List<Recipe> recipeList = percentageIngredients.get(key);
            for (Recipe recipe : recipeList) {
                recipe.setPercentageMatch(key);
                finalList.add(recipe);
                adapter.notifyDataSetChanged();;
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

    // query all the recipes
    // check if the recipes have the same ingredients
        // compare the ingresient list of the original recipe to each the ingredient list of each recipe
        // figure out how many ingredients they have in common (give a percentage)
        // add this percentage to a map, Key == percentage, value == recipe.
}