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
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//? >=26.1 {
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
//?} else {
/*import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

//~ if >=26.1 'sensible_stackables$onlyRenaming' -> 'onlyRenaming' {
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

	@Shadow public abstract void createResult();
    //~ if neoforge 'private' -> 'public'
	@Shadow private int repairItemCountCost;
    //~ if >=26.1 'Unique' -> 'Shadow'
	@Shadow private boolean onlyRenaming;

	public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access/*? >=26.1 {*/, ItemCombinerMenuSlotDefinition slotDefinition/*?}*/) {
        super(menuType, containerId, inventory, access/*? >=26.1 {*/, slotDefinition/*?}*/);
    }

    //? <26.1 {
    /*@Inject(
            //? if fabric || (<26.1 && neoforge)
            //method = "createResult",
            //? if >=26.1 && neoforge
            method = "createResultInternal",
            at = @At("HEAD")
    )
    private void resetOnlyRenaming(CallbackInfo ci) {
        onlyRenaming = false;
    }

    @Definition(id = "t", local = @Local(type = int.class, ordinal = 2))
    @Definition(id = "set", method = "Lnet/minecraft/world/inventory/DataSlot;set(I)V")
    @Definition(id = "cost", field = "Lnet/minecraft/world/inventory/AnvilMenu;cost:Lnet/minecraft/world/inventory/DataSlot;")
    @Expression("this.cost.set(t)")
    @Inject(
            //? if fabric || (<26.1 && neoforge)
            //method = "createResult",
            //? if >=26.1 && neoforge
            method = "createResultInternal",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private void setOnlyRenaming(CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j) {
        if (j == i && j > 0) onlyRenaming = true;
    }
    *///?}

    @Definition(id = "resultSlots", field = "Lnet/minecraft/world/inventory/AnvilMenu;resultSlots:Lnet/minecraft/world/inventory/ResultContainer;")
    @Definition(id = "setItem", method = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V")
    @Definition(id = "result", local = @Local(type = ItemStack.class, ordinal = 1))
    @Expression("this.resultSlots.setItem(0, @(result))")
    @ModifyExpressionValue(
            //? if fabric || (<26.1 && neoforge)
            method = "createResult",
            //? if >=26.1 && neoforge
            //method = "createResultInternal",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private ItemStack modifyResult(ItemStack stack) {
        if (!onlyRenaming && stack.is(Items.ENCHANTED_BOOK) && stack.getCount() > 1) stack.setCount(1);
        return stack;
    }

	@Definition(id = "input", local = @Local(type = ItemStack.class, ordinal = 0))
	@Definition(id = "getCount", method = "Lnet/minecraft/world/item/ItemStack;getCount()I")
	@Expression("input.getCount() > 1")
	@ModifyExpressionValue(
            //? if fabric || (<26.1 && neoforge)
            method = "createResult",
            //? if >=26.1 && neoforge
            //method = "createResultInternal",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean modifyInputCountCondition(boolean original, @Local(ordinal = 0) ItemStack input) {
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
    private void triggerCreateResultOnTake(Player player, ItemStack carried, Operation<Void> original) {
        original.call(player, carried);
        createResult();
    }
}
//~}
