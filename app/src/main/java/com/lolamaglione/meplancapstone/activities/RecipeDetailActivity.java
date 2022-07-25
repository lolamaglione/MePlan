package com.lolamaglione.meplancapstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.lolamaglione.meplancapstone.Constants;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.adapters.SampleFragmentPageAdapter;
import com.lolamaglione.meplancapstone.databinding.ActivityRecipeDetailBinding;
import com.lolamaglione.meplancapstone.fragments.FeedFragment;
import com.lolamaglione.meplancapstone.models.Recipe;
import org.parceler.Parcels;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.facebook.login.LoginManager;
import com.parse.ParseUser;

import java.util.Objects;

/**
 * RecipeDetailActivity gives you access to the currentRecipe you chose and adds a new tab
 * where you can see the suggested recipes based on the ingredients of the recipe you chose
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ActivityRecipeDetailBinding binding;
    private TabLayout tabLayout;
    private Recipe recipe;
    private ImageView ivRecipeDetail;
    private NestedScrollView nestedScroll;
    private Toolbar toolBar;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = binding.viewPager;
        tabLayout = binding.slidingTabs;
        nestedScroll = binding.nestedScroll;
        ivRecipeDetail = binding.ivRecipeDetail;
        toolBar = binding.toolbar;

        recipe = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        nestedScroll.setFillViewport(true);
        Glide.with(this).load(recipe.getImageURL()).into(ivRecipeDetail);
        String query = getIntent().getStringExtra(Constants.KEY_QUERY);
        viewPager.setAdapter(new SampleFragmentPageAdapter(getSupportFragmentManager(), RecipeDetailActivity.this, recipe, query, recipe.title));
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "feed");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}