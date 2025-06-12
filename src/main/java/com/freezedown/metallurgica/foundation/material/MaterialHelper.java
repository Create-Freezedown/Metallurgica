package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.freezedown.metallurgica.registry.material.MetMaterialBlocks.MATERIAL_BLOCKS;
import static com.freezedown.metallurgica.registry.material.MetMaterialItems.MATERIAL_ITEMS;

public class MaterialHelper {

    public static List<ItemEntry<? extends MaterialItem>> getAllItems(Material material) {
        List<ItemEntry<? extends MaterialItem>> allItems = new ArrayList<>();
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof ItemFlag) {
                var item = MATERIAL_ITEMS.get(flagKey, material);
                if (item == null) continue;
                allItems.add(item);
            }
        }
        return allItems;
    }

    public static List<BlockEntry<? extends MaterialBlock>> getAllBlocks(Material material) {
        List<BlockEntry<? extends MaterialBlock>> allBlocks = new ArrayList<>();
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof BlockFlag) {
                var block = MATERIAL_BLOCKS.get(flagKey, material);
                if (block == null) continue;
                allBlocks.add(block);
            }
        }
        return allBlocks;
    }

    public static Codec<Material> byNameCodec() {
        return ResourceLocation.CODEC.flatXmap((resLoc) -> Optional.ofNullable(MetMaterials.registeredMaterials.get(resLoc)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown material: " + resLoc.toString())), (material) ->  {
            ResourceLocation resLoc = material.getId();
            if (resLoc == null) {
                return DataResult.error(() -> "Material has no registry name: " + material.getName());
            }
            return DataResult.success(resLoc);
        });
    }

    public static ItemEntry<? extends MaterialItem> getItem(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof ItemFlag) {
            return MATERIAL_ITEMS.get(flagKey, material);
        } else {
            throw new IllegalArgumentException("Flag " + flagKey + " is not an ItemFlag for material: " + material.getName());
        }
    }

    public static BlockEntry<? extends MaterialBlock> getBlock(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof BlockFlag) {
            return MATERIAL_BLOCKS.get(flagKey, material);
        } else {
            throw new IllegalArgumentException("Flag " + flagKey + " is not a BlockFlag for material: " + material.getName());
        }
    }

    public static List<Item> getAllMaterialItems(Material material) {
        if (material == null) {
            Metallurgica.LOGGER.error("Material is null, cannot get all items for null material.");
            return new ArrayList<>();
        }
        Map<FlagKey<?>, ResourceLocation> existingIds = material.materialInfo().existingIds;
        List<Item> items = new ArrayList<>();
        for (ItemEntry<? extends MaterialItem> item : MaterialHelper.getAllItems(material)) {
            items.add(item.get());
        }
        for (BlockEntry<? extends MaterialBlock> block : MaterialHelper.getAllBlocks(material)) {
            items.add(block.get().asItem());
        }
        for (FlagKey<? extends IMaterialFlag> flagKey : material.getFlags().getNoRegister()) {
            if (material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
                ResourceLocation itemId = existingIds.containsKey(flagKey) ? existingIds.get(flagKey) : itemFlag.getExistingId(material, material.materialInfo().nameAlternatives().get(flagKey));
                Item item = BuiltInRegistries.ITEM.get(itemId);
                if (item != null) {
                    items.add(item);
                } else {
                    Metallurgica.LOGGER.warn("Item {} for material {} is not registered. Consider updating or removing the Material Flag's existing namespace", itemId, material.getName());
                }
            }
            if (material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                ResourceLocation blockId = existingIds.containsKey(flagKey) ? existingIds.get(flagKey) : blockFlag.getExistingId(material, material.materialInfo().nameAlternatives().get(flagKey));
                Block block = BuiltInRegistries.BLOCK.get(blockId);
                if (block != null) {
                    Item item = block.asItem();
                    items.add(item);
                } else {
                    Metallurgica.LOGGER.warn("Block {} for material {} is not registered. Consider updating or removing the Material Flag's existing namespace", blockId, material.getName());
                }
            }
        }
        return items;
    }
}
