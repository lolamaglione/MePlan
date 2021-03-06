package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.SwipeToDeleteCallback;
import com.lolamaglione.meplancapstone.activities.MainActivity;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.fragments.GroceryListFragment;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the adapter that allows us to see the meal plan recipe for each day in the meal plan fragment
 * connects to the recipe adapter so we can see each recipe specifically
 */
public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder>{

    private Context context;
    private HashMap<Integer, List<RecipeController>> addedRecipes;
    private HashMap<Integer, String> intToDay;

    public DaysAdapter(Context context, HashMap<Integer, String> intToDay, HashMap<Integer, List<RecipeController>> addedRecipes){
        this.context = context;
        this.intToDay = intToDay;
        this.addedRecipes = addedRecipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean isExpandable = false;
        String day = intToDay.get(position);
        holder.bind(day, position, isExpandable);
    }

    @Override
    public int getItemCount() {
        return addedRecipes.keySet().size();
    }

    public void clear() {
        addedRecipes.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView day;
        private RecyclerView rvRecipes;
        private LinearLayout linear_layout;
        private RelativeLayout rlExpandaleLayout;
        private ImageView ivArrow;
        private Button btnClear;

        RecipeAdapter adapter;
        List<RecipeController> dailyRecipes;

        // break down recipes for each day in order to send to each recipeAdapter
        // for the specific day recyclerView
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tvDay);
            rvRecipes = itemView.findViewById(R.id.rvDailyRecipes);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            rlExpandaleLayout = itemView.findViewById(R.id.rlExpandaleLayout);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            btnClear = itemView.findViewById(R.id.btnClear);

        }

        public void bind(String dayOfWeek, int position, boolean isExpandable) {
            day.setText(dayOfWeek);
            List<Recipe> recipesInDB = new ArrayList<>();
            adapter = new RecipeAdapter(context, recipesInDB);
            rvRecipes.setAdapter(adapter);
            enableSwipeToDeleteAndUndo(recipesInDB);
            dailyRecipes = addedRecipes.get(position);
            if (dailyRecipes.size() > 0){
                ivArrow.setImageResource(R.drawable.arrow_down);
                createRecipesInDB(recipesInDB);
                rlExpandaleLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
                linear_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rlExpandaleLayout.getVisibility() == View.VISIBLE){
                            rlExpandaleLayout.setVisibility(View.GONE);
                            ivArrow.setImageResource(R.drawable.arrow_down);
                        } else{
                            rlExpandaleLayout.setVisibility(View.VISIBLE);
                            ivArrow.setImageResource(R.drawable.arrow_up);
                        }
                    }
                });
            }else{
                ivArrow.setImageResource(R.drawable.ic_not_added);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rvRecipes.setLayoutManager(linearLayoutManager);

            deleteAllRecipesFromDay(position, recipesInDB);
        }

        private void deleteAllRecipesFromDay(int position, List<Recipe> recipesInDB) {
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseQuery<ScheduleController> query = ParseQuery.getQuery(ScheduleController.class);
                    query.whereEqualTo(ScheduleController.KEY_USER, ParseUser.getCurrentUser());
                    query.whereEqualTo(ScheduleController.KEY_DAY, position);

                    query.findInBackground(new FindCallback<ScheduleController>() {
                        @Override
                        public void done(List<ScheduleController> objects, ParseException e) {
                            for (ScheduleController object : objects){
                                object.deleteInBackground();
                            }
                            recipesInDB.removeAll(objects);
                            adapter.clear();
                        }
                    });
                    rlExpandaleLayout.setVisibility(View.GONE);
                    ivArrow.setImageResource(R.drawable.arrow_down);
                }
            });
        }

        private void createRecipesInDB(List<Recipe> recipesInDB) {
            for (RecipeController recipe : dailyRecipes) {
                Recipe newRecipe = new Recipe();
                newRecipe.setUrl(recipe.getUrl());
                newRecipe.setGeneralIngredients(recipe.getGeneralIngredients());
                newRecipe.setImageUrl(recipe.getImageURL());
                newRecipe.setTitle(recipe.getTitle());
                newRecipe.setSpecificIngredients(recipe.getSpecificIngredients());
                newRecipe.setObjectID(recipe.getObjectId());
                recipesInDB.add(newRecipe);
            }
        }

        private void enableSwipeToDeleteAndUndo(List<Recipe> recipes) {
            SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAbsoluteAdapterPosition();
                    final Recipe item = adapter.getRecipe().get(position);
                    final ScheduleController scheduleUndo = new ScheduleController();
                    ParseQuery<RecipeController> queryRecipe = ParseQuery.getQuery(RecipeController.class);
                    queryRecipe.whereEqualTo(RecipeController.KEY_OBJECT_ID, item.getObjectID());
                    queryRecipe.findInBackground(new FindCallback<RecipeController>() {
                        @Override
                        public void done(List<RecipeController> objects, ParseException e) {
                            for (RecipeController object : objects){
                                List<String> generalIngredients = object.getGeneralIngredients();
                                GroceryListFragment.removeTrie(generalIngredients, position);
                                ParseQuery<ScheduleController> query = ParseQuery.getQuery(ScheduleController.class);
                                query.whereEqualTo(ScheduleController.KEY_RECIPE, object);
                                try {
                                    List<ScheduleController> foundScheduleController = query.find();
                                    for (ScheduleController schedule : foundScheduleController){
                                        scheduleUndo.setDayOfWeek(schedule.getDayOfWeek());
                                        scheduleUndo.setRecipe(schedule.getRecipe());
                                        scheduleUndo.setUser(schedule.getUser());
                                        schedule.delete();
                                        adapter.removeItem(position);
                                        undoRemovingRecipe(item, position, scheduleUndo);
                                    }
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                    super.onSwiped(viewHolder, direction);
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchHelper.attachToRecyclerView(rvRecipes);
        }

        private void undoRemovingRecipe(Recipe item, int position, ScheduleController scheduleUndo) {
            Snackbar snackbar = Snackbar.make(itemView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    adapter.restoreItem(item, position);
                    scheduleUndo.saveInBackground();
                    rvRecipes.scrollToPosition(position);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
