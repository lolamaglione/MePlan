package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lolamaglione.meplancapstone.fragments.RecipeDetailFragment;
import com.lolamaglione.meplancapstone.fragments.SuggestedRecipesFragment;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.parceler.Parcels;

import java.util.ArrayList;

public class SampleFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Recipe", "Suggested Recipes"};
    private Context context;
    private Recipe recipe;
    private String query;

    public SampleFragmentPageAdapter(@NonNull FragmentManager fm, Context context, Recipe recipe, String query) {
        super(fm);
        this.context = context;
        this.recipe = recipe;
        this.query = query;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            System.out.println(recipe.getGeneralIngredients());
            return RecipeDetailFragment.newInstance(Parcels.wrap(recipe));
        } else if (position == 1){
            return SuggestedRecipesFragment.newInstance((ArrayList<String>) recipe.getGeneralIngredients(), query);
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
