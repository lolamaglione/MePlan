package com.lolamaglione.meplancapstone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.databinding.ActivityRecipeDetailBinding;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.parceler.Parcels;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView tvLabel;
    ImageView ivRecipePicture;
    TextView tvDetailUrl;
    ListView lvIngredients;
    TextView tvTotalTimeCook;
    ActivityRecipeDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tvLabel = binding.tvLabelDetail;
        ivRecipePicture = binding.ivDetail;
        tvDetailUrl = binding.tvUrlDetail;
        lvIngredients = binding.lvIngredientList;

        Recipe recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, R.layout.ingredient_list_item, recipe.getIngredients());

        lvIngredients.setAdapter(itemsAdapter);
        tvLabel.setText(recipe.getTitle());
        tvDetailUrl.setText(recipe.getURL());

        Glide.with(this).load(recipe.getImageURL()).into(ivRecipePicture);
    }


}