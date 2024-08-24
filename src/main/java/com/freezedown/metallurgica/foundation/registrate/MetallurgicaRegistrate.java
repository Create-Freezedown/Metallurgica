package com.freezedown.metallurgica.foundation.registrate;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.content.fluids.types.ReactiveGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasBlock;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.MBuilderTransformers;
import com.freezedown.metallurgica.foundation.item.AlloyItem;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.freezedown.metallurgica.foundation.material.MetalEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.DepositCapacity;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.*;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.createDeposit;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.createTypedDeposit;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class MetallurgicaRegistrate extends CreateRegistrate {
    /**
     * A list of all compositions that have been registered.
     * <p>
     * This is used to generate the default lang file and tooltips.
     * * <p>
     * The key is the name of the material, the value is the element composition.
     */
    public static final List<Map<String, String>> COMPOSITIONS = new ArrayList<>();
    public static final List<String> COMP_MOD_BLACKLIST = new ArrayList<>();
    
    public static void printCompositions() {
        for (Map<String, String> composition : COMPOSITIONS) {
            for (Map.Entry<String, String> entry : composition.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
    
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected MetallurgicaRegistrate(String modid) {
        super(modid);
        COMP_MOD_BLACKLIST.add("gtceu");
    }

    // UTIL
    
    public static String autoLang(String id) {
        StringBuilder builder = new StringBuilder();
        boolean b = true;
        for (char c: id.toCharArray()) {
            if(c == '_') {
                builder.append(' ');
                b = true;
            } else {
                builder.append(b ? String.valueOf(c).toUpperCase() : c);
                b = false;
            }
        }
        return builder.toString();
    }
    
    public static MetallurgicaRegistrate create(String modid) {
        return new MetallurgicaRegistrate(modid);
    }
    

    //GAS
    public FluidBuilder<FlowingGas.Flowing, CreateRegistrate> gas(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TintedFluid.create(color), FlowingGas.Flowing::new).source(FlowingGas.Source::new).block(GasBlock::new).build();
    }
    
    public FluidBuilder<ReactiveGas.Flowing, CreateRegistrate> reactiveGas(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TintedFluid.create(color), ReactiveGas.Flowing::new).source(ReactiveGas.Source::new).block(GasBlock::new).build();
    }
    
    
    //FLUIDS
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualDust(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/dust_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/dust_flow");
        return tintedVirtualFluid(name, color, still, flow);
    }
    
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color) {
        return tintedVirtualFluid(name, color, Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"));
    }
    
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color, ResourceLocation still, ResourceLocation flow) {
        return virtualFluid(name, still, flow, TintedFluid.create(color, still, flow), VirtualFluid::new);
    }
    
    
    
    public FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> tintedFluid(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TintedFluid.create(color), ForgeFlowingFluid.Flowing::new);
    }
    
    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, float acidity) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return acid(name, color, still, flow, acidity);
    }

    public FluidEntry<MoltenMetal> moltenMetal(String name, double moltenTemperature) {
        String id = "molten_" + name;
        ResourceLocation modelParent = new ResourceLocation("item/generated");
        ResourceLocation itemTexture = Metallurgica.asResource("item/molten_metal_bucket");
        ResourceLocation still = Metallurgica.asResource("fluid/molten_metal_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/molten_metal_flow");
        return virtualFluid("molten_" + name, still, flow, MoltenMetal.MoltenMetalFluidType::new, p -> new MoltenMetal(p).moltenTemperature(moltenTemperature))
                .lang(autoLang(id))
                .tag(MetallurgicaTags.modFluidTag("molten_metals/" + name))
                .tag(MetallurgicaTags.modFluidTag("molten_metals"))
                .register();
    }

    public FluidEntry<Acid> acid(String name, int color, float acidity, String lang) {
        return Metallurgica.registrate.acid(name, color, acidity).lang(lang).register();
    }

    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, ResourceLocation still, ResourceLocation flow, float acidity) {
        if (acidity > 14 || acidity < 0) {
            throw new IllegalArgumentException("Acidity must be between 0 and 14 for " + name);
        }
        return virtualFluid(name, still, flow, TintedFluid.create(color), p -> new Acid(p).acidity(acidity));
    }

    //ITEM
    public <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> factory, NonNullUnaryOperator<Item.Properties> properties, String... tags) {
        ItemBuilder<T, ?> builder = this.item(name, factory).properties(properties);
        for(String tag : tags) {
            builder.tag(AllTags.forgeItemTag(tag));
        }
        return builder.register();
    }
    
    public MDepositFeatureConfigEntry surfaceDeposit(String name, float frequency, int chance, Couple<NonNullSupplier<? extends Block>> surfaceDepositStones, NonNullSupplier<? extends Block> mineralStone, NonNullSupplier<? extends Block> deposit) {
        return createDeposit(name + "_surface_deposit", frequency, chance, 100, 320)
                .standardDatagenExt()
                .withBlocks(surfaceDepositStones, mineralStone, deposit)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
    
    public MDepositFeatureConfigEntry surfaceDeposit(String name, float frequency, int chance, Couple<NonNullSupplier<? extends Block>> surfaceDepositStones, NonNullSupplier<? extends Block> mineralStone, NonNullSupplier<? extends Block> deposit, TagKey<Biome> biomes, Dimension dimension) {
        return createDeposit(name + "_surface_deposit", frequency, chance, 100, 320)
                .standardDatagenExt()
                .withBlocks(surfaceDepositStones, mineralStone, deposit)
                .biomeTag(dimension.biomeTag())
                .biomeTag(biomes)
                .parent();
    }
    
    public MTypedDepositFeatureConfigEntry typedDeposit(String name, int maxHeight, int minHeight, int maxWidth, int minWidth, int maxDepth, int minDepth, float depositBlockChance, DepositCapacity capacity, float frequency, int chance, Couple<NonNullSupplier<? extends Block>> surfaceDepositStones, NonNullSupplier<? extends Block> mineralStone, NonNullSupplier<? extends Block> deposit, TagKey<Biome> biomes, Dimension dimension) {
        return createTypedDeposit("large_" + name + "_deposit", maxWidth, minWidth, maxDepth, minDepth, depositBlockChance, capacity, frequency, chance, minHeight, maxHeight)
                .standardDatagenExt()
                .withBlocks(surfaceDepositStones, mineralStone, deposit)
                .biomeTag(dimension.biomeTag())
                .biomeTag(biomes)
                .parent();
    }
    
    public enum Dimension {
        OVERWORLD(BiomeTags.IS_OVERWORLD),
        NETHER(BiomeTags.IS_NETHER),
        END(BiomeTags.IS_END);
        
        public final TagKey<Biome> biomeTag;
        
        Dimension(TagKey<Biome> biomeTag) {
            this.biomeTag = biomeTag;
        }
        
        public TagKey<Biome> biomeTag() {
            return biomeTag;
        }
    }
    
    public MaterialEntry material(String name, boolean richb) {
        return new MaterialEntry(this, name, richb);
    }
    
    public MetalEntry metal(String name, double meltingPoint, String element) {
        return new MetalEntry(this, name, meltingPoint, element);
    }
    

    public ItemEntry<Item> simpleItem(String name, String... tags) {
        return item(name, Item::new, p->p, tags);
    }

    public ItemEntry<MetallurgicaItem> metallurgicaItem(String name, String... tags) {
        return item(name, p -> new MetallurgicaItem(p), p->p, tags);
    }
    
    public ItemEntry<MetallurgicaItem> cluster(String name) {
        return this.item(name, p -> new MetallurgicaItem(p))
                .tag(AllTags.forgeItemTag("gem_clusters/" + name))
                .tag(AllTags.forgeItemTag("gem_clusters"))
                .lang(autoLang(name))
                .register();
    }

    public ItemEntry<MetallurgicaItem> raw(String name) {
        return this.item(name, p -> new MetallurgicaItem(p))
                .tag(AllTags.forgeItemTag("raw_materials/" + name))
                .tag(AllTags.forgeItemTag("raw_materials"))
                .lang(autoLang(name))
                .register();
    }

    public ItemEntry<Item> rubble(String name) {
        return this.item(name, Item::new)
                .tag(AllTags.forgeItemTag("material_rubble/" + name))
                .tag(AllTags.forgeItemTag("material_rubble"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<MetallurgicaItem> powder(String name) {
        return this.item(name, p -> new MetallurgicaItem(p))
                .tag(AllTags.forgeItemTag("powders/" + name))
                .tag(AllTags.forgeItemTag("powders"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloyItem(String name, String... tags) {
        return item(name, p -> new AlloyItem(p), p->p, tags);
    }
    public ItemEntry<AlloyItem> alloyNugget(String name) {
        return this.item(name, AlloyItem::new)
                .tag(AllTags.forgeItemTag("nuggets/" + name))
                .tag(AllTags.forgeItemTag("nuggets"))
                .tag(AllTags.forgeItemTag("alloy_nuggets/" + name))
                .tag(AllTags.forgeItemTag("alloy_nuggets"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloyDust(String name) {
        return this.item(name, AlloyItem::new)
                .tag(AllTags.forgeItemTag("dusts/" + name))
                .tag(AllTags.forgeItemTag("dusts"))
                .tag(AllTags.forgeItemTag("alloy_dusts/" + name))
                .tag(AllTags.forgeItemTag("alloy_dusts"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloySheet(String name) {
        return this.item(name, AlloyItem::new)
                .tag(AllTags.forgeItemTag("plates/" + name))
                .tag(AllTags.forgeItemTag("plates"))
                .tag(AllTags.forgeItemTag("alloy_sheets/" + name))
                .tag(AllTags.forgeItemTag("alloy_sheets"))
                .lang(autoLang(name))
                .register();
    }

    //BLOCK ENTITY
    public <T extends BlockEntity> BlockEntityEntry<T> blockEntity(
            String name,
            BlockEntityBuilder.BlockEntityFactory<T> factory,
            @Nullable BiFunction<MaterialManager, T, BlockEntityInstance<? super T>> instance,
            @Nullable NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>> renderer,
            BlockEntry<? extends Block>... blocks) {
        CreateBlockEntityBuilder<T, CreateRegistrate> builder = this.blockEntity(name, factory);
        if(instance != null) {
            builder = builder.instance(() -> instance);
        }
        NonNullSupplier<? extends Block>[] blocks2 = new NonNullSupplier[blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            BlockEntry<? extends Block> blockEntry = blocks[i];
            blocks2[i] = (NonNullSupplier<Block>) blockEntry::get;
        }
        BlockEntityBuilder<T, ?> builder2 = builder.validBlocks(blocks2);
        if (renderer != null) {
            builder2 = builder2.renderer(() -> renderer);
        }
        return builder2.register();
    }

    public <T extends BlockEntity> BlockEntityEntry<T> simpleBlockEntity(
            String name,
            BlockEntityBuilder.BlockEntityFactory<T> factory,
            BlockEntry<?>... blocks) {
        return blockEntity(name, factory, null, null, blocks);
    }

    public <T extends BlockEntity> BlockEntityEntry<T> simpleBlockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory, MetallurgicaOre[] blocks) {
        BlockEntry[] blocks2 = new BlockEntry[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blocks2[i] = blocks[i].MATERIAL.depositBlock();
        }
        return simpleBlockEntity(name, factory, blocks2);
    }

    //BLOCKS
    public BlockEntry<MineralDepositBlock> depositBlock(
            String name,
            ItemEntry<MetallurgicaItem> mineral
    ) {
        return this.block(name, MineralDepositBlock::new)
                .transform(MBuilderTransformers.mineralDeposit())
                .loot((p, bl) -> p.add(bl, RegistrateBlockLootTables.createSingleItemTable(Items.COBBLESTONE)
                        .withPool(RegistrateBlockLootTables.applyExplosionCondition(mineral.get(), LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0f, 5.0f))
                                .add(LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
                .lang(autoLang(name))
                .register();
    }

    public BlockEntry<Block> mineralBlock(
            String name,
            TagKey<Block> tag,
            ItemEntry<MetallurgicaItem> mineral
    ) {
        return this.block(name + "_rich_stone", Block::new)
                .transform(MBuilderTransformers.mineralStone(name))
                .loot(
                        (lt, bl) -> lt.add(bl,
                                RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                                        RegistrateBlockLootTables.applyExplosionDecay(bl, LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1)))
                                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
                .tag(tag)
                .lang(autoLang(name + "_rich_stone"))
                .register();
    }

    public <T extends Block> BlockEntry<T> simpleMachineBlock(
            String name,
            @Nullable String lang,
            NonNullFunction<BlockBehaviour.Properties, T> builder,
            SoundType sound,
            NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> blockstate) {
        BlockBuilder<T, CreateRegistrate> b = this.block(name, builder)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(sound))
                .transform(pickaxeOnly())
                .blockstate(blockstate)
                .addLayer(() -> RenderType::cutoutMipped)
                .simpleItem();
        b = lang != null ? b.lang(lang) : b;
        return b.register();
    }

    //MISC
    public static class TintedFluid extends AllFluids.TintedFluidType {
        public ResourceLocation still;
        public ResourceLocation flow;
        public int color;
        
        public TintedFluid(Properties properties, ResourceLocation still, ResourceLocation flow) {
            super(properties, still, flow);
        }
        
        public TintedFluid color(int color) {
            this.color = color;
            return this;
        }
        
        public TintedFluid still(ResourceLocation still) {
            this.still = still;
            return this;
        }
        
        public TintedFluid flow(ResourceLocation flow) {
            this.flow = flow;
            return this;
        }
        
        public static FluidBuilder.FluidTypeFactory create(int color) {
            return (properties, still, flow) -> new TintedFluid(properties, still, flow).color(color);
        }
        
        public static FluidBuilder.FluidTypeFactory create(int color, ResourceLocation still, ResourceLocation flow) {
            return (properties, still1, flow1) -> new TintedFluid(properties, still, flow).color(color).still(still).flow(flow);
        }
        
        @Override
        protected int getTintColor(FluidStack stack) {
            int c = color | 0xFF000000;
            return c;
        }
        
        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return NO_TINT;
        }
    }
}
