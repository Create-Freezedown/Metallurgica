package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.block.IMaterialBlock;
import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.freezedown.metallurgica.registry.material.init.MetMaterialBlocks.MATERIAL_BLOCKS;
import static com.freezedown.metallurgica.registry.material.init.MetMaterialFluids.MATERIAL_FLUIDS;
import static com.freezedown.metallurgica.registry.material.init.MetMaterialItems.MATERIAL_ITEMS;

public class MaterialHelper {

    public static List<Item> getAllItems(Material material) {
        List<Item> allItems = new ArrayList<>();
        Map<FlagKey<?>, ResourceLocation> existingIds = material.materialInfo().existingIds;
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof ItemFlag itemFlag) {
                if (existingIds.containsKey(flagKey)) {
                    var item = BuiltInRegistries.ITEM.get(existingIds.get(flagKey));
                    if (item == null) continue;
                    allItems.add(item);
                    continue;
                }
                var item = getItem(material, itemFlag.getKey());
                if (item == null) continue;
                allItems.add(item);
            }
        }
        return allItems;
    }

    public static List<Block> getAllBlocks(Material material) {
        return getAllBlocks(material, false);
    }

    public static List<Block> getAllBlocks(Material material, boolean onlyChemicalTooltippable) {
        List<Block> allBlocks = new ArrayList<>();
        Map<FlagKey<?>, ResourceLocation> existingIds = material.materialInfo().existingIds;
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof BlockFlag blockFlag) {
                if (onlyChemicalTooltippable)
                    if (!blockFlag.shouldHaveComposition()) continue;
                if (existingIds.containsKey(flagKey)) {
                    var block = BuiltInRegistries.BLOCK.get(existingIds.get(flagKey));
                    if (block == null) continue;
                    allBlocks.add(block);
                    continue;
                }
                var block = getBlock(material, blockFlag.getKey());
                if (block == null) continue;
                allBlocks.add(block);
            }
        }
        return allBlocks;
    }

    public static List<Fluid> getAllFluids(Material material) {
        List<Fluid> allFluids = new ArrayList<>();
        Map<FlagKey<?>, ResourceLocation> existingIds = material.materialInfo().existingIds;
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof FluidFlag fluidFlag) {
                if (existingIds.containsKey(flagKey)) {
                    var fluid = BuiltInRegistries.FLUID.get(existingIds.get(flagKey));
                    if (fluid == null) continue;
                    allFluids.add(fluid);
                    continue;
                }
                var fluid = getFluid(material, fluidFlag.getKey());
                if (fluid == null) continue;
                allFluids.add(fluid);
            }
        }
        return allFluids;
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

    public static Item getItem(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof ItemFlag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not an item flag");
        var flag = material.getFlag(flagKey);
        ResourceLocation resultId = flag.getExistingId(material);
        Item item = BuiltInRegistries.ITEM.get(resultId);
        if (item == null) throw new RuntimeException("No valid item of flag: " + flagKey.toString() + " found for material: " + material.getId());
        return item;
    }

    public static Block getBlock(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof BlockFlag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a block flag");
        var flag = material.getFlag(flagKey);
        ResourceLocation resultId = flag.getExistingId(material);
        Block block = BuiltInRegistries.BLOCK.get(resultId);
        if (block == null) throw new RuntimeException("No valid block of flag: " + flagKey.toString() + " found for material: " + material.getId());
        return block;
    }

    public static Fluid getFluid(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof FluidFlag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a fluid flag");
        var flag = material.getFlag(flagKey);
        ResourceLocation resultId = flag.getExistingId(material);
        Fluid fluid = BuiltInRegistries.FLUID.get(resultId);
        if (fluid == null) throw new RuntimeException("No valid fluid of flag: " + flagKey.toString() + " found for material: " + material.getId());
        return fluid;
    }

    public static List<Item> getAllMaterialItems(Material material) {
        if (material == null) {
            Metallurgica.LOGGER.error("Material is null, cannot get all items for null material.");
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>(MaterialHelper.getAllItems(material));
        for (Block block : MaterialHelper.getAllBlocks(material)) {
            items.add(block.asItem());
        }
        return items;
    }

    public static List<Item> getAllMaterialItemsForTooltips(Material material) {
        if (material == null) {
            Metallurgica.LOGGER.error("Material is null, cannot get all items for null material.");
            return new ArrayList<>();
        }
        Map<FlagKey<?>, ResourceLocation> existingIds = material.materialInfo().existingIds;
        List<Item> items = new ArrayList<>(MaterialHelper.getAllItems(material));
        for (Block block : MaterialHelper.getAllBlocks(material, true)) {
            items.add(block.asItem());
        }
        return items;
    }
}
