package com.lolamaglione.meplancapstone;

import java.io.CharConversionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeSuggestions {

    static final int MAX_NUM = 128;

    public static class TrieNode {
        // change this into a hashmap
        TrieNode[] children = new TrieNode[MAX_NUM];
        //Map<Character, TrieNode> children;
        char c;
        // if this is the last node it will have the title of the recipe
        List<String> recipes;

        TrieNode(char ch) {
            this.c = ch;
            recipes = new ArrayList<>();
            //children = new HashMap<>();
        }
    }

    public static class Trie {
        TrieNode root = new TrieNode(' ');

        //Inserts a phone number into the trie, Iteration
        //Time O(s), Space O(s), s is word length
//        void insert(HashMap<String, List<String>> recipeIngredient) {
//
//            for (String key : recipeIngredient.keySet()){
//                TrieNode node = root;
//                List<String> ingredients = recipeIngredient.get(key);
//                for (int i = 0; i < ingredients.size(); i++) {
//                    String ingredient = ingredients.get(i);
//                    TrieNode child = node.children.get(ingredient);
//                    if (child == null)
//                        child = new TrieNode(ingredient);
//                    node.children.put(ingredient, child);
//                    node = node.children.get(ingredient);
//                }
//                node.recipes.add(key);
//            }
//
//        }
        // insert for Ingredient List
        public void insertIngredient(String ingredient) {
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
            node.recipes.add(ingredient.substring(1));
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
            if (node.recipes.size() != 0)
                res.addAll(node.recipes);
            for (TrieNode child : node.children)
                helper(child, res);
        }


    }

}
