package dev.metallurgists.metallurgica.content.metalworking;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class ToolPartItem extends Item {
    private Material[] materialList;
    private Tier tier;
    private String moldType;

    public ToolPartItem(Properties pProperties, Tier pTier, Material... materials) {
        super(pProperties);
        materialList = materials;
        tier = pTier;
    }


}
