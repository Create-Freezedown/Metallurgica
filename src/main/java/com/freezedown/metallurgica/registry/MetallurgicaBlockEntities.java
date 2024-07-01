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
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

import static com.freezedown.metallurgica.Metallurgica.registrate;

public class MetallurgicaBlockEntities {
    
    public static final BlockEntityEntry<ElectrolyzerBlockEntity> electrolyzer =
            blockEntity("electrolyzer", ElectrolyzerBlockEntity::new, ElectrolyzerInstance::new, ElectrolyzerRenderer::new, MetallurgicaBlocks.electrolyzer);
    
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator =
            blockEntity("drill_activator", DrillActivatorBlockEntity::new, DrillActivatorInstance::new, DrillActivatorRenderer::new, MetallurgicaBlocks.drillActivator);

    public static final BlockEntityEntry<DrillTowerDeployerBlockEntity> drillTowerDeployer =
            simpleBlockEntity("drill_tower_deployer", DrillTowerDeployerBlockEntity::new, MetallurgicaBlocks.drillTowerDeployer);

    public static final BlockEntityEntry<MineralDepositBlockEntity> mineralDeposit =
            simpleBlockEntity("mineral_deposit", MineralDepositBlockEntity::new, MetallurgicaBlocks.magnetiteDeposit, MetallurgicaBlocks.nativeCopperDeposit);
    
    public static final BlockEntityEntry<ChannelDepotBlockEntity> channelDepot =
            blockEntity("channel_depot", ChannelDepotBlockEntity::new, null, ChannelDepotRenderer::new, MetallurgicaBlocks.channelDepot);
    
    public static final BlockEntityEntry<ChannelBlockEntity> channel =
            blockEntity("channel", ChannelBlockEntity::new, null, ChannelRenderer::new, MetallurgicaBlocks.channelDepot);
    
    public static final BlockEntityEntry<FaucetBlockEntity> faucet =
            simpleBlockEntity("faucet", FaucetBlockEntity::new, MetallurgicaBlocks.faucet);
    
    public static final BlockEntityEntry<TuyereBlockEntity> tuyere =
            simpleBlockEntity("tuyere", TuyereBlockEntity::new, MetallurgicaBlocks.tuyere);
    
    public static final BlockEntityEntry<HearthBlockEntity> hearth =
            simpleBlockEntity("hearth", HearthBlockEntity::new, MetallurgicaBlocks.hearth);
    
    public static final BlockEntityEntry<CastingTableBlockEntity> castingTable =
            blockEntity("casting_table", CastingTableBlockEntity::new, null, CastingTableRenderer::new, MetallurgicaBlocks.castingTable);

    private static <T extends BlockEntity> BlockEntityEntry<T> blockEntity(
            String name,
            BlockEntityBuilder.BlockEntityFactory<T> factory,
            @Nullable BiFunction<MaterialManager, T, BlockEntityInstance<? super T>> instance,
            @Nullable NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>> renderer,
            BlockEntry<?>... blocks) {
        CreateBlockEntityBuilder<T, CreateRegistrate> builder = registrate.blockEntity(name, factory);
        if(instance != null) {
            builder = builder.instance(() -> instance);
        }
        BlockEntityBuilder<T, CreateRegistrate> builder2 = builder.validBlocks(blocks);
        if (renderer != null) {
            builder2 = builder2.renderer(() -> renderer);
        }
        return builder2.register();
    }

    private static <T extends BlockEntity> BlockEntityEntry<T> simpleBlockEntity(
            String name,
            BlockEntityBuilder.BlockEntityFactory<T> factory,
            BlockEntry<?>... blocks) {
        return blockEntity(name, factory, null, null, blocks);
    }

    public static void register() {}
}
