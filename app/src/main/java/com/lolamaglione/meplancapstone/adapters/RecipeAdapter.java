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
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.lolamaglione.meplancapstone.Constants;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.activities.RecipeDetailActivity;
import com.lolamaglione.meplancapstone.fragments.FeedFragment;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

/**
 * This is the recipe adapter where we can see each specific recipe item, we use this in the feed,
 * the suggested recipe, and the meal plan fragment. If you click on a specific recipe item you are
 * taken to the recipe detail activity.
 */
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

    public void addAll(List<Recipe> list) {
        recipes.addAll(list);
        notifyDataSetChanged();
    }

    public List<Recipe> getRecipe() {
        return recipes;
    }

    public void removeItem(int position) {
        recipes.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Recipe item, int position) {
        recipes.add(position, item);
        notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView ivFeedRecipe;
        private TextView tvCookTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivFeedRecipe = itemView.findViewById(R.id.ivFeedRecipe);
            tvCookTime = itemView.findViewById(R.id.tvCookTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Recipe recipe = recipes.get(position);
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
                        intent.putExtra(Constants.KEY_QUERY, ParseUser.getCurrentUser().getString(Constants.KEY_LAST_QUERY));
                        intent.putExtra(Constants.KEY_TITLE, recipe.title);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Recipe recipe){
            tvTitle.setText(recipe.getTitle());
            tvCookTime.setText(String.valueOf(recipe.getCookTime()));
            //Glide.with(context).load(recipe.getImageURL()).centerCrop().into(ivFeedRecipe);
            String imageURL = recipe.getImageURL();
            Glide.with(context).load(imageURL).centerCrop().
                    placeholder(R.drawable.recipe_placeholder_image).
                    error(R.drawable.recipe_placeholder_image).into(ivFeedRecipe);
        }
    }
}
