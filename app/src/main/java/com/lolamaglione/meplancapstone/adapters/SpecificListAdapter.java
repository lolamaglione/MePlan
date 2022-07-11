package com.lolamaglione.meplancapstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lolamaglione.meplancapstone.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpecificListAdapter extends RecyclerView.Adapter<SpecificListAdapter.ListViewHolder>{

    private Context context;
    private HashMap<String, Integer> amountIngredientTitle;
    private List<String> ingredients;

    public SpecificListAdapter(Context context, HashMap<String, Integer> ingredients, List<String> ingredientList){
        this.context = context;
        this.amountIngredientTitle = ingredients;
        this.ingredients = ingredientList;
        //this.ingredients.addAll(ingredients.keySet());
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grocery_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ingredients.addAll(amountIngredientTitle.keySet());
        String ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }


    @Override
    public int getItemCount() {
        return amountIngredientTitle.keySet().size();
    }

    public void clear() {
        ingredients.clear();
        notifyDataSetChanged();
    }

    public void reshapeList(List<String> newIngredients) {
        ingredients.clear();
        ingredients.addAll(newIngredients);
        notifyDataSetChanged();
    }


    class ListViewHolder extends RecyclerView.ViewHolder{

        private TextView tvIngredient;
        private TextView tvAmount;
        private CheckBox checkbox;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredient = itemView.findViewById(R.id.tvIngredient);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            checkbox = itemView.findViewById(R.id.checkBox);


        }

        public void bind(String ingredient){
            tvIngredient.setText(ingredient);
            tvAmount.setText("" + amountIngredientTitle.get(ingredient));
        }
    }
}
