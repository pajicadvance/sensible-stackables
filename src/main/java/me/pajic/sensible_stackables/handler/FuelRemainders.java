package me.pajic.sensible_stackables.handler;

import net.minecraft.world.item.ItemStack;

public class FuelRemainders {

    /**
     * Furnaces only return the crafting remainder of a fuel item once the whole fuel stack is used up,
     * so stacked fuel with a remainder (such as lava buckets) loses all remainders but the last one.
     */
    public static boolean hasCraftingRemainder(ItemStack stack) {
        //? >=26.1 {
        return stack.getItem().getCraftingRemainder() != null;
        //?} else {
        /*return stack.getItem().hasCraftingRemainingItem();
        *///?}
    }
}
