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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.models.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder>{

    private Context context;
    private List<String> daysOfWeek;
    private HashMap<Integer, List<RecipeController>> addedRecipes;
    private HashMap<Integer, String> intToDay;

    public DaysAdapter(Context context, HashMap<Integer, String> intToDay, HashMap<Integer, List<RecipeController>> addedRecipes){
        this.context = context;
        this.intToDay = intToDay;
        this.addedRecipes = addedRecipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_item, parent, false);
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

        RecipeAdapter adapter;
        List<RecipeController> dailyRecipes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tvDay);
            rvRecipes = itemView.findViewById(R.id.rvDailyRecipes);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            rlExpandaleLayout = itemView.findViewById(R.id.rlExpandaleLayout);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            // break down recipes for each day in order to send to each recipeAdapter
            // for the specific day recyclerView
        }

        public void bind(String dayOfWeek, int position, boolean isExpandable) {
            day.setText(dayOfWeek);

            dailyRecipes = addedRecipes.get(position);
            List<Recipe> recipesInDB = new ArrayList<>();
            if (dailyRecipes.size() > 0) {
                for (RecipeController recipe : dailyRecipes) {
                    Recipe newRecipe = new Recipe();
                    newRecipe.setUrl(recipe.getUrl());
                    newRecipe.setGeneralIngredients(recipe.getGeneralIngredients());
                    newRecipe.setImageUrl(recipe.getImageURL());
                    newRecipe.setTitle(recipe.getTitle());
                    newRecipe.setSpecificIngredients(recipe.getSpecificIngredients());
                    recipesInDB.add(newRecipe);
                }
                adapter = new RecipeAdapter(context, recipesInDB);
                rvRecipes.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                rvRecipes.setLayoutManager(linearLayoutManager);
            }
            rlExpandaleLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
            linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rlExpandaleLayout.getVisibility() == View.VISIBLE){
                        rlExpandaleLayout.setVisibility(View.GONE);
                    } else{
                        rlExpandaleLayout.setVisibility(View.VISIBLE);
                    }
                    dailyRecipes = addedRecipes.get(position);
                    List<Recipe> recipesInDB = new ArrayList<>();
                    if (dailyRecipes.size() > 0) {
                        for (RecipeController recipe : dailyRecipes) {
                            Recipe newRecipe = new Recipe();
                            newRecipe.setUrl(recipe.getUrl());
                            newRecipe.setGeneralIngredients(recipe.getGeneralIngredients());
                            newRecipe.setImageUrl(recipe.getImageURL());
                            newRecipe.setTitle(recipe.getTitle());
                            newRecipe.setSpecificIngredients(recipe.getSpecificIngredients());
                            recipesInDB.add(newRecipe);
                        }
                        adapter = new RecipeAdapter(context, recipesInDB);
                        rvRecipes.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        rvRecipes.setLayoutManager(linearLayoutManager);
                    }
                }
            });
        }
    }
}
