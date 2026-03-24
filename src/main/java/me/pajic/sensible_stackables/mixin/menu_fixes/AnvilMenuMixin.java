package me.pajic.sensible_stackables.mixin.menu_fixes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow public abstract void createResult();

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }

    @ModifyArg(
			//? if fabric
            method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 4
            ),
            index = 1
    )
    private ItemStack modifyResult(ItemStack stack) {
        if (stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) stack.setCount(1);
        return stack;
    }

    @ModifyExpressionValue(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getCount()I",
                    ordinal = 1
            )
    )
    private int modifyInputCountCondition(int original, @Local(name = "input") ItemStack input) {
        if (input.is(Items.ENCHANTED_BOOK)) return 1;
        return original;
    }

    @ModifyArg(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 3
            ),
            index = 1
    )
    private ItemStack modifyLeftInputOnTake(ItemStack original) {
        ItemStack stack = inputSlots.getItem(0);
        if (stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) {
            stack.shrink(1);
            return stack;
        }
        return original;
    }

    @ModifyArg(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 2
            ),
            index = 1
    )
    private ItemStack modifyRightInputOnTake(ItemStack original) {
        ItemStack stack = inputSlots.getItem(1);
        if (stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) {
            stack.shrink(1);
            return stack;
        }
        return original;
    }

    @WrapMethod(method = "onTake")
    private void triggerCreateResultOnTake(Player player, ItemStack stack, Operation<Void> original) {
        original.call(player, stack);
        createResult();
    }
}
