package com.lolamaglione.meplancapstone;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Organization {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    Long id;

    @ColumnInfo
    String title;
    @ColumnInfo
    String imageURl;
    @ColumnInfo
    String url;
    @ColumnInfo
    List<String> specificIngredients;
    @ColumnInfo
    List<String> generalIngredients;




}
