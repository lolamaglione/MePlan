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

public class SuggestRecipeAdapter extends RecyclerView.Adapter<SuggestRecipeAdapter.ViewHolder> {
    private Context context;
    private List<Recipe> suggestRecipes;

    public SuggestRecipeAdapter(Context context, List<Recipe> suggestRecipes){
        this.context = context;
        this.suggestRecipes = suggestRecipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggested_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = suggestRecipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return suggestRecipes.size();
    }

    public void clear() {
        suggestRecipes.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSugLabel;
        private TextView tvPercentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSugLabel = itemView.findViewById(R.id.tvSugLabel);
            tvPercentage = itemView.findViewById(R.id.tvSugPerc);
        }

        public void bind(Recipe recipe){
            tvSugLabel.setText(recipe.getTitle());
            tvPercentage.setText("" + recipe.getPercentageMatch());
        }
    }
}
