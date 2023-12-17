package com.freezedown.metallurgica.content.mineral.deposit;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;

public enum DepositTypes {
    magnetite(MetallurgicaBlocks.magnetiteDeposit.get(), MetallurgicaItems.magnetite.get(), 0.72f, 1.0f),
    nativeCopper(MetallurgicaBlocks.nativeCopperDeposit.get(), MetallurgicaItems.nativeCopper.get(), 0.12f, 0.88f)
    ;
    private final Block depositBlock;
    private final Item mineralItem;
    private final float chance;
    private final float minimumEfficiency;
    
    DepositTypes(Block depositBlock, Item mineralItem, float chance, float minimumEfficiency) {
        this.depositBlock = depositBlock;
        this.mineralItem = mineralItem;
        this.chance = chance;
        this.minimumEfficiency = minimumEfficiency;
    }
    
    public Block getDepositBlock() {
        return depositBlock;
    }
    
    public Item getMineralItem() {
        return mineralItem;
    }
    
    public float getChance() {
        return chance;
    }
    
    public float getMinimumEfficiency() {
        return minimumEfficiency;
    }
    
    public static DepositTypes getDepositTypeFromBlock(Block block) {
        for (DepositTypes depositType : DepositTypes.values()) {
            if (depositType.getDepositBlock() == block) {
                return depositType;
            }
        }
        return null;
    }
}
