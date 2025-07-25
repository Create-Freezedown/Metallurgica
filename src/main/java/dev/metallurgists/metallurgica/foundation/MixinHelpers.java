package dev.metallurgists.metallurgica.foundation;

import dev.metallurgists.metallurgica.foundation.material.block.IMaterialBlock;
import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialBlocks;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialItems;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MixinHelpers {

    public static <T> void generateDynamicTags(Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap, Registry<T> registry) {
        if (registry == BuiltInRegistries.ITEM) {
            for (Table.Cell<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> entryCell : MetMaterialItems.MATERIAL_ITEMS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var itemEntry = entryCell.getValue();
                if (material != null) {
                    String materialName = material.getName();
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
                        List<String> tagPatterns = itemFlag.getTagPatterns();
                        for (String tagPattern : tagPatterns) {
                            List<TagLoader.EntryWithSource> tags = new ArrayList<>();
                            String tagName = tagPattern.formatted(materialName);
                            ResourceLocation tagId = ResourceLocation.tryParse(tagName);
                            tags.add(new TagLoader.EntryWithSource(
                                    TagEntry.element(BuiltInRegistries.ITEM.getKey(itemEntry.get())),
                                    "Metallurgica Custom Tags"));
                            tagMap.computeIfAbsent(tagId, path -> new ArrayList<>()).addAll(tags);
                        }
                    }
                }
            }
            for (Table.Cell<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> entryCell : MetMaterialBlocks.MATERIAL_BLOCKS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var itemEntry = entryCell.getValue().asItem();
                if (material != null) {
                    String materialName = material.getName();
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                        List<String> tagPatterns = blockFlag.getItemTagPatterns();
                        for (String tagPattern : tagPatterns) {
                            List<TagLoader.EntryWithSource> tags = new ArrayList<>();
                            String tagName = tagPattern.formatted(materialName);
                            ResourceLocation tagId = ResourceLocation.tryParse(tagName);
                            tags.add(new TagLoader.EntryWithSource(
                                    TagEntry.element(BuiltInRegistries.ITEM.getKey(itemEntry)),
                                    "Metallurgica Custom Tags"));
                            tagMap.computeIfAbsent(tagId, path -> new ArrayList<>()).addAll(tags);
                        }
                    }
                }
            }
        }
        if (registry == BuiltInRegistries.BLOCK) {
            for (Table.Cell<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> entryCell : MetMaterialBlocks.MATERIAL_BLOCKS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var blockEntry = entryCell.getValue();
                if (material != null) {
                    String materialName = material.getName();
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                        List<String> tagPatterns = blockFlag.getTagPatterns();
                        for (String tagPattern : tagPatterns) {
                            List<TagLoader.EntryWithSource> tags = new ArrayList<>();
                            String tagName = tagPattern.formatted(materialName);
                            ResourceLocation tagId = ResourceLocation.tryParse(tagName);
                            tags.add(new TagLoader.EntryWithSource(
                                    TagEntry.element(BuiltInRegistries.BLOCK.getKey(blockEntry.get())),
                                    "Metallurgica Custom Tags"));
                            tagMap.computeIfAbsent(tagId, path -> new ArrayList<>()).addAll(tags);
                        }
                    }
                }
            }
        }
    }


}
