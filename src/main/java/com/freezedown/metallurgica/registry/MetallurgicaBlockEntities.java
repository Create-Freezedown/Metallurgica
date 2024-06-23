package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.blast_furnace.hearth.HearthBlockEntity;
import com.freezedown.metallurgica.content.blast_furnace.tuyere.TuyereBlockEntity;
import com.freezedown.metallurgica.content.machines.blast_furnace.hearth.HearthBlockEntity;
import com.freezedown.metallurgica.content.machines.blast_furnace.tuyere.TuyereBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelRenderer;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotRenderer;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlockEntity;
import com.freezedown.metallurgica.content.forging.advanced_casting.CastingTableBlockEntity;
import com.freezedown.metallurgica.content.forging.advanced_casting.CastingTableRenderer;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorInstance;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorRenderer;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.google.common.collect.ImmutableSet;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.freezedown.metallurgica.Metallurgica.registrate;

public class MetallurgicaBlockEntities {
    
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator = registrate
            .blockEntity("drill_activator", DrillActivatorBlockEntity::new)
            .instance(() -> DrillActivatorInstance::new)
            .validBlocks(MetallurgicaBlocks.drillActivator)
            .renderer(() -> DrillActivatorRenderer::new)
            .register();
    
    public static final BlockEntityEntry<MineralDepositBlockEntity> mineralDeposit = registrate
            .blockEntity("mineral_deposit", MineralDepositBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.magnetiteDeposit, MetallurgicaBlocks.nativeCopperDeposit)
            .register();
    
    public static final BlockEntityEntry<ChannelDepotBlockEntity> channelDepot = registrate
            .blockEntity("channel_depot", ChannelDepotBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.channelDepot)
            .renderer(() -> ChannelDepotRenderer::new)
            .register();
    
    public static final BlockEntityEntry<ChannelBlockEntity> channel = registrate
            .blockEntity("channel", ChannelBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.channelDepot)
            .renderer(() -> ChannelRenderer::new)
            .register();
    
    public static final BlockEntityEntry<FaucetBlockEntity> faucet = registrate
            .blockEntity("faucet", FaucetBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.faucet)
            .register();
    
    public static final BlockEntityEntry<TuyereBlockEntity> tuyere = registrate
            .blockEntity("tuyere", TuyereBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.tuyere)
            .register();
    
    public static final BlockEntityEntry<HearthBlockEntity> hearth = registrate
            .blockEntity("hearth", HearthBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.hearth)
            .register();
    
    public static final BlockEntityEntry<CastingTableBlockEntity> castingTable = registrate
            .blockEntity("casting_table", CastingTableBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.castingTable)
            .renderer(() -> CastingTableRenderer::new)
            .register();
    
    public static void register() {}
}
