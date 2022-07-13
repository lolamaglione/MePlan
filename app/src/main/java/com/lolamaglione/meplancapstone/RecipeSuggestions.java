package com.lolamaglione.meplancapstone;

import com.lolamaglione.meplancapstone.models.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeSuggestions {

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
        TrieNode root = new TrieNode(' ');
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

        public void removeIngredient(Ingredient ingredientObject, int position){
            String ingredient = "" + position + ingredientObject.getIngredientName().toLowerCase(Locale.ROOT);
            TrieNode node = root;
            for (int i = 0; i < ingredient.length(); i++){
                char ch = ingredient.charAt(i);
                TrieNode child = node.children[ch];
                node = node.children[ch];
            }
            node.ingredients.remove(ingredientObject);
        }

        //find the node with prefix's last char, then call helper to find all words using recursion
        //Time O(n), Space O(n), n is number of nodes included(prefix and branches)
//        List<String> autocomplete(List<String> ingredients) {
//            TrieNode node = root;
//            List<String> suggestions = new ArrayList<String>();
//
//            for (int i = 0; i < ingredients.size(); i++) {
//                String ingredient = ingredients.get(i);
//                node = node.children.get(ingredient);
//                if (node == null)
//                    return new ArrayList<String>();
//            }
//            helper(node, suggestions);
//            return suggestions;
//        }

        public boolean isNull(){
            TrieNode node = root;
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
        //Time O(n), Space O(n), n is number of nodes in branches
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
