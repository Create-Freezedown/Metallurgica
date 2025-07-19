package com.freezedown.metallurgica.infastructure.material.scrapping;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.MaterialHelper;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import lombok.Getter;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class ScrappingData {

    @Getter
    private final List<ScrappingOutput> outputs;
    @Getter
    private final List<ExtraScrappingOutput> extras;

    private ScrappingData() {
        this.outputs = new ArrayList<>();
        this.extras = new ArrayList<>();
    }

    public static ScrappingData create() {
        return new ScrappingData();
    }

    public ScrappingData addOutput(Material material, int amount, int discardAmount, float discardChance) {
        this.outputs.add(new ScrappingOutput(material, amount, discardAmount, discardChance));
        return this;
    }

    public ScrappingData addOutput(Material material, int amount, float discardChance) {
        this.addOutput(material, amount, amount, discardChance);
        return this;
    }

    public ScrappingData addOutput(Material material, float discardChance) {
        this.addOutput(material, 1, 1, discardChance);
        return this;
    }

    public ScrappingData addExtraOutput(Material material, FlagKey<?> flagKey, int amount, float chance) {
        ItemLike item = MaterialHelper.getItem(material, flagKey);
        if (item != null) {
            return addExtraOutput(item, amount, chance);
        }
        return this;
    }

    public ScrappingData addExtraOutput(ItemLike item, int amount, float chance) {
        this.extras.add(new ExtraScrappingOutput(item, amount, chance));
        return this;
    }

    public ScrappingData addExtraOutput(ItemLike item, int amount) {
        this.addExtraOutput(item, amount, 1.0f);
        return this;
    }

    public ScrappingData addExtraOutput(ItemLike item, float chance) {
        this.addExtraOutput(item, 1, chance);
        return this;
    }




    public record ScrappingOutput(Material material, int amount, int discardAmount, float discardChance) {

    }

    public record ExtraScrappingOutput(ItemLike item, int amount, float chance) {

    }
}
