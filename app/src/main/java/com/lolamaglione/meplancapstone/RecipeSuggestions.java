package com.lolamaglione.meplancapstone;

import com.lolamaglione.meplancapstone.models.Ingredient;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is the trie class that creates an autocomplete algorithm using the trie data structure for
 * the ingredient list
 */
public class RecipeSuggestions {

    public RecipeSuggestions() {

    }

    static final int MAX_NUM = 128;


    public static class TrieNode {
        // change this into a hashmap
        TrieNode[] children = new TrieNode[MAX_NUM];
        char c;
        // if this is the last node it will have the title of the recipe
        List<Ingredient> ingredients;

        TrieNode(char ch) {
            this.c = ch;
            ingredients = new ArrayList<>();
            //children = new HashMap<>();
        }
    }

    public static class Trie {
        public TrieNode root = new TrieNode(' ');
        // insert for Ingredient List
        public void insertIngredient(Ingredient ingredientObject, int position) {
            String ingredient = "" + position + ingredientObject.getIngredientName().toLowerCase(Locale.ROOT);
            TrieNode node = root;
            for (int i = 0; i < ingredient.length(); i++){
                char ch = ingredient.charAt(i);
                TrieNode child = node.children[ch];
                if (child == null){
                    node.children[ch] = new TrieNode(ch);
                }
                node = node.children[ch];
            }
            node.ingredients.add(ingredientObject);
        }

        public TrieNode removeIngredient(TrieNode nodeAt, String ingredient, int characterAt){
            // if trie is empty
            if (nodeAt == null){
                return null;
            }

            //if we are in the last node:
            if(characterAt == ingredient.length()){
                //remove the ingredient
                nodeAt.ingredients.remove(nodeAt.ingredients.size()-1);
                if(nodeAt.ingredients.size() == 0 && isNull(nodeAt)){
                    nodeAt = null;
                }

                return nodeAt;
            }

            int charAt = ingredient.charAt(characterAt);
            nodeAt.children[charAt] = removeIngredient(nodeAt.children[charAt], ingredient, characterAt +1 );

            if(isNull(nodeAt) && nodeAt.ingredients.size() == 0){
                nodeAt = null;
            }

            return nodeAt;
        }

        public boolean isNull(TrieNode node){
            boolean isNull = true;
            for (int i = 0; i < node.children.length; i++){
                if(node.children[i] != null){
                    isNull = false;
                }
            }
            return isNull;
        }

        public List<Ingredient> autocomplete(String ingredients) {
            TrieNode node = root;
            List<Ingredient> suggestions = new ArrayList<Ingredient>();
            for (int i = 0; i < ingredients.length(); i++) {
                char c = ingredients.charAt(i);
                node = node.children[c];
                if (node == null)
                    return new ArrayList<Ingredient>();
            }
            helper(node, suggestions);
            return suggestions;
        }

        //recursion function called by autocomplete
        void helper(TrieNode node, List<Ingredient> res) {
            if (node == null) //base condition
                return;
            if (node.ingredients.size() != 0)
                res.addAll(node.ingredients);
            for (TrieNode child : node.children)
                helper(child, res);
        }
    }

}
