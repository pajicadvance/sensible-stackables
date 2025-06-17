package me.pajic.sensible_stackables.mixin;

import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//? if <= 1.21.1 {
import net.minecraft.world.food.FoodProperties;
//?}
//? if > 1.21.4
/*import net.minecraft.world.item.equipment.Equippable;*/

@Mixin(Item.class)
public class ItemMixin {

    @Mutable @Shadow @Final private DataComponentMap components;
    //? if > 1.21.1 {
    /*@Unique private final Set<String> bowlFoods = Set.of(
            "mushroom_stew", "rabbit_stew", "beetroot_soup", "suspicious_stew"
    );
    *///?}

    //? if > 1.21.5 {
    /*@Unique Set<Equippable> harnesses = new HashSet<>();
    @Unique private boolean isHarness() {
        if (harnesses.isEmpty()) for (DyeColor color : DyeColor.values()) harnesses.add(Equippable.harness(color));
        return harnesses.contains(components.get(DataComponents.EQUIPPABLE));
    }
    *///?}

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void modifyDataComponents(Item.Properties properties, CallbackInfo ci) {
        Item item = (Item) (Object) this;
        int newStackSize = -1;

        if (ModConfig.CONFIG.enableStackablePotions && item instanceof PotionItem) {
            newStackSize = ModConfig.CONFIG.potionMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableSaddles &&
                //? if <= 1.21.4
                item instanceof SaddleItem
                //? if > 1.21.4
                /*components.has(DataComponents.EQUIPPABLE) && components.get(DataComponents.EQUIPPABLE).equals(Equippable.saddle())*/
        ) {
            newStackSize = ModConfig.CONFIG.saddleMaxStackSize;
        //? if > 1.21.5 {
        /*} else if (ModConfig.CONFIG.enableStackableHarness && isHarness()) {
            newStackSize = ModConfig.CONFIG.harnessMaxStackSize;
        *///?}
        } else if (ModConfig.CONFIG.enableStackableMinecarts && item instanceof MinecartItem) {
            newStackSize = ModConfig.CONFIG.minecartMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableBoats && item instanceof BoatItem) {
            newStackSize = ModConfig.CONFIG.boatMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableBeds && item instanceof BedItem) {
            newStackSize = ModConfig.CONFIG.bedMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableSnowballs && item instanceof SnowballItem) {
            newStackSize = ModConfig.CONFIG.snowballMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableEggs && item instanceof EggItem) {
            newStackSize = ModConfig.CONFIG.eggMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableBannerPatterns &&
                //? if <= 1.21.4
                item instanceof BannerPatternItem
                //? if > 1.21.4
                /*components.has(DataComponents.PROVIDES_BANNER_PATTERNS)*/
        ) {
            newStackSize = ModConfig.CONFIG.bannerPatternMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableEnderPearls && item instanceof EnderpearlItem) {
            newStackSize = ModConfig.CONFIG.enderPearlMaxStackSize;
        //? if <= 1.21.4 {
        } else if (ModConfig.CONFIG.enableStackableHorseArmor && item instanceof AnimalArmorItem && !components.has(DataComponents.MAX_DAMAGE)) {
            newStackSize = ModConfig.CONFIG.horseArmorMaxStackSize;
        //?}
        } else if (ModConfig.CONFIG.enableStackableMusicDiscs && components.has(DataComponents.JUKEBOX_PLAYABLE)) {
            newStackSize = ModConfig.CONFIG.musicDiscMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableEnchantedBooks &&
                //? if <= 1.21.1
                item instanceof EnchantedBookItem
                //? if > 1.21.1
                /*components.has(DataComponents.STORED_ENCHANTMENTS) && !components.has(DataComponents.MAX_DAMAGE) && item.getDescriptionId().equals("item.minecraft.enchanted_book")*/
        ) {
            newStackSize = ModConfig.CONFIG.enchantedBookMaxStackSize;
        } else if (ModConfig.CONFIG.enableStackableBowlFoods && components.has(DataComponents.FOOD)) {
            //? if <= 1.21.1 {
            FoodProperties food = components.get(DataComponents.FOOD);
            if (food.usingConvertsTo().isPresent() && food.usingConvertsTo().get().is(Items.BOWL)) {
                newStackSize = ModConfig.CONFIG.bowlFoodMaxStackSize;
            }
            //?}
            //? if > 1.21.1 {
            /*for (String bowlFood : bowlFoods) {
                if (item.getDescriptionId().equals("item.minecraft." + bowlFood)) {
                    newStackSize = ModConfig.CONFIG.bowlFoodMaxStackSize;
                }
            }
            *///?}
        }

        if (newStackSize > 0 && newStackSize <= 64) {
            components = PatchedDataComponentMap.fromPatch(components, DataComponentPatch.builder().set(DataComponents.MAX_STACK_SIZE, newStackSize).build());
        }
    }
}