package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lolamaglione.meplancapstone.fragments.RecipeDetailFragment;
import com.lolamaglione.meplancapstone.fragments.RecipeWebFragment;
import com.lolamaglione.meplancapstone.fragments.SuggestedRecipesFragment;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * This is used in the recipe detail activity for the tab where one is the recipe and the other
 * is the suggested recipes tab
 */
public class SampleFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"Recipe", "Recipe Website", "Suggested Recipes"};
    private Context context;
    private Recipe recipe;
    private String query;
    private String title;

    public SampleFragmentPageAdapter(@NonNull FragmentManager fm, Context context, Recipe recipe, String query, String title) {
        super(fm);
        this.context = context;
        this.recipe = recipe;
        this.query = query;
        this.title = title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return RecipeDetailFragment.newInstance(Parcels.wrap(recipe));
        } else if (position == 1){
            return RecipeWebFragment.newInstance(recipe.getURL());
        } else if (position == 2){
            return SuggestedRecipesFragment.newInstance((ArrayList<String>) recipe.getGeneralIngredients(), query, title);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
