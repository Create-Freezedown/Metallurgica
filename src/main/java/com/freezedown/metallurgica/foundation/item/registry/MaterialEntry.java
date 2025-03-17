package com.freezedown.metallurgica.foundation.item.registry;

import com.freezedown.metallurgica.foundation.config.TFMGConductor;
import com.freezedown.metallurgica.foundation.item.registry.flags.*;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.conductor.CableItem;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.conductor.ConductorEntry;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Objects;

@SuppressWarnings("DataFlowIssue")
@Getter
public class MaterialEntry {

    private final MetallurgicaRegistrate registrate;
    private final Material material;

    public MaterialEntry(MetallurgicaRegistrate registrate, Material material) {
        this.registrate = registrate;
        this.material = material;
    }


    public void registerEverything() {
        String materialName = material.getName();
        if (material.hasFlag(FlagKey.INGOT)) {
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            String name = Objects.requireNonNullElse(material.getMaterialInfo().getNameAlternatives().get(FlagKey.INGOT), material.getName());
            if (!ingotFlag.isNoRegister()) {
                NonNullFunction<Item.Properties, ? extends Item> factory = ingotFlag.hasFactory() ? ingotFlag.getFactory() : Item::new;
                getRegistrate().item(name + "_ingot", factory)
                        .model((ctx, prov) -> prov.generated(ctx, matAssetLoc(material, "ingot")))
                        .tag(AllTags.forgeItemTag("ingots/" + materialName))
                        .register();
            }
        }
        if (material.hasFlag(FlagKey.SHEET)) {
            SheetFlag sheetFlag = material.getFlag(FlagKey.SHEET);
            String name = Objects.requireNonNullElse(material.getMaterialInfo().getNameAlternatives().get(FlagKey.SHEET), material.getName());
            if (!sheetFlag.isNoRegister()) {
                NonNullFunction<Item.Properties, ? extends Item> factory = sheetFlag.hasFactory() ? sheetFlag.getFactory() : Item::new;
                getRegistrate().item(name + "_sheet", factory)
                        .model((ctx, prov) -> prov.generated(ctx, matAssetLoc(material, "sheet")))
                        .tag(AllTags.forgeItemTag("plates/" + materialName))
                        .register();
            }
            if (sheetFlag.isNeedsTransitional()) {
                getRegistrate().item("semi_pressed_" + name + "_sheet", SequencedAssemblyItem::new)
                        .model((ctx, prov) -> prov.generated(ctx, matAssetLoc(material, "semi_pressed_sheet")))
                        .register();
            }
        }
        if (material.hasFlag(FlagKey.WIRING)) {
            WiringFlag wiringFlag = material.getFlag(FlagKey.WIRING);
            String name = Objects.requireNonNullElse(material.getMaterialInfo().getNameAlternatives().get(FlagKey.WIRING), material.getName());
            ConductorEntry<Conductor> conductor = getRegistrate().conductor(materialName, Conductor::new)
                    .properties(p -> p.color1(wiringFlag.getColors().getFirst()).color2(wiringFlag.getColors().getSecond()))
                    .transform(TFMGConductor.setResistivity(wiringFlag.getResistivity()))
                    .register();
            getRegistrate().item(name + "_cable", (p) -> new CableItem(p, conductor))
                    .model((ctx, prov) -> prov.generated(ctx, matAssetLoc(material, "cable")))
                    .tag(MetallurgicaTags.modItemTag("cables"))
                    .register();
            getRegistrate().item(name + "_wire", Item::new)
                    .model((ctx, prov) -> prov.generated(ctx, matAssetLoc(material, "wire")))
                    .tag(AllTags.forgeItemTag("wires/" + materialName))
                    .recipe((ctx, prov) -> prov.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/" + materialName)), RecipeCategory.MISC, ctx, 2))
                    .register();
        }
    }

    private ResourceLocation matAssetLoc(Material material, String name) {
        return new ResourceLocation(material.getModid(), "item/materials/"+material.getName()+"/" + name);
    }
}
