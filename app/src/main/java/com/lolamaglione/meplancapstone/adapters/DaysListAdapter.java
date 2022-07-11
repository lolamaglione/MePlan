package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.RecipeSuggestions;
import com.lolamaglione.meplancapstone.controllers.IngredientController;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.fragments.AddToCalendarFragment;
import com.lolamaglione.meplancapstone.models.Ingredient;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DaysListAdapter extends RecyclerView.Adapter<DaysListAdapter.ViewHolder>{

    private Context context;
    private List<String> daysOfWeek;
    private HashMap<Integer, List<RecipeController>> addedRecipes;
    private HashMap<Integer, String> intToDay;
    private RecipeSuggestions.Trie trie;
    //private AddToCalendarFragment fragment = new AddToCalendarFragment();

    public DaysListAdapter(Context context, HashMap<Integer, String> intToDay, HashMap<Integer, List<RecipeController>> addedRecipes, RecipeSuggestions.Trie trie){
        this.context = context;
        this.intToDay = intToDay;
        this.addedRecipes = addedRecipes;
        this.trie = trie;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean isExpandable = false;
        String day = intToDay.get(position);
        holder.bind(day, position, isExpandable);
    }

    @Override
    public int getItemCount() {
        return addedRecipes.keySet().size();
    }

    public void clear() {
        addedRecipes.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView day;
        private RecyclerView rvRecipes;
        private LinearLayout linear_layout;
        private RelativeLayout rlExpandaleLayout;
        private ImageView ivArrow;

        SpecificListAdapter adapter;
        List<RecipeController> dailyRecipesList;
        HashMap<String, Integer> ingredientAmount;
        List<String> listIngredients;
        SearchView svIngredients;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tvDay_list);
            rvRecipes = itemView.findViewById(R.id.rvDailyRecipes_list);
            linear_layout = itemView.findViewById(R.id.linear_layout_list);
            rlExpandaleLayout = itemView.findViewById(R.id.rlExpandaleLayout_list);
            ivArrow = itemView.findViewById(R.id.ivArrow_list);
            svIngredients = itemView.findViewById((R.id.svIngredients));
            // break down recipes for each day in order to send to each recipeAdapter
            // for the specific day recyclerView
        }

        public void bind(String dayOfWeek, int position, boolean isExpandable) {
            day.setText(dayOfWeek);

            // get recipe for a specific day
            dailyRecipesList = addedRecipes.get(position);
            // recipes queried from the DataBase
            List<Recipe> recipesInDB = new ArrayList<>();
            // Ingredients from the dataBase
            ingredientAmount = new HashMap<>();
            listIngredients = new ArrayList<>();
            dailyRecipesList = addedRecipes.get(position);
            if (dailyRecipesList.size() > 0){
                makeRecipesInDB(recipesInDB);
                addToIngredientList(recipesInDB, trie, position);
                adapter = new SpecificListAdapter(context, ingredientAmount, listIngredients);
                rvRecipes.setAdapter(adapter);
            }
            rlExpandaleLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
            linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandLayoutAndImage();
                    if (dailyRecipesList.size() > 0) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        rvRecipes.setLayoutManager(linearLayoutManager);
                        svIngredients.setSubmitButtonEnabled(true);
                        svIngredients.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                List<String> autocomplete = trie.autocomplete("" + position + newText);
                                adapter.reshapeList(autocomplete);
                                return false;
                            }
                        });
                    }
                }
            });

        }

        // call when you cant to change visibility of the expandable layout
        private void expandLayoutAndImage() {
            if(rlExpandaleLayout.getVisibility() == View.VISIBLE){
                rlExpandaleLayout.setVisibility(View.GONE);
                ivArrow.setImageResource(R.drawable.arrow_down);
            } else{
                rlExpandaleLayout.setVisibility(View.VISIBLE);
                ivArrow.setImageResource(R.drawable.arrow_up);
            }
        }

        private void addToIngredientList(List<Recipe> recipesInDB, RecipeSuggestions.Trie trie, int position) {
            for (Recipe recipe : recipesInDB){
                for (String ingredient : recipe.getIngredientIDs()){
                    ParseQuery<IngredientController> query = ParseQuery.getQuery(IngredientController.class);
                    query.whereEqualTo(IngredientController.KEY_OBJECT_ID, ingredient);
                    query.findInBackground(new FindCallback<IngredientController>() {
                        @Override
                        public void done(List<IngredientController> objects, ParseException e) {
                            for (IngredientController ingredientController : objects){
                                ingredientAmount.putIfAbsent(ingredientController.getName(), 0);
                                listIngredients.add(ingredientController.getName());
                                ingredientAmount.put(ingredientController.getName(), ingredientAmount.get(ingredientController.getName()) + ingredientController.getAmount());
//                                if(ingredientAmount.get(ingredientController.getName()) == null){
//                                    continue;
//                                }
//                                ingredientAmount.add(ingredientController.getName());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    //trie.insertIngredient("" + position + ingredient);
                }
            }
        }

        private void makeRecipesInDB(List<Recipe> recipesInDB) {
            for (RecipeController recipe : dailyRecipesList) {
                Recipe newRecipe = new Recipe();
                newRecipe.setUrl(recipe.getUrl());
                newRecipe.setGeneralIngredients(recipe.getGeneralIngredients());
                newRecipe.setImageUrl(recipe.getImageURL());
                newRecipe.setTitle(recipe.getTitle());
                newRecipe.setIngredientIDs(recipe.getIngredientIDs());
                newRecipe.setSpecificIngredients(recipe.getSpecificIngredients());
                recipesInDB.add(newRecipe);
            }
        }
    }
}

