package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.DepositCapacity;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.MOD;
import static com.freezedown.metallurgica.registry.MetallurgicaTags.optionalTag;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.create;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.createDeposit;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.createTypedDeposit;

public class MaterialEntry {
    private final BlockEntry<MineralDepositBlock> deposit;
    private final BlockEntry<Block> stone;
    private final TagKey<Block> tag;
    private final String name;
    private final ItemEntry<MetallurgicaItem> raw;
    private final ItemEntry<Item> rubble;
    @Nullable
    private final ItemEntry<MetallurgicaItem> rich;

    public MaterialEntry(MetallurgicaRegistrate reg, String pName, boolean richb) {
        raw = reg.raw(pName);
        rubble = reg.simpleItem(pName + "_rubble", "material_rubble/" + pName, "material_rubble");
        deposit = reg.depositBlock(pName + "_deposit", raw);
        ResourceLocation id = new ResourceLocation(MOD.id, "deposit_replaceable/" + pName);
        tag = MOD.optionalDefault ? optionalTag(ForgeRegistries.BLOCKS, id) : BlockTags.create(id);
        stone = reg.mineralBlock(pName, tag, raw);
        name = pName;
        rich = richb ? reg.metallurgicaItem("rich_magnetite", "enriched_materials/" + pName, "enriched_materials") : null;
    }
    
    public BlockEntry<MineralDepositBlock> depositBlock() { return deposit; }
    public BlockEntry<Block> stone() { return stone; }

    public TagKey<Block> tag() { return tag; }

    public ItemEntry<MetallurgicaItem> raw() {
        return raw;
    }

    public ItemEntry<MetallurgicaItem> rich() {
        return rich;
    }

    public ItemEntry<Item> rubble() {
        return rubble;
    }

    public MOreFeatureConfigEntry deposit(int size, int frequency, int minHeight, int maxHeight) {
        return create(name + "_deposit", size, frequency, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(stone, new TagMatchTest(tag))
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
    
    @Nullable
    public MDepositFeatureConfigEntry surfaceDeposit(float frequency, int chance, Couple<NonNullSupplier<? extends Block>> surfaceDepositStones, TagKey<Biome> biomeTag) {
        return createDeposit(name + "_surface_deposit", frequency, chance, 100, 320)
                .standardDatagenExt()
                .withBlocks(surfaceDepositStones, stone, deposit)
                .biomeTag(biomeTag)
                .parent();
    }
    
    @Nullable
    public MDepositFeatureConfigEntry surfaceDeposit(float frequency, int chance, Couple<NonNullSupplier<? extends Block>> surfaceDepositStones) {
        return createDeposit(name + "_surface_deposit", frequency, chance, 100, 320)
                .standardDatagenExt()
                .withBlocks(surfaceDepositStones, stone, deposit)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
    
    @Nullable
    public MDepositFeatureConfigEntry surfaceDepositNether(float frequency, int chance, Couple<NonNullSupplier<? extends Block>> surfaceDepositStones) {
        return createDeposit(name + "_surface_deposit", frequency, chance, 100, 320)
                .standardDatagenExt()
                .withBlocks(surfaceDepositStones, stone, deposit)
                .biomeTag(BiomeTags.IS_NETHER)
                .parent();
    }
    
    public MOreFeatureConfigEntry cluster(int size, int frequency, int minHeight, int maxHeight) {
        return create(name + "_cluster", size, frequency, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(stone, OreFeatures.STONE_ORE_REPLACEABLES)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
    
    @Nullable
    public MTypedDepositFeatureConfigEntry deposit(int maxHeight, int minHeight, int maxWidth, int minWidth, int maxDepth, int minDepth, float depositBlockChance, DepositCapacity capacity, float frequency, int chance, Couple<NonNullSupplier<? extends Block>> depositStones, TagKey<Biome> biomes) {
        return createTypedDeposit("large_" + name + "_deposit", maxWidth, minWidth, maxDepth, minDepth, depositBlockChance, capacity, frequency, chance, minHeight, maxHeight)
                .standardDatagenExt()
                .withBlocks(depositStones, stone, deposit)
                .biomeTag(biomes)
                .parent();
    }
    
    @Nullable
    public MTypedDepositFeatureConfigEntry deposit(int maxHeight, int minHeight, int maxWidth, int minWidth, int maxDepth, int minDepth, float depositBlockChance, DepositCapacity capacity, float frequency, int chance, Couple<NonNullSupplier<? extends Block>> depositStones) {
        return createTypedDeposit("large_" + name + "_deposit", maxWidth, minWidth, maxDepth, minDepth, depositBlockChance, capacity, frequency, chance, minHeight, maxHeight)
                .standardDatagenExt()
                .withBlocks(depositStones, stone, deposit)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
}
