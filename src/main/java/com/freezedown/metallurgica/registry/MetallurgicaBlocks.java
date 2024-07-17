package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.machines.blast_furnace.hearth.HearthBlock;
import com.freezedown.metallurgica.content.machines.blast_furnace.tuyere.TuyereBlock;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelBlock;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelGenerator;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlock;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotGenerator;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlock;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetGenerator;
import com.freezedown.metallurgica.content.forging.advanced_casting.CastingTable;
import com.freezedown.metallurgica.content.machines.electolizer.ElectrolyzerBlock;
import com.freezedown.metallurgica.content.machines.reverbaratory.ReverbaratoryBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlock;
import com.freezedown.metallurgica.foundation.MBuilderTransformers;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.multiblock.FluidOutputBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class MetallurgicaBlocks {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);

    //MACHINES
    public static final BlockEntry<ReverbaratoryBlock> reverbaratory = registrate.block("reverbaratory", ReverbaratoryBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();

    public static final BlockEntry<FluidOutputBlock> fluidOutput = registrate.block("fluid_output", FluidOutputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/reverbaratory/input_output"))))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();

    public static final BlockEntry<ElectrolyzerBlock> electrolyzer = registrate.block("electrolyzer", ElectrolyzerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(TagGen.pickaxeOnly())
            .transform(BlockStressDefaults.setImpact(8.0))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();
    
    public static final BlockEntry<DrillActivatorBlock> drillActivator = registrate.block("drill_activator", DrillActivatorBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .transform(BlockStressDefaults.setImpact(6.0))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_activator/block"))))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DrillTowerDeployerBlock> drillTowerDeployer = registrate.block("drill_tower_deployer", DrillTowerDeployerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_tower_deployer/block"))))
            .simpleItem()
            .register();

    public static final BlockEntry<DrillTowerBlock> drillTower = registrate.block("drill_tower", DrillTowerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TagGen.pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_tower/block"))))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<Block> drillExpansion = registrate.block("drill_expansion", Block::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(MetallurgicaSpriteShifts.drillExpansion)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, MetallurgicaSpriteShifts.drillExpansion)))
            .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                    .cubeColumn(c.getName(), p.modLoc("block/" + c.getName()), p.modLoc("block/" + c.getName() + "_end"))))
            .simpleItem()
            .lang("Drill Expansion")
            .register();

    //SAND
    public static final BlockEntry<SandBlock> magnetiteSand = registrate.block("magnetite_sand", p -> new SandBlock(0x8D8388, p))
            .loot(BlockLoot::dropSelf)
            .simpleItem()
            .register();
    
    public static final BlockEntry<SandBlock> magnetiteTracedSand = registrate.block("magnetite_traced_sand", p -> new SandBlock(0x8D8388, p))
            .transform(MBuilderTransformers.mineralStone("magnetite_traced_sand"))
            .loot((p, bl) -> p.add(bl,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                            RegistrateBlockLootTables.applyExplosionDecay(bl, LootItem.lootTableItem(Items.SAND)))))
            .register();

    //SIMPLE MACHINES
    public static final BlockEntry<ChannelDepotBlock>
            channelDepot =             registrate.simpleMachineBlock("channel_depot", null, ChannelDepotBlock::new, SoundType.LODESTONE, new ChannelDepotGenerator()::generate);
    
    public static final BlockEntry<ChannelBlock>
            channel =                  registrate.simpleMachineBlock("channel", null, ChannelBlock::new, SoundType.LODESTONE, new ChannelGenerator()::generate);
    //TODO
    //.properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
    // .properties(BlockBehaviour.Properties::noOcclusion)
    
    public static final BlockEntry<FaucetBlock>
            faucet =                   registrate.simpleMachineBlock("faucet", null, FaucetBlock::new, SoundType.LODESTONE, new FaucetGenerator()::generate);
    
    public static final BlockEntry<TuyereBlock>
            tuyere =                   registrate.simpleMachineBlock("tuyere", "Tuyere", TuyereBlock::new, SoundType.COPPER, (c, p) -> p.simpleBlock(c.get()));

    public static final BlockEntry<HearthBlock>
            hearth =                   registrate.simpleMachineBlock("hearth", "Hearth", HearthBlock::new, SoundType.COPPER, (c, p) -> p.horizontalBlock(c.get(), p.models().getExistingFile(p.modLoc("block/hearth"))));

    public static final BlockEntry<Block>
            carbonBrick =              registrate.simpleMachineBlock("carbon_brick", "Carbon Brick", Block::new, SoundType.DEEPSLATE_BRICKS, (c, p) -> p.simpleBlock(c.get()));

    public static final BlockEntry<CastingTable>
            castingTable =             registrate.simpleMachineBlock("casting_table", null, CastingTable::new, SoundType.DEEPSLATE_BRICKS, (c, p) -> p.simpleBlock(c.getEntry(), p.models().getExistingFile(p.modLoc("block/casting_table"))));


    public static void register() {}
}
