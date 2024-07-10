package com.freezedown.metallurgica.foundation;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.content.fluids.types.ReactiveGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasBlock;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockAndTintGetter;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

import static com.freezedown.metallurgica.Metallurgica.registrate;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class MetallurgicaRegistrate extends CreateRegistrate {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected MetallurgicaRegistrate(String modid) {
        super(modid);
    }
    
    public static MetallurgicaRegistrate create(String modid) {
        return new MetallurgicaRegistrate(modid);
    }
    
    public FluidBuilder<MoltenMetal.Flowing, CreateRegistrate> moltenMetal(String name) {
        return fluid(name, new ResourceLocation(getModid(), "fluid/" + name + "_still"), new ResourceLocation(getModid(), "fluid/" + name + "_flow"), MoltenMetal.MoltenMetalFluidType::new, MoltenMetal.Flowing::new);
    }
    
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
    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, float acidity) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return acid(name, color, still, flow, acidity);
    }

    public static FluidEntry<MoltenMetal.Flowing> createMoltenMetal(String name, String langName) {
        return Metallurgica.registrate.moltenMetal("molten_" + name).lang("Molten " + langName).source(MoltenMetal.Source::new).bucket().build().register();
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

    public <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> factory, NonNullUnaryOperator<Item.Properties> properties, String... tags) {
        ItemBuilder<T, ?> builder = this.item(name, factory).properties(properties);
        for(String tag : tags) {
            builder.tag(AllTags.forgeItemTag(tag));
        }
        return builder.register();
    }

    public MaterialEntry material(String name) {
        return new MaterialEntry(this, name);
    }

    public ItemEntry<Item> simpleItem(String name, String... tags) {
        return item(name, Item::new, p->p, tags);
    }

    public ItemEntry<MetallurgicaItem> metallurgicaItem(String name, String... tags) {
        return item(name, p -> new MetallurgicaItem(p).showElementComposition(), p->p, tags);
    }

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
