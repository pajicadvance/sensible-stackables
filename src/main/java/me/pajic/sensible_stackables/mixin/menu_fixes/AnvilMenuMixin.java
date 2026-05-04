package me.pajic.sensible_stackables.mixin.menu_fixes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
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

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

	@Shadow public abstract void createResult();
	@Shadow private int repairItemCountCost;
	@Shadow private boolean onlyRenaming;

	public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }

	@Definition(id = "resultSlots", field = "Lnet/minecraft/world/inventory/AnvilMenu;resultSlots:Lnet/minecraft/world/inventory/ResultContainer;")
	@Definition(id = "setItem", method = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V")
	@Definition(id = "result", local = @Local(type = ItemStack.class, name = "result"))
	@Expression("this.resultSlots.setItem(0, @(result))")
	@ModifyExpressionValue(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private ItemStack modifyResult(ItemStack stack) {
        if (!onlyRenaming && stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) stack.setCount(1);
        return stack;
    }

	@Definition(id = "input", local = @Local(type = ItemStack.class, name = "input"))
	@Definition(id = "getCount", method = "Lnet/minecraft/world/item/ItemStack;getCount()I")
	@Expression("input.getCount() > 1")
	@ModifyExpressionValue(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean modifyInputCountCondition(boolean original, @Local(name = "input") ItemStack input) {
		return !input.is(Items.ENCHANTED_BOOK) && original;
    }

	@Definition(id = "inputSlots", field = "Lnet/minecraft/world/inventory/AnvilMenu;inputSlots:Lnet/minecraft/world/Container;")
	@Definition(id = "setItem", method = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V")
	@Definition(id = "EMPTY", field = "Lnet/minecraft/world/item/ItemStack;EMPTY:Lnet/minecraft/world/item/ItemStack;")
	@Expression("this.inputSlots.setItem(0, @(EMPTY))")
	@ModifyExpressionValue(
			method = "onTake",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private ItemStack modifyLeftInputOnTake(ItemStack original) {
        ItemStack stack = inputSlots.getItem(0);
        if (!onlyRenaming && stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) {
            stack.shrink(1);
            return stack;
        }
        return original;
    }

	@Definition(id = "inputSlots", field = "Lnet/minecraft/world/inventory/AnvilMenu;inputSlots:Lnet/minecraft/world/Container;")
	@Definition(id = "setItem", method = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V")
	@Definition(id = "EMPTY", field = "Lnet/minecraft/world/item/ItemStack;EMPTY:Lnet/minecraft/world/item/ItemStack;")
	@Expression("this.inputSlots.setItem(1, @(EMPTY))")
	@ModifyExpressionValue(
			method = "onTake",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private ItemStack modifyRightInputOnTake(ItemStack original) {
		if (repairItemCountCost == 0) {
			ItemStack stack = inputSlots.getItem(1);
			if (stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) {
				stack.shrink(1);
				return stack;
			}
		}
        return original;
    }

    @WrapMethod(method = "onTake")
    private void triggerCreateResultOnTake(Player player, ItemStack stack, Operation<Void> original) {
        original.call(player, stack);
        createResult();
    }
}
