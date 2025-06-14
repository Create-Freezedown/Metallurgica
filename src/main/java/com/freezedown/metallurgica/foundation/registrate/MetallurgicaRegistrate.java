package com.freezedown.metallurgica.foundation.registrate;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.*;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasBlock;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.MBuilderTransformers;
import com.freezedown.metallurgica.foundation.fluid.MaterialFluidType;
import com.freezedown.metallurgica.foundation.fluid.MoltenMetalFluid;
import com.freezedown.metallurgica.foundation.fluid.VirtualMaterialFluid;
import com.freezedown.metallurgica.foundation.item.AlloyItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.foundation.material.OreEntry;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.conductor.ConductorBuilder;
import com.freezedown.metallurgica.infastructure.element.Element;
import com.freezedown.metallurgica.infastructure.element.ElementBuilder;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.freezedown.metallurgica.registry.MetallurgicaSpriteShifts;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.block.connected.RotatedPillarCTBehaviour;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
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
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class MetallurgicaRegistrate extends CreateRegistrate {
    /**
     * A list of all compositions that have been registered.
     * <p>
     * This is used to generate the default lang file and tooltips.
     * * <p>
     * The key is the name of the material, the value is the element composition.
     */
    public static final List<String> COMP_MOD_BLACKLIST = new ArrayList<>();

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
        return fluid(name, still, flow, TransparentTintedFluidType.create(color), FlowingGas.Flowing::new).source(FlowingGas.Source::new).block(GasBlock::new).build();
    }
    
    public FluidBuilder<ReactiveGas.Flowing, CreateRegistrate> reactiveGas(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TransparentTintedFluidType.create(color), ReactiveGas.Flowing::new).source(ReactiveGas.Source::new).block(GasBlock::new).build();
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
        return virtualFluid(name, still, flow, TransparentTintedFluidType.create(color), VirtualFluid::createSource, VirtualFluid::createFlowing);
    }

    public FluidBuilder<VirtualMaterialFluid, CreateRegistrate> materialVirtualFluid(String name, ResourceLocation still, ResourceLocation flow, Material material, FluidFlag flag) {
        return entry(name, c -> new VirtualFluidBuilder<>(self(), self(), name, c, still, flow, MaterialFluidType.create(material), (p) -> VirtualMaterialFluid.createSource(p, material, flag), (p) -> VirtualMaterialFluid.createFlowing(p, material, flag)));
    }

    public FluidBuilder<VirtualMaterialFluid, CreateRegistrate> materialVirtualFluid(String name, ResourceLocation still, ResourceLocation flow, Material material, FluidFlag flag, boolean tint) {
        return entry(name, c -> new VirtualFluidBuilder<>(self(), self(), name, c, still, flow, MaterialFluidType.create(material, tint), (p) -> VirtualMaterialFluid.createSource(p, material, flag), (p) -> VirtualMaterialFluid.createFlowing(p, material, flag)));
    }

    public FluidBuilder<MoltenMetalFluid, CreateRegistrate> moltenMetal(String name, Material material, FluidFlag flag, double moltenTemperature) {
        ResourceLocation still = Metallurgica.asResource("fluid/molten_metal_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/molten_metal_flow");
        return entry(name, c -> new VirtualFluidBuilder<>(self(), self(), name, c, still, flow,
                MaterialFluidType.create(material, false),
                (p) -> MoltenMetalFluid.createSource(p, material, flag).meltingPoint(moltenTemperature),
                (p) -> MoltenMetalFluid.createFlowing(p, material, flag).meltingPoint(moltenTemperature)));
    }
    
    public FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> tintedFluid(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TransparentTintedFluidType.create(color), ForgeFlowingFluid.Flowing::new);
    }
    
    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, float acidity) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return acid(name, color, still, flow, acidity);
    }

    public FluidEntry<Acid> acid(String name, int color, float acidity, String lang) {
        return Metallurgica.registrate.acid(name, color, acidity).lang(lang).register();
    }

    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, ResourceLocation still, ResourceLocation flow, float acidity) {
        if (acidity > 14 || acidity < 0) {
            throw new IllegalArgumentException("Acidity must be between 0 and 14 for " + name);
        }
        return virtualFluid(name, still, flow, TransparentTintedFluidType.create(color), (p) -> Acid.createSource(p).acidity(acidity), (p) -> Acid.createFlowing(p).acidity(acidity));
    }

    //ITEM
    public <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> factory, NonNullUnaryOperator<Item.Properties> properties, String... tags) {
        ItemBuilder<T, ?> builder = this.item(name, factory).properties(properties);
        for(String tag : tags) {
            builder.tag(AllTags.forgeItemTag(tag));
        }
        return builder.register();
    }

    public <T extends Element> ElementBuilder<T, MetallurgicaRegistrate> element(String symbol, NonNullFunction<Element.Properties, T> factory) {
        return element((MetallurgicaRegistrate) self(), symbol, factory);
    }

    public <T extends Element> ElementBuilder<T, MetallurgicaRegistrate> element(String name, String symbol, NonNullFunction<Element.Properties, T> factory) {
        return element((MetallurgicaRegistrate) self(), name, symbol, factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, String symbol, NonNullFunction<Element.Properties, T> factory) {
        return element(parent, currentName(), symbol, factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, String name, String symbol, NonNullFunction<Element.Properties, T> factory) {
        return entry(name, callback -> ElementBuilder.create(this, parent, name, symbol, callback, factory));
    }

    public <T extends Conductor> ConductorBuilder<T, MetallurgicaRegistrate> conductor(NonNullFunction<Conductor.Properties, T> factory) {
        return conductor((MetallurgicaRegistrate) self(), factory);
    }

    public <T extends Conductor> ConductorBuilder<T, MetallurgicaRegistrate> conductor(String name, NonNullFunction<Conductor.Properties, T> factory) {
        return conductor((MetallurgicaRegistrate) self(), name, factory);
    }

    public <T extends Conductor, P> ConductorBuilder<T, P> conductor(P parent, NonNullFunction<Conductor.Properties, T> factory) {
        return conductor(parent, currentName(), factory);
    }

    public <T extends Conductor, P> ConductorBuilder<T, P> conductor(P parent, String name, NonNullFunction<Conductor.Properties, T> factory) {
        return entry(name, callback -> ConductorBuilder.create(this, parent, name, callback, factory));
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
    
    public OreEntry material(String name, boolean richb) {
        return new OreEntry(this, name, richb, false);
    }

    public OreEntry material(String name, boolean richb, boolean sideTop) {
        return new OreEntry(this, name, richb, sideTop);
    }
    

    public ItemEntry<Item> simpleItem(String name, String... tags) {
        return item(name, Item::new, p->p, tags);
    }

    public ItemEntry<MetallurgicaItem> metallurgicaItem(String name, String... tags) {
        return item(name, MetallurgicaItem::new, p->p, tags);
    }
    
    public ItemEntry<MetallurgicaItem> cluster(String name) {
        return this.item(name, MetallurgicaItem::new)
                .tag(AllTags.forgeItemTag("gem_clusters/" + name))
                .tag(AllTags.forgeItemTag("gem_clusters"))
                .lang(autoLang(name))
                .register();
    }

    public ItemEntry<MetallurgicaItem> raw(String name) {
        return this.item(name, MetallurgicaItem::new)
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
        return this.item(name, MetallurgicaItem::new)
                .tag(AllTags.forgeItemTag("powders/" + name))
                .tag(AllTags.forgeItemTag("powders"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloyItem(String name, String... tags) {
        return item(name, AlloyItem::new, p->p, tags);
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
    @Override
    public <T extends BlockEntity> @NotNull CreateBlockEntityBuilder<T, CreateRegistrate> blockEntity(String name,
                                                                                                      BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(self(), name, factory);
    }

    @Override
    public <T extends BlockEntity, P> @NotNull CreateBlockEntityBuilder<T, P> blockEntity(P parent, String name,
                                                                                          BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return (CreateBlockEntityBuilder<T, P>) entry(name,
                (callback) -> CreateBlockEntityBuilder.create(this, parent, name, callback, factory));
    }

    public <T extends BlockEntity> BlockEntityBuilder<T, CreateRegistrate> simpleBlockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory, MetallurgicaOre[] blocks) {
        BlockEntityBuilder<T, CreateRegistrate> builder = blockEntity(self(), name, factory);
        for (MetallurgicaOre ore : blocks) {
            builder.validBlock(ore.ORE.depositBlock());
        }
        return builder;
    }

    //BLOCKS
    public BlockEntry<MineralDepositBlock> depositBlock(String name, ItemEntry<MetallurgicaItem> mineral) {
        return this.block(name, MineralDepositBlock::new)
                .transform(MBuilderTransformers.mineralDeposit())
                .loot((lt, bl) -> lt.add(bl, lt.createSingleItemTable(Items.COBBLESTONE)
                        .withPool(lt.applyExplosionCondition(mineral.get(), LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0f, 5.0f))
                                .add(LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
                .lang(autoLang(name))
                .register();
    }

    public BlockEntry<MineralDepositBlock> depositBlock(String name, ItemEntry<MetallurgicaItem> mineral, boolean sideTop) {
        if (!sideTop)
            return depositBlock(name, mineral);
        return this.block(name, MineralDepositBlock::new)
                .transform(MBuilderTransformers.mineralDepositSideTop())
                .loot((lt, bl) -> lt.add(bl, lt.createSingleItemTable(Items.COBBLESTONE)
                        .withPool(lt.applyExplosionCondition(mineral.get(), LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0f, 5.0f))
                                .add(LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
                .lang(autoLang(name))
                .register();
    }

    public BlockEntry<Block> mineralBlock(String name, TagKey<Block> tag, ItemEntry<MetallurgicaItem> mineral) {
        return this.block(name + "_rich_stone", Block::new)
                .transform(MBuilderTransformers.mineralStone(name))
                .loot((lt, bl) -> lt.add(bl, RegistrateBlockLootTables.createSilkTouchDispatchTable(bl, lt.applyExplosionDecay(bl, LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
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
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(sound))
                .transform(pickaxeOnly())
                .blockstate(blockstate)
//                .addLayer(() -> RenderType::cutoutMipped)
                .simpleItem();
        b = lang != null ? b.lang(lang) : b;
        return b.register();
    }

    public <T extends ConnectedPillarBlock> BlockEntry<T> directionalMetalBlock(
            String name,
            String lang,
            NonNullFunction<BlockBehaviour.Properties, T> builder,
            SoundType sound,
            NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> blockstate
    ) {
        BlockBuilder<T, CreateRegistrate> b = this.block(name, builder)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(sound))
                .blockstate(blockstate)
                .onRegister(connectedTextures(() -> new RotatedPillarCTBehaviour(MetallurgicaSpriteShifts.directionalMetalBlock, MetallurgicaSpriteShifts.directionalMetalBlock)))
                .simpleItem();
        b = b.lang(lang);
        return b.register();
    }
}
