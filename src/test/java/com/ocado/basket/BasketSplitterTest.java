package com.ocado.basket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {
    private BasketSplitter basketSplitter;

    @BeforeEach
    void setUp() {
        basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\test\\resources\\config.json");
    }

    @Test
    public void testSplitWithEmptyBasket() {
        List<String> emptyBasket = List.of();
        Map<String, List<String>> result = basketSplitter.split(emptyBasket);

        assertEquals(0, result.size());
    }

    @Test
    public void testSplitWithGivenBasket0() {
        basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\test\\resources\\config0.json");

        List<String> basket = Arrays.asList(
                "Steak (300g)",
                "Carrots (1kg)",
                "Cold Beer (330ml)",
                "AA Battery (4 Pcs.)",
                "Espresso Machine",
                "Garden Chair"
        );
        Map<String, List<String>> result = basketSplitter.split(basket);

        assertEquals(2, result.size());

        assertEquals(Arrays.asList("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)", "AA Battery (4 Pcs.)"), result.get("Express Delivery"));
        assertEquals(Arrays.asList("Espresso Machine", "Garden Chair"), result.get("Courier"));
    }

    @Test
    public void testSplitWithGivenBasket1() {
        basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\test\\resources\\config.json");

        List<String> basket = Arrays.asList(
                "Cocoa Butter",
                "Tart - Raisin And Pecan",
                "Table Cloth 54x72 White",
                "Flower - Daisies",
                "Fond - Chocolate",
                "Cookies - Englishbay Wht"
        );
        Map<String, List<String>> result = basketSplitter.split(basket);

        assertEquals(2, result.size());

        assertEquals(Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht"), result.get("Courier"));
        assertEquals(List.of("Fond - Chocolate"), result.get("Mailbox delivery"));
    }

    @Test
    public void testSplitWithGivenBasket2() {
        basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\test\\resources\\config.json");

        List<String> basket = Arrays.asList(
                "Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen",
                "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry", "Apples - Spartan",
                "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea", "Sauce - Mint", "Numi - Assorted Teas", "Garlic - Peeled",
                "Cake - Miini Cheesecake Cherry"
        );

        Map<String, List<String>> result = basketSplitter.split(basket);

        assertEquals(3, result.size());
        assertEquals(Arrays.asList(
                "Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen",
                "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry", "Apples - Spartan",
                "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea"
        ), result.get("Express Collection"));
        assertEquals(Arrays.asList("Sauce - Mint", "Numi - Assorted Teas", "Garlic - Peeled"), result.get("Same day delivery"));
        assertEquals(List.of("Cake - Miini Cheesecake Cherry"), result.get("Courier"));
    }

    @Test
    public void testSplitWithInvalidConfigFile() {
        assertThrows(RuntimeException.class, () -> new BasketSplitter("nonexistent_config.json"));
    }

    @Test
    public void testWithItemNotInConfigFile() {
        basketSplitter = new BasketSplitter("D:\\OCADO_EX\\src\\test\\resources\\config0.json");
        List<String> basket = Arrays.asList(
                "Steak (300g)",
                "Carrots (1kg)",
                "Soda (24x330ml)",
                "AA Battery (4 Pcs.)",
                "Espresso Machine",
                "Garden Chair"
        );
        assertThrows(RuntimeException.class, () -> basketSplitter.split(basket));
    }







}