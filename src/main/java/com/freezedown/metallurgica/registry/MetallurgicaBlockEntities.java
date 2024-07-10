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
import com.freezedown.metallurgica.content.machines.electolizer.ElectrolyzerInstance;
import com.freezedown.metallurgica.content.machines.electolizer.ElectrolyzerRenderer;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorInstance;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorRenderer;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.freezedown.metallurgica.registry.MetallurgicaBlocks.*;
import static com.freezedown.metallurgica.Metallurgica.registrate;
import static com.freezedown.metallurgica.registry.MetallurgicaMaterials.*;

public class MetallurgicaBlockEntities {
    
    public static final BlockEntityEntry<ElectrolyzerBlockEntity> electrolyzer =
            registrate.blockEntity("electrolyzer", ElectrolyzerBlockEntity::new, ElectrolyzerInstance::new, ElectrolyzerRenderer::new, MetallurgicaBlocks.electrolyzer);
    
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator =
            registrate.blockEntity("drill_activator", DrillActivatorBlockEntity::new, DrillActivatorInstance::new, DrillActivatorRenderer::new, MetallurgicaBlocks.drillActivator);

    public static final BlockEntityEntry<DrillTowerDeployerBlockEntity> drillTowerDeployer =
            registrate.simpleBlockEntity("drill_tower_deployer", DrillTowerDeployerBlockEntity::new, MetallurgicaBlocks.drillTowerDeployer);

    public static final BlockEntityEntry<MineralDepositBlockEntity> mineralDeposit =
            registrate.simpleBlockEntity("mineral_deposit", MineralDepositBlockEntity::new,
                    magnetite.depositBlock(),
                    nativeCopper.depositBlock(),
                    magnetite.depositBlock(),
                    bauxite.depositBlock());
    
    public static final BlockEntityEntry<ChannelDepotBlockEntity> channelDepot =
            registrate.blockEntity("channel_depot", ChannelDepotBlockEntity::new, null, ChannelDepotRenderer::new, MetallurgicaBlocks.channelDepot);
    
    public static final BlockEntityEntry<ChannelBlockEntity> channel =
            registrate.blockEntity("channel", ChannelBlockEntity::new, null, ChannelRenderer::new, MetallurgicaBlocks.channelDepot);
    
    public static final BlockEntityEntry<FaucetBlockEntity> faucet =
            registrate.simpleBlockEntity("faucet", FaucetBlockEntity::new, MetallurgicaBlocks.faucet);
    
    public static final BlockEntityEntry<TuyereBlockEntity> tuyere =
            registrate.simpleBlockEntity("tuyere", TuyereBlockEntity::new, MetallurgicaBlocks.tuyere);
    
    public static final BlockEntityEntry<HearthBlockEntity> hearth =
            registrate.simpleBlockEntity("hearth", HearthBlockEntity::new, MetallurgicaBlocks.hearth);
    
    public static final BlockEntityEntry<CastingTableBlockEntity> castingTable =
            registrate.blockEntity("casting_table", CastingTableBlockEntity::new, null, CastingTableRenderer::new, MetallurgicaBlocks.castingTable);


    public static void register() {}
}
