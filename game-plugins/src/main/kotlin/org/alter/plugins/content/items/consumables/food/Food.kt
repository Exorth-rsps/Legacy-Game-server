package org.alter.plugins.content.items.food

import org.alter.api.cfg.Items


enum class Food(val item: Int, val heal: Int = 0, val overheal: Boolean = false,
                val replacement: Int = -1, val tickDelay: Int = 3,
                val comboFood: Boolean = false) {

    /**
     * Sea food.
     */
    SHRIMP(item = Items.SHRIMPS, heal = 3),
    SARDINE(item = Items.SARDINE, heal = 4),
    HERRING(item = Items.HERRING, heal = 5),
    MACKEREL(item = Items.MACKEREL, heal = 6),
    TROUT(item = Items.TROUT, heal = 7),
    COD(item = Items.COD, heal = 7),
    PIKE(item = Items.PIKE, heal = 8),
    SALMON(item = Items.SALMON, heal = 9),
    TUNA(item = Items.TUNA, heal = 10),
    RAINBOW(item = Items.RAINBOW_FISH, heal = 11),
    CAVEEEL(item = Items.CAVE_EEL, heal = 9),
    LOBSTER(item = Items.LOBSTER, heal = 12),
    BASS(item = Items.BASS, heal = 13),
    SWORDFISH(item = Items.SWORDFISH, heal = 14),
    MONKFISH(item = Items.MONKFISH, heal = 16),
    KARAMBWAN(item = Items.COOKED_KARAMBWAN, heal = 18, comboFood = true),
    SHARK(item = Items.SHARK, heal = 20),
    SEATURTLE(item = Items.SEA_TURTLE, heal = 21),
    MANTA_RAY(item = Items.MANTA_RAY, heal = 21),
    DARK_CRAB(item = Items.DARK_CRAB, heal = 22),
    ANGLERFISH(item = Items.ANGLERFISH, overheal = true),

    /**
     * Meat.
     */
    CHICKEN(item = Items.COOKED_CHICKEN, heal = 4),
    MEAT(item = Items.COOKED_MEAT, heal = 4),
    ROASTBEASTMEAT(item = Items.ROAST_BEAST_MEAT, heal = 8),
    KEBAB(item = Items.UGTHANKI_KEBAB, heal = 19),

    /**
     * Pastries.
     */
    BREAD(item = Items.BREAD, heal = 5),
    CAKE(item = Items.CAKE, heal = 4, replacement = Items._23_CAKE),
    CAKE23(item = Items._23_CAKE, heal = 4, replacement = Items.SLICE_OF_CAKE),
    SLICEOFCAKE(item = Items.SLICE_OF_CAKE, heal = 4),
    CHOCOLATECAKE(item = Items.CHOCOLATE_CAKE, heal = 5, replacement = Items._23_CHOCOLATE_CAKE),
    CHOCOLATECAKE23(item = Items._23_CHOCOLATE_CAKE, heal = 5, replacement = Items.CHOCOLATE_SLICE),
    CHOCOLATESLICEOFCAKE(item = Items.CHOCOLATE_SLICE, heal = 5),

    /**
     * Pizza.
     */
    PLAIN_PIZZA(item = Items.PLAIN_PIZZA, heal = 7, replacement = Items.HALF_PLAIN_PIZZA),
    HALF_PLAIN_PIZZA(item = Items.HALF_PLAIN_PIZZA, heal = 7),
    MEAT_PIZZA(item = Items.MEAT_PIZZA, heal = 8, replacement = Items.HALF_MEAT_PIZZA),
    HALF_MEAT_PIZZA(item = Items.HALF_MEAT_PIZZA, heal = 8),
    ANCHOVY_PIZZA(item = Items.ANCHOVY_PIZZA, heal = 9, replacement = Items.HALF_ANCHOVY_PIZZA),
    HALF_ANCHOVY_PIZZA(item = Items.HALF_ANCHOVY_PIZZA, heal = 9),
    PINEAPPLE_PIZZA(item = Items.PINEAPPLE_PIZZA, heal = 11, replacement = Items.HALF_PINEAPPLE_PIZZA),
    HALF_PINEAPPLE_PIZZA(item = Items.HALF_PINEAPPLE_PIZZA, heal = 11),
    /**
     * Other.
     */
    ONION(item = Items.ONION, heal = 1),
    CABBAGE(item = Items.CABBAGE, heal = 1),
    POTATO(item = Items.BAKED_POTATO, heal = 4),
    ;
    companion object {
        val values = enumValues<Food>()
    }
}