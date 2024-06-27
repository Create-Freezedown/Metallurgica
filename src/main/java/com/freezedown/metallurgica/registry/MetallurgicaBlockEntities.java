package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.content.machines.blast_furnace.hearth.HearthBlockEntity;
import com.freezedown.metallurgica.content.machines.blast_furnace.tuyere.TuyereBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelRenderer;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlockEntity;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotRenderer;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlockEntity;
import com.freezedown.metallurgica.content.forging.advanced_casting.CastingTableBlockEntity;
import com.freezedown.metallurgica.content.forging.advanced_casting.CastingTableRenderer;
import com.freezedown.metallurgica.content.machines.electolizer.ElectrolyzerBlockEntity;
import com.freezedown.metallurgica.content.machines.electolizer.ElectrolyzerRenderer;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorInstance;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorRenderer;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.freezedown.metallurgica.Metallurgica.registrate;

public class MetallurgicaBlockEntities {
    
    public static final BlockEntityEntry<ElectrolyzerBlockEntity> electrolyzer = registrate
            .blockEntity("electrolizer", ElectrolyzerBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.electrolyzer)
            .renderer(() -> ElectrolyzerRenderer::new)
            .register();
    
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator = registrate
            .blockEntity("drill_activator", DrillActivatorBlockEntity::new)
            .instance(() -> DrillActivatorInstance::new)
            .validBlocks(MetallurgicaBlocks.drillActivator)
            .renderer(() -> DrillActivatorRenderer::new)
            .register();
    public static final BlockEntityEntry<DrillTowerDeployerBlockEntity> drillTowerDeployer = registrate
            .blockEntity("drill_tower_deployer", DrillTowerDeployerBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.drillTowerDeployer)
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
