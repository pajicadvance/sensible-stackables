package me.pajic.sensible_stackables.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.SensibleStackablesClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//~ if >=26.1 'com.mojang.blaze3d.vertex.PoseStack' -> 'org.joml.Matrix3x2fStack'
import org.joml.Matrix3x2fStack;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {

    //~ if >=26.1 'PoseStack' -> 'Matrix3x2fStack'
    @Shadow @Final private Matrix3x2fStack pose;

    /**
     * @reason Shortens large item counts and scales down font size to prevent text overlapping in GUIs.
     */
    @WrapOperation(
            method = {"itemCount", "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"},
            at = @At(
                    value = "INVOKE",
                    //? <26.1
                    //target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"
                    //? >=26.1
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
            )
    )
    //~ if >=26.1 'int' -> 'void'
    private void modifyStackSizeText(
            //~ if >=26.1 'Integer' -> 'Void'
			GuiGraphicsExtractor instance, Font font, String str, int x, int y, int color, boolean dropShadow, Operation<Void> original,
		    @Local(ordinal = 1) String amount, @Local(argsOnly = true, ordinal = 0) int xParam, @Local(argsOnly = true, ordinal = 1) int yParam
	) {
        if (SensibleStackables.CONFIG.uncapStackSize.get() && amount.length() > 2 && NumberUtils.isCreatable(amount)) {
            String formatted = SensibleStackablesClient.CONFIG.itemCountShortening.get() ?
					SensibleStackablesClient.FORMATTER.format(Integer.parseInt(amount)) : str;
            float scale = SensibleStackablesClient.CONFIG.itemCountScaling.get() ? switch (formatted.length()) {
				case 1, 2 -> 1.0F;
                case 3 -> 0.75F;
                default -> 0.5F;
            } : 1.0F;
            pose.translate(xParam, yParam/*? <26.1 {*//*, 0*//*?}*/);
            if (scale != 1) pose.scale(scale, scale/*? <26.1 {*//*, 1*//*?}*/);
            /*? <26.1 {*//*return *//*?}*/original.call(
                    instance, font, formatted,
                    (int) (16 / scale - font.width(formatted) + (scale * 0.33F) + 1 / scale),
                    (int) (16 / scale - font.lineHeight + Mth.ceil(scale) + 1 / scale),
                    color, dropShadow
            );
        }
        //~ if >=26.1 'return' -> 'else'
        else original.call(instance, font, str, x, y, color, dropShadow);
    }
}
