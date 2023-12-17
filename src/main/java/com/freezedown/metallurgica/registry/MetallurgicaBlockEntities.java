package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelRenderer;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotRenderer;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlockEntity;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorInstance;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorRenderer;
import com.freezedown.metallurgica.content.mineral.drill.drill_display.DrillDisplayBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerInstance;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerRenderer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.freezedown.metallurgica.Metallurgica.registrate;
import static com.simibubi.create.Create.REGISTRATE;

public class MetallurgicaBlockEntities {
    
    public static final BlockEntityEntry<DrillTowerBlockEntity> drillTower = registrate
            .blockEntity("drill_tower", DrillTowerBlockEntity::new)
            .instance(() -> DrillTowerInstance::new)
            .validBlocks(MetallurgicaBlocks.drillTower)
            .renderer(() -> DrillTowerRenderer::new)
            .register();
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator = registrate
            .blockEntity("drill_activator", DrillActivatorBlockEntity::new)
            .instance(() -> DrillActivatorInstance::new)
            .validBlocks(MetallurgicaBlocks.drillActivator)
            .renderer(() -> DrillActivatorRenderer::new)
            .register();
    public static final BlockEntityEntry<DrillDisplayBlockEntity> drillDisplay = registrate
            .blockEntity("drill_display", DrillDisplayBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.drillDisplay)
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
    public static void register() {}
}
