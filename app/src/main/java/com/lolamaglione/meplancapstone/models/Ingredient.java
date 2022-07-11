package com.lolamaglione.meplancapstone.models;


import org.parceler.Parcel;

@Parcel
public class Ingredient {

    String ingredientTitle;
    int amount;
    String measure;

    public Ingredient(){
        
    }

    public String getIngredientTitle() {
        return ingredientTitle;
    }

    public void setIngredientTitle(String ingredientTitle) {
        this.ingredientTitle = ingredientTitle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMeasure() {
        return measure;
    }

    public void setRecipeID(String measure) {
        this.measure = measure;
    }

}
