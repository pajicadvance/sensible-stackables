package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.math.NumberUtils;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {

    @Shadow @Final private Matrix3x2fStack pose;

    /**
     * @reason Abbreviates large item counts to prevent text overlapping in GUIs.
     */
    @WrapOperation(
            method = "itemCount",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
            )
    )
    private void modifyStackSizeText(
			GuiGraphicsExtractor instance, Font font, String string, int x, int y, int color, boolean dropShadow, Operation<Void> original,
			@Local(name = "amount") String amount, @Local(name = "x") int xParam, @Local(name = "y") int yParam
	) {
        if (ModConfig.CONFIG.uncapStackSize() && amount.length() > 2 && NumberUtils.isCreatable(amount)) {
            String formatted = SensibleStackables.FORMATTER.format(Integer.parseInt(amount));
            float scale = switch (formatted.length()) {
                case 1, 2 -> 1.0F;
                case 3 -> 0.75F;
                default -> 0.5F;
            };
            pose.translate(xParam, yParam);
            if (scale != 1) pose.scale(scale, scale);
			original.call(
                    instance, font, formatted,
                    (int) (16 / scale - font.width(formatted) + (scale * 0.33F) + 1 / scale),
                    (int) (16 / scale - font.lineHeight + Mth.ceil(scale) + 1 / scale),
                    color, dropShadow
            );
        }
        else original.call(instance, font, string, x, y, color, dropShadow);
    }
}
