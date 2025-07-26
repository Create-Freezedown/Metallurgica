package dev.metallurgists.metallurgica.foundation.mixin;

import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.MaterialCompositionManager;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public class ChemicalInfoTooltipMixin {
    
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        Item item = stack.getItem();
        boolean notAMaterialCheckSingleCompositions = false;
        for (Material material : MetMaterials.registeredMaterials.values()) {
            if (MaterialCompositionManager.hasComposition(material)) {
                var allMatItem = MaterialHelper.getAllMaterialItemsForTooltips(material);
                if (allMatItem.contains(item)) {
                    LangBuilder compositionName = ClientUtil.lang();
                    metallurgica$createTooltip(compositionName, MaterialCompositionManager.getSubCompositions(material));
                    if (!compositionName.string().isEmpty()) {
                        tooltip.add(ClientUtil.lang().space().space().space()
                                .add(compositionName)
                                .component().withStyle(style -> style.withColor(MetallurgicaConfigs.client().tooltipColor.get())));
                    }
                    return; // No need to check further if we found a material match
                } else {
                    notAMaterialCheckSingleCompositions = true;
                }
            }
        }
        if (notAMaterialCheckSingleCompositions) {
            if (CompositionManager.hasComposition(stack.getItem())) {
                LangBuilder compositionName = ClientUtil.lang();
                metallurgica$createTooltip(compositionName, CompositionManager.getSubCompositions(stack.getItem()));
                if (!compositionName.string().isEmpty()) {
                    tooltip.add(ClientUtil.lang().space().space().space()
                            .add(compositionName)
                            .component().withStyle(style -> style.withColor(MetallurgicaConfigs.client().tooltipColor.get())));
                }
            }
        }
    }

    @Unique
    private void metallurgica$createTooltip(LangBuilder compositionName, List<SubComposition> subCompositions) {
        int size = subCompositions.size();
        for (int i = 0; i < size; i++) {
            if (subCompositions.get(i) == null) continue;
            LangBuilder subComp = ClientUtil.lang();
            SubComposition subComposition = Objects.requireNonNull(subCompositions.get(i));
            int elementsSize = subComposition.getElements().size();
            if (elementsSize > 1) {
                subComp.add(Component.literal("("));
                for (int j = 0; j < elementsSize; j++) {
                    if (subComposition.getElements().get(j) == null) continue;
                    subComp.add(Component.literal(subComposition.getElement(j).getDisplay()));
                }
                subComp.add(Component.literal(")"));
            } else {
                subComp.add(Component.literal(subComposition.getElement(0).getDisplay()));
            }
            compositionName.add(subComp);
        }
    }
}
