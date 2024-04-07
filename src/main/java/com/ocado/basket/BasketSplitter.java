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
            throw new RuntimeException("Wrong config file");
        }
    }

    public Map<String, List<String>> split(List<String> items) {
        // parse input
        Map<String, List<String>> input = new LinkedHashMap<>();

        for (String item : items) {
            JSONArray catList = (JSONArray) config.get(item);
            if (catList == null) {
                throw new RuntimeException("The item is not in the config file");
            }
            for (Object cat : catList) {
                String category = (String) cat;
                input.computeIfAbsent(category, k -> new LinkedList<>()).add(item);
            }
        }

        // find solution - greedy approximation
        Map<String, List<String>> result = new LinkedHashMap<>();
        while (!input.isEmpty()) {
            // find category with the greatest number of uncovered elements
            String numerousCategory = input.keySet().stream()
                    .max(Comparator.comparingInt(category -> input.get(category).size()))
                    .orElse(null);

            List<String> products = input.remove(numerousCategory);

            // add category to result
            result.put(numerousCategory, products);

            // update input map
            input.forEach((category, categoryProducts) ->
                    categoryProducts.removeAll(products));

            // remove empty categories
            input.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        }

        return result;
    }

    public static void main(String[] args) throws IOException, ParseException {
        BasketSplitter basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\test\\resources\\config0.json");

        List<String> basket = Arrays.asList(
                "Steak (300g)",
                "Carrots (1kg)",
                "Cold Beer (330ml)",
                "AA Battery (4 Pcs.)",
                "Espresso Machine",
                "Garden Chair"
        );



        Map<String, List<String>> result = basketSplitter.split(basket);
        for (Object e : result.entrySet()) {
            System.out.println(e);
        }
    }
}
