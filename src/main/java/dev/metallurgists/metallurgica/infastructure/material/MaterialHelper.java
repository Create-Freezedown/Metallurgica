package dev.metallurgists.metallurgica.infastructure.material;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.*;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class MaterialHelper {

    public static List<Item> getAllItems(Material material) {
        return getAllItems(material, false);
    }

    public static List<Item> getAllItems(Material material, boolean onlyChemicalTooltippable) {
        List<Item> allItems = new ArrayList<>();
        Map<FlagKey<?>, ResourceLocation> existingIds = material.materialInfo().existingIds;
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof IItemRegistry itemFlag) {
                if (onlyChemicalTooltippable)
                    if (itemFlag instanceof IConditionalComposition conditionalComposition && !conditionalComposition.shouldHaveComposition()) continue;
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
            if (flag instanceof IBlockRegistry blockFlag) {
                if (onlyChemicalTooltippable)
                    if (blockFlag instanceof IConditionalComposition conditionalComposition && !conditionalComposition.shouldHaveComposition()) continue;
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
            if (flag instanceof IFluidRegistry fluidFlag) {
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
        if (!(material.getFlag(flagKey) instanceof IItemRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not an item flag");
        ResourceLocation resultId = flag.getExistingId(material);
        Item item = BuiltInRegistries.ITEM.get(resultId);
        if (item == null) throw new RuntimeException("No valid item of flag: " + flagKey.toString() + " found for material: " + material.getId());
        return item;
    }

    public static Block getBlock(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof IBlockRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a block flag");
        ResourceLocation resultId = flag.getExistingId(material);
        Block block = BuiltInRegistries.BLOCK.get(resultId);
        if (block == null) throw new RuntimeException("No valid block of flag: " + flagKey.toString() + " found for material: " + material.getId());
        return block;
    }

    public static Fluid getFluid(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof IFluidRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a fluid flag");
        ResourceLocation resultId = flag.getExistingId(material);
        Fluid fluid = BuiltInRegistries.FLUID.get(resultId);
        if (fluid == null) throw new RuntimeException("No valid fluid of flag: " + flagKey.toString() + " found for material: " + material.getId());
        return fluid;
    }

    public static List<Item> getAllMaterialItems(Material material) {
        if (material == null) {
            cannotGetAllItemsForNullMaterial();
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
            cannotGetAllItemsForNullMaterial();
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>(MaterialHelper.getAllItems(material, true));
        MaterialHelper.getAllBlocks(material, true).forEach(block -> items.add(block.asItem()));
        return items;
    }

    private static void cannotGetAllItemsForNullMaterial() {
        Metallurgica.LOGGER.error("Material is null, cannot get all items for null material.");
    }

    public static String getNameForRecipe(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof IIdPattern idPattern) {
            String namespacePrefix = Objects.equals(material.getNamespace(), "metallurgica") ? "" : material.getNamespace() + "_";
            return namespacePrefix + idPattern.getIdPattern().formatted(material.getName());
        } else throw new IllegalArgumentException("FlagKey: " + flagKey.toString() + " does not implement IIdPattern and thus cannot be used for a recipe path.");
    }

    public static class FluidValues {
        public static int NUGGET = 16;
        public static int INGOT = 144;
        public static int BLOCK = 1296;

        public static int ingots(int amount) {
            return INGOT * amount;
        }
        public static int nuggets(int amount) {
            return NUGGET * amount;
        }
        public static int blocks(int amount) {
            return BLOCK * amount;
        }
    }
}
