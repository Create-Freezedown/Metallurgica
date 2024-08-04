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
import com.freezedown.metallurgica.content.machines.reverbaratory.ReverbaratoryBlockEntity;
import com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment.HeaterSegmentBlockEntity;
import com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment.HeaterSegmentInstance;
import com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment.HeaterSegmentRenderer;
import com.freezedown.metallurgica.content.machines.rotary_kiln.segment.RotaryKilnSegmentBlockEntity;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableBlockEntity;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableInstance;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableRenderer;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorInstance;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorRenderer;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlockEntity;
import com.freezedown.metallurgica.content.primitive.log_pile.charred_pile.CharredLogPileBlockEntity;
import com.freezedown.metallurgica.foundation.multiblock.FluidOutputBlockEntity;
import com.simibubi.create.content.kinetics.base.CutoutRotatingInstance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.freezedown.metallurgica.Metallurgica.registrate;
import static com.freezedown.metallurgica.registry.MetallurgicaMaterials.*;

public class MetallurgicaBlockEntities {
    
    public static final BlockEntityEntry<RotaryKilnSegmentBlockEntity> rotaryKilnSegment = registrate.blockEntity("rotary_kiln_segment", RotaryKilnSegmentBlockEntity::new)
            .instance(() -> CutoutRotatingInstance::new, false)
            .validBlocks(MetallurgicaBlocks.rotaryKilnSegment)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .register();
    
    public static final BlockEntityEntry<HeaterSegmentBlockEntity> heaterSegment = registrate.blockEntity("heater_segment", HeaterSegmentBlockEntity::new)
            .instance(() -> HeaterSegmentInstance::new, false)
            .validBlocks(MetallurgicaBlocks.heaterSegment)
            .renderer(() -> HeaterSegmentRenderer::new)
            .register();
    
    public static final BlockEntityEntry<ElectrolyzerBlockEntity> electrolyzer =
            registrate.blockEntity("electrolyzer", ElectrolyzerBlockEntity::new, ElectrolyzerInstance::new, ElectrolyzerRenderer::new, MetallurgicaBlocks.electrolyzer);
    
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator =
            registrate.blockEntity("drill_activator", DrillActivatorBlockEntity::new, DrillActivatorInstance::new, DrillActivatorRenderer::new, MetallurgicaBlocks.drillActivator);

    public static final BlockEntityEntry<DrillTowerDeployerBlockEntity> drillTowerDeployer =
            registrate.simpleBlockEntity("drill_tower_deployer", DrillTowerDeployerBlockEntity::new, MetallurgicaBlocks.drillTowerDeployer);

    public static final BlockEntityEntry<MineralDepositBlockEntity> mineralDeposit =
            registrate.simpleBlockEntity("mineral_deposit", MineralDepositBlockEntity::new, values());
    
    public static final BlockEntityEntry<ChannelDepotBlockEntity> channelDepot =
            registrate.blockEntity("channel_depot", ChannelDepotBlockEntity::new, null, ChannelDepotRenderer::new, MetallurgicaBlocks.channelDepot);
    
    public static final BlockEntityEntry<ChannelBlockEntity> channel =
            registrate.blockEntity("channel", ChannelBlockEntity::new, null, ChannelRenderer::new, MetallurgicaBlocks.channelDepot);
    
    public static final BlockEntityEntry<FaucetBlockEntity> faucet =
            registrate.simpleBlockEntity("faucet", FaucetBlockEntity::new, MetallurgicaBlocks.faucet);
    
    public static final BlockEntityEntry<TuyereBlockEntity> tuyere =
            registrate.simpleBlockEntity("tuyere", TuyereBlockEntity::new, MetallurgicaBlocks.tuyere);
    
    public static final BlockEntityEntry<ReverbaratoryBlockEntity> reverbaratory = registrate
            .blockEntity("reverbaratory", ReverbaratoryBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.reverbaratory)
            .register();

    public static final BlockEntityEntry<FluidOutputBlockEntity> fluidOutput = registrate
            .blockEntity("fluid_output", FluidOutputBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.fluidOutput)
            .register();
    
    public static final BlockEntityEntry<ShakingTableBlockEntity> shakingTable =
            registrate.blockEntity("shaking_table", ShakingTableBlockEntity::new, ShakingTableInstance::new, ShakingTableRenderer::new, MetallurgicaBlocks.shakingTable);



    public static final BlockEntityEntry<HearthBlockEntity> hearth =
            registrate.simpleBlockEntity("hearth", HearthBlockEntity::new, MetallurgicaBlocks.hearth);

    public static final BlockEntityEntry<CastingTableBlockEntity> castingTable =
            registrate.blockEntity("casting_table", CastingTableBlockEntity::new, null, CastingTableRenderer::new, MetallurgicaBlocks.castingTable);
    
    public static final BlockEntityEntry<CharredLogPileBlockEntity> charredLogPile =
            registrate.simpleBlockEntity("charred_log_pile", CharredLogPileBlockEntity::new, MetallurgicaBlocks.charredLogPile);

    public static void register() {}
}
