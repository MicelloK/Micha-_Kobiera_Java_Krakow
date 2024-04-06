package com.ocado.basket;

import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class BasketSplitter {
    private final JSONObject config;

    public BasketSplitter(String absolutePathToConfigFile) {
        JSONParser jsonParser = new JSONParser();
        try {
            Reader reader = new FileReader(absolutePathToConfigFile);
            config = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<String>> split(List<String> items) {
        // parse input
        Map<String, List<String>> input = new LinkedHashMap<>();
        for (String item : items) {
            JSONArray catList = (JSONArray) config.get(item);
            for (Object cat : catList) {
                String category = (String) cat;
                if (!input.containsKey(category)) {
                    List<String> list = new LinkedList<>();
                    list.add(item);

                    input.put(category, list);
                }
                else {
                    input.get(category).add(item);
                }
            }
        }

        // find solution - greedy approximation
        Map<String, List<String>> result = new LinkedHashMap<>();
        while (!input.isEmpty()) {
            // find category with number of uncovered elements
            String numerousCategory = "";
            int numOfElements = 0;
            for (String category : input.keySet()) {
                if (input.get(category).size() > numOfElements) {
                    numerousCategory = category;
                    numOfElements = input.get(category).size();
                }
            }
            List<String> products = input.get(numerousCategory);

            // add category to result
            result.put(numerousCategory, products);

            // update input map
            input.remove(numerousCategory);
            for (String product : products) {
//                for (String category : input.keySet()) {
//                    List<String> categoryProducts = input.get(category);
//                    categoryProducts.remove(product);
//                    if (categoryProducts.isEmpty()) {
//                        input.remove(category);
//                    }
//                }
                Iterator<String> categoryIterator = input.keySet().iterator();
                while (categoryIterator.hasNext()) {
                    String category = categoryIterator.next();
                    List<String> categoryProducts = input.get(category);
                    categoryProducts.remove(product);
                    if (categoryProducts.isEmpty()) {
                        categoryIterator.remove();
                    }
                }
            }

        }

        return result;
    }

    public static void main(String[] args) throws IOException, ParseException {
        BasketSplitter basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\main\\resources\\config.json");
        List<String> basket = new LinkedList<>();

        basket.add("Cocoa Butter");
        basket.add("Tart - Raisin And Pecan");
        basket.add("Table Cloth 54x72 White");
        basket.add("Flower - Daisies");
        basket.add("Fond - Chocolate");
        basket.add("Cookies - Englishbay Wht");

        Map<String, List<String>> result = basketSplitter.split(basket);
        for (Object e : result.entrySet()) {
            System.out.println(e);
        }


    }
}
