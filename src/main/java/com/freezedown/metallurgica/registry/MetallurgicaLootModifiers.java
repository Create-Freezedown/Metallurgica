package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.infastructure.loot_modifier.ReplaceItemLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS;
    public static final RegistryObject<Codec<ReplaceItemLootModifier>> REPLACE_ITEM_LOOT_MODIFIER;
    
    public MetallurgicaLootModifiers() {
    }
    
    static {
        LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "metallurgica");
        REPLACE_ITEM_LOOT_MODIFIER = LOOT_MODIFIERS.register("replace_item_loot_modifier", () -> ReplaceItemLootModifier.CODEC);
    }
}
