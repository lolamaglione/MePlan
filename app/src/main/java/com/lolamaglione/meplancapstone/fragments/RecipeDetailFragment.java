package com.lolamaglione.meplancapstone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.models.Recipe;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragments shows this details of the recipe (ingredients, total prep time, picture, and label)
 *
 * This fragment shows the detail of the recipe, the url, image and specific ingredient, it also
 * shows a webvview.
 */
public class RecipeDetailFragment extends Fragment {

    TextView tvLabel;
    TextView tvDetailUrl;
    ListView lvIngredients;
    ImageButton btnAddToSched;
    Toolbar toolBar;
    CollapsingToolbarLayout collapsingToolbar;

    private static final String RECIPE_OBJ = "recipe_obj";
    private FloatingActionButton btnShare;
    private Recipe mRecipe_obj;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailFragment newInstance(Parcelable recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_OBJ, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe_obj = (Recipe) Parcels.unwrap(getArguments().getParcelable(RECIPE_OBJ));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolBar = view.findViewById(R.id.toolbar);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        tvLabel = view.findViewById(R.id.tvLabelDetail);
        lvIngredients = view.findViewById(R.id.lvIngredientList);
        btnShare = view.findViewById(R.id.ibShareRecipe);
        btnAddToSched = view.findViewById(R.id.ibAddToSched);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getContext(), R.layout.ingredient_list_item, mRecipe_obj.getSpecificIngredients());

        lvIngredients.setAdapter(itemsAdapter);
        tvLabel.setText(mRecipe_obj.getTitle());
        btnAddToSched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                showAddScheduleDialog();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl(mRecipe_obj.getURL());
            }
        });

    }

    private void showAddScheduleDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddToCalendarFragment addToCal = new AddToCalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Recipe.class.getSimpleName(), Parcels.wrap(mRecipe_obj));
        addToCal.setArguments(bundle);
        addToCal.show(fm, "fragment_add_to_calendar");
    }

    private void shareUrl(String recipeUrl){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, recipeUrl);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}