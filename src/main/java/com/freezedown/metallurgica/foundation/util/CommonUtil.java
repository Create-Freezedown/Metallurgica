package com.freezedown.metallurgica.foundation.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonUtil {
    
    public static String getItemIdentifier(Item item) {
        String unwrappedHolder = ForgeRegistries.ITEMS.getHolder(item).flatMap(Holder::unwrapKey).toString();
        String prunedKey = unwrappedHolder.replace("Optional[ResourceKey[minecraft:item / ", "").replace("]]", "");
        return prunedKey;
    }
    
    public static ResourceLocation getItemIdentifierAsResourceLocation(Item item) {
        return new ResourceLocation(getItemIdentifier(item));
    }
}
