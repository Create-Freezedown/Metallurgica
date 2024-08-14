package com.freezedown.metallurgica.content.forging;

import com.freezedown.metallurgica.foundation.material.MetalEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class ToolPartItem extends Item {
    private MetalEntry[] metals;
    private Tier tier;
    private String moldType;

    public ToolPartItem(Properties pProperties, Tier pTier, MetalEntry... metal) {
        super(pProperties);
        metals = metal;
        tier = pTier;
    }


}
