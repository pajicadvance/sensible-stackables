package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.math.NumberUtils;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    //? if 1.21.1
    //@Shadow @Final private PoseStack pose;
    //? if > 1.21.1
    @Shadow @Final private Matrix3x2fStack pose;

    /**
     * @reason Abbreviates large item counts to prevent text overlapping in GUIs.
     */
    @WrapOperation(
            //? if 1.21.1
            //method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            //? if > 1.21.1
            method = "renderItemCount",
            at = @At(
                    value = "INVOKE",
                    //? if 1.21.1
                    //target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"
                    //? if > 1.21.1
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
            )
    )
    private /*? if 1.21.1 {*//*int*//*?} else {*/void/*?}*/ modifyStackSizeText(
            GuiGraphics instance, Font font, String text, int x, int y, int color, boolean dropShadow, Operation<Integer> original,
            @Local(ordinal = 1) String string, @Local(argsOnly = true, ordinal = 0) int xParam, @Local(argsOnly = true, ordinal = 1) int yParam
    ) {
        if (ModConfig.CONFIG.uncapStackSize() && string.length() > 2 && NumberUtils.isCreatable(string)) {
            String formatted = SensibleStackables.FORMATTER.format(Integer.parseInt(string));
            float scale = switch (formatted.length()) {
                case 1, 2 -> 1.0F;
                case 3 -> 0.75F;
                default -> 0.5F;
            };
            pose.translate(xParam, yParam/*? if 1.21.1 {*//*, 0*//*?}*/);
            if (scale != 1) pose.scale(scale, scale/*? if 1.21.1 {*//*, 0*//*?}*/);
            /*? if 1.21.1 {*//*return*//*?}*/ original.call(
                    instance, font, formatted,
                    (int) (16 / scale - font.width(formatted) + (scale * 0.33F) + 1 / scale),
                    (int) (16 / scale - font.lineHeight + Mth.ceil(scale) + 1 / scale),
                    color, dropShadow
            );
        }
        else /*? if 1.21.1 {*//*return*//*?}*/ original.call(instance, font, text, x, y, color, dropShadow);
    }
}
