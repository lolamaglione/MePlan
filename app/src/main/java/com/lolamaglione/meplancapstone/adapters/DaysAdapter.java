package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lolamaglione.meplancapstone.DoubleClickListener;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.SwipeToDeleteCallback;
import com.lolamaglione.meplancapstone.controllers.IngredientController;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.models.Ingredient;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.models.Schedule;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder>{

    private Context context;
    private List<String> daysOfWeek;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tvDay);
            rvRecipes = itemView.findViewById(R.id.rvDailyRecipes);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            rlExpandaleLayout = itemView.findViewById(R.id.rlExpandaleLayout);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            btnClear = itemView.findViewById(R.id.btnClear);
            // break down recipes for each day in order to send to each recipeAdapter
            // for the specific day recyclerView
        }

        public void bind(String dayOfWeek, int position, boolean isExpandable) {
            day.setText(dayOfWeek);
            List<Recipe> recipesInDB = new ArrayList<>();
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
                    dailyRecipes = addedRecipes.get(position);
                    List<Recipe> recipesInDB = new ArrayList<>();
                    if (dailyRecipes.size() > 0) {
                        for (RecipeController recipe : dailyRecipes) {
                            Recipe newRecipe = new Recipe();
                            newRecipe.setUrl(recipe.getUrl());
                            List<Ingredient> generalIngredients = null;
                            try {
                                generalIngredients = createIngredientObjects(recipe);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            newRecipe.setGeneralIngredients(generalIngredients);
                            newRecipe.setImageUrl(recipe.getImageURL());
                            newRecipe.setTitle(recipe.getTitle());
                            newRecipe.setSpecificIngredients(recipe.getSpecificIngredients());
                            newRecipe.setObjectID(recipe.getObjectId());
                            recipesInDB.add(newRecipe);
                        }
                    }
                    adapter = new RecipeAdapter(context, recipesInDB);
                    rvRecipes.setAdapter(adapter);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    rvRecipes.setLayoutManager(linearLayoutManager);
                    enableSwipeToDeleteAndUndo();
                }


            });

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

        private List<Ingredient> createIngredientObjects(RecipeController recipe) throws ParseException {
            List<Ingredient> listOfGeneralIng = new ArrayList<>();
            for (String ingredientID : recipe.getGeneralIngredients()){
                ParseQuery<IngredientController> query = ParseQuery.getQuery(IngredientController.class);
                query.whereEqualTo(IngredientController.KEY_OBJECT_ID, ingredientID);
                IngredientController ingredient = query.find().get(0);
                Ingredient newIngredient = new Ingredient();
                newIngredient.setMeasure(ingredient.getMeasure());
                newIngredient.setAmount(ingredient.getAmount());
                newIngredient.setTitle(ingredient.getName());
                listOfGeneralIng.add(newIngredient);
            }
            return  listOfGeneralIng;
        }

        private void enableSwipeToDeleteAndUndo() {
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
                                ParseQuery<ScheduleController> query = ParseQuery.getQuery(ScheduleController.class);
                                query.whereEqualTo(ScheduleController.KEY_RECIPE, object);
                                query.findInBackground(new FindCallback<ScheduleController>() {
                                    @Override
                                    public void done(List<ScheduleController> objects, ParseException e) {
                                        for (ScheduleController object : objects){
                                            scheduleUndo.setDayOfWeek(object.getDayOfWeek());
                                            scheduleUndo.setRecipe(object.getRecipe());
                                            scheduleUndo.setUser(object.getUser());
                                            object.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    adapter.removeItem(position);

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
                                            });

                                        }
                                    }
                                });
                            }
                        }
                    });

                    super.onSwiped(viewHolder, direction);
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchHelper.attachToRecyclerView(rvRecipes);
        }
    }
}
