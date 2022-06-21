package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.models.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Recipe recipe = recipes.get(position);
        holder.bind(recipe);

    }

    @Override
    public int getItemCount() {

        return recipes.size();
    }

    public void clear() {
        recipes.clear();
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvUrl;
        private TextView tvIngredients;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            tvIngredients = itemView.findViewById(R.id.tvIngredientList);
        }

        public void bind(Recipe recipe){
            tvUrl.setText(recipe.getURL());
            tvTitle.setText(recipe.getTitle());
            tvIngredients.setText(recipe.getIngredients().toString());
        }
    }
}
