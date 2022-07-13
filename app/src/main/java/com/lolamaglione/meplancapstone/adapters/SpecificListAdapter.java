package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.controllers.IngredientController;
import com.lolamaglione.meplancapstone.models.Ingredient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SpecificListAdapter extends RecyclerView.Adapter<SpecificListAdapter.ListViewHolder>{

    private Context context;
    private List<Ingredient> ingredients;
    public static final String TAG = "Specfic List adapter";

    public SpecificListAdapter(Context context, List<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grocery_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void clear() {
        ingredients.clear();
        notifyDataSetChanged();
    }

    public void reshapeList(List<Ingredient> newIngredients) {
        ingredients.clear();
        ingredients.addAll(newIngredients);
        notifyDataSetChanged();
    }

    public void addAll(List<Ingredient> ingredientList) {
        ingredients.clear();
        ingredients.addAll(ingredientList);
        notifyDataSetChanged();
    }


    class ListViewHolder extends RecyclerView.ViewHolder{

        private TextView tvIngredient;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredient = itemView.findViewById(R.id.tvIngredient);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

        public void bind(Ingredient ingredient){
            tvIngredient.setText(ingredient.getIngredientName().toLowerCase(Locale.ROOT));
            if(ingredient.isChecked()){
                tvIngredient.setPaintFlags(tvIngredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            tvIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tvIngredient.getPaintFlags() != (tvIngredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG)){
                        tvIngredient.setPaintFlags(tvIngredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else if (tvIngredient.getPaintFlags() == (tvIngredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG)) {
                        tvIngredient.setPaintFlags(0);
                    }
                    ParseQuery<IngredientController> query = ParseQuery.getQuery(IngredientController.class);
                    query.whereEqualTo(IngredientController.KEY_OBJECT_ID, ingredient.getIngredientID());
                    query.findInBackground(new FindCallback<IngredientController>() {
                        @Override
                        public void done(List<IngredientController> objects, ParseException e) {
                            for (IngredientController object : objects){
                                object.setIsChecked(!object.getIsChecked());
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if ( e != null){
                                            Log.e(TAG, "error saving checked");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });


        }
    }
}
