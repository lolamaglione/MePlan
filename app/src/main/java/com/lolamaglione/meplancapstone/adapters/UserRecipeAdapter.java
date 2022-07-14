package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.activities.RecipeDetailActivity;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

/**
 * DEMO
 */
public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.ViewHolder>{

    private Context context;
    private List<RecipeController> recipeControllers;

    public UserRecipeAdapter(Context context, List<RecipeController> recipes){
        this.context = context;
        this.recipeControllers = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RecipeController recipe = recipeControllers.get(position);
        holder.bind(recipe);

    }

    @Override
    public int getItemCount() {
        return recipeControllers.size();
    }

    public void clear() {
        recipeControllers.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView ivFeedRecipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivFeedRecipe = itemView.findViewById(R.id.ivFeedRecipe);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        RecipeController recipe = recipeControllers.get(position);
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
                        intent.putExtra("query", ParseUser.getCurrentUser().getString("last_query"));
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(RecipeController recipe){
            tvTitle.setText(recipe.getTitle());
            Glide.with(context).load(recipe.getImageURL()).into(ivFeedRecipe);
        }

    }
}
