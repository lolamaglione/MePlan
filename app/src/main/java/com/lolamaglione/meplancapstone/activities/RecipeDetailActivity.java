package com.lolamaglione.meplancapstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.SampleFragmentPageAdapter;
import com.lolamaglione.meplancapstone.databinding.ActivityRecipeDetailBinding;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.parceler.Parcels;

/**
 * RecipeDetailActivity gives you access to the currentRecipe you chose and adds a new tab
 * where you can see the suggested recipes based on the ingredients of the recipe you chose
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ActivityRecipeDetailBinding binding;
    private TabLayout tabLayout;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewPager = binding.viewPager;
        tabLayout = binding.slidingTabs;

        recipe = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        String query = getIntent().getStringExtra("query");
        viewPager.setAdapter(new SampleFragmentPageAdapter(getSupportFragmentManager(), RecipeDetailActivity.this, recipe, query));
        tabLayout.setupWithViewPager(viewPager);

    }


}