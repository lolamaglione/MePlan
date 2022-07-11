package com.lolamaglione.meplancapstone;

import com.lolamaglione.meplancapstone.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeSuggestions {

    static final int MAX_NUM = 128;

    public static class TrieNode {
        // change this into a hashmap
        TrieNode[] children = new TrieNode[MAX_NUM];
        char c;
        // if this is the last node it will have the title of the recipe
        List<String> ingredients;
        Ingredient ingredient;

        TrieNode(char ch) {
            this.c = ch;
            ingredients = new ArrayList<>();
            //children = new HashMap<>();
        }
    }

    public static class Trie {
        TrieNode root = new TrieNode(' ');
        // insert for Ingredient List
        public void insertIngredient(String ingredient, int amount, String measure) {
            TrieNode node = root;
            for (int i = 0; i < ingredient.length(); i++){
                char ch = ingredient.charAt(i);
                //String key = Character.toString(ch);
                TrieNode child = node.children[ch];
                if (child == null){
                    node.children[ch] = new TrieNode(ch);
                }
                node = node.children[ch];
            }
            node.ingredients.add(ingredient.substring(1));
            if (node.ingredient == null){
                node.ingredient = new Ingredient(ingredient, amount, measure);
            } else {
                node.ingredient.setAmount(node.ingredient.getAmount() + amount);
            }
        }

        public void removeIngredient(String ingredient){
            TrieNode node = root;
            for (int i = 0; i < ingredient.length(); i++){
                char ch = ingredient.charAt(i);
                TrieNode child = node.children[ch];
                node = node.children[ch];
            }
            node.ingredients.remove(ingredient.substring(1));
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

        public List<String> autocomplete(String ingredients) {
            TrieNode node = root;
            List<String> suggestions = new ArrayList<String>();

            for (int i = 0; i < ingredients.length(); i++) {
                char c = ingredients.charAt(i);
                node = node.children[c];
                if (node == null)
                    return new ArrayList<String>();
            }
            helper(node, suggestions);
            return suggestions;
        }

        //recursion function called by autocomplete
        //Time O(n), Space O(n), n is number of nodes in branches
        void helper(TrieNode node, List<String> res) {
            if (node == null) //base condition
                return;
            if (node.ingredients.size() != 0)
                res.addAll(node.ingredients);
            for (TrieNode child : node.children)
                helper(child, res);
        }


    }

}
