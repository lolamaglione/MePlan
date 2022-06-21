package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.activities.RecipeDetailActivity;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.parceler.Parcels;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Recipe recipe = recipes.get(position);
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Recipe recipe){
            tvUrl.setText(recipe.getURL());
            tvTitle.setText(recipe.getTitle());
            tvIngredients.setText(recipe.getIngredients().toString());
        }
    }
}
