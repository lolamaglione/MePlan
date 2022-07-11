package com.lolamaglione.meplancapstone.models;

public class Ingredient {

    private String name;
    private int amount;
    private String measure;

    public Ingredient(String name, int amount, String measure){
        this.name = name;
        this.amount = amount;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setMeasure(String measure) {
        this.measure = measure;
    }

}
