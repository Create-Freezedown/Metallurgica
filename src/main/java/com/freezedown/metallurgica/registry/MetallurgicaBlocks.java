package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
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
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlock;
import com.freezedown.metallurgica.foundation.MBuilderTransformers;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class MetallurgicaBlocks {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
    
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
            .item()
            .build()
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
            .item()
            .build()
            .lang("Drill Expansion")
            .register();
    public static final BlockEntry<MineralDepositBlock> magnetiteDeposit = registrate.block("magnetite_deposit", MineralDepositBlock::new)
            .transform(MBuilderTransformers.mineralDeposit())
            .loot((p, bl) -> p.add(bl, RegistrateBlockLootTables.createSingleItemTable(Items.COBBLESTONE)
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(MetallurgicaItems.magnetite.get(), LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                            .add(LootItem.lootTableItem(MetallurgicaItems.magnetite.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
            .register();
    public static final BlockEntry<Block> magnetiteStone = registrate.block("magnetite_rich_stone", Block::new)
            .transform(MBuilderTransformers.mineralStone("magnetite"))
            .loot((lt, bl) -> lt.add(bl,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                            RegistrateBlockLootTables.applyExplosionDecay(bl, LootItem.lootTableItem(MetallurgicaItems.magnetite.get()).apply(LimitCount.limitCount(IntRange.range(0, 1)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_MAGNETITE.tag)
            .register();
    public static final BlockEntry<MineralDepositBlock> nativeCopperDeposit = registrate.block("native_copper_deposit", MineralDepositBlock::new)
            .transform(MBuilderTransformers.mineralDeposit())
            .loot((p, bl) -> p.add(bl, RegistrateBlockLootTables.createSingleItemTable(Items.COBBLESTONE)
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(MetallurgicaItems.nativeCopper.get(), LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                            .add(LootItem.lootTableItem(MetallurgicaItems.nativeCopper.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
            .register();
    public static final BlockEntry<Block> nativeCopperStone = registrate.block("native_copper_rich_stone", Block::new)
            .transform(MBuilderTransformers.mineralStone("native_copper"))
            .loot((lt, bl) -> lt.add(bl,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                            RegistrateBlockLootTables.applyExplosionDecay(bl, LootItem.lootTableItem(MetallurgicaItems.nativeCopper.get()).apply(LimitCount.limitCount(IntRange.range(0, 1)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_NATIVE_COPPER.tag)
            .register();
    public static final BlockEntry<MineralDepositBlock> bauxiteDeposit = registrate.block("bauxite_deposit", MineralDepositBlock::new)
            .transform(MBuilderTransformers.mineralDeposit())
            .loot((p, bl) -> p.add(bl, RegistrateBlockLootTables.createSingleItemTable(TFMGPaletteStoneTypes.BAUXITE.getBaseBlock().get().asItem())
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(MetallurgicaItems.bauxite.get(), LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                            .add(LootItem.lootTableItem(MetallurgicaItems.bauxite.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
            .register();
    public static final BlockEntry<Block> bauxiteStone = registrate.block("bauxite_rich_stone", Block::new)
            .transform(MBuilderTransformers.mineralStone("bauxite"))
            .loot((lt, bl) -> lt.add(bl,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                            RegistrateBlockLootTables.applyExplosionDecay(bl, LootItem.lootTableItem(MetallurgicaItems.bauxite.get()).apply(LimitCount.limitCount(IntRange.range(0, 1)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_BAUXITE.tag)
            .register();
    public static final BlockEntry<MineralDepositBlock> nativeGoldDeposit = registrate.block("native_gold_deposit", MineralDepositBlock::new)
            .transform(MBuilderTransformers.mineralDeposit())
            .loot((p, bl) -> p.add(bl, RegistrateBlockLootTables.createSingleItemTable(Items.COBBLESTONE)
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(MetallurgicaItems.nativeGold.get(), LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                            .add(LootItem.lootTableItem(MetallurgicaItems.nativeGold.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
            .register();
    public static final BlockEntry<Block> nativeGoldStone = registrate.block("native_gold_rich_stone", Block::new)
            .transform(MBuilderTransformers.mineralStone("native_gold"))
            .loot((lt, bl) -> lt.add(bl,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                            RegistrateBlockLootTables.applyExplosionDecay(bl, LootItem.lootTableItem(MetallurgicaItems.nativeGold.get()).apply(LimitCount.limitCount(IntRange.range(0, 1)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_NATIVE_GOLD.tag)
            .register();
    public static final BlockEntry<ChannelDepotBlock> channelDepot = registrate.block("channel_depot", ChannelDepotBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.LODESTONE))
            .transform(pickaxeOnly())
            .blockstate(new ChannelDepotGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();
    
    public static final BlockEntry<ChannelBlock> channel = registrate.block("channel", ChannelBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.LODESTONE))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(new ChannelGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();
    
    public static final BlockEntry<FaucetBlock> faucet = registrate.block("faucet", FaucetBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.LODESTONE))
            .transform(pickaxeOnly())
            .blockstate(new FaucetGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();
    
    public static final BlockEntry<TuyereBlock> tuyere = registrate.block("tuyere", TuyereBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.COPPER))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .lang("Tuyere")
            .register();
    public static final BlockEntry<HearthBlock> hearth = registrate.block("hearth", HearthBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.COPPER))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models().getExistingFile(p.modLoc("block/hearth"))))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .lang("Hearth")
            .register();
    public static final BlockEntry<Block> carbonBrick = registrate.block("carbon_brick", Block::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE_BRICKS))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .lang("Carbon Brick")
            .register();
    
    public static final BlockEntry<CastingTable> castingTable = registrate.block("casting_table", CastingTable::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE_BRICKS))
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/casting_table"))))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();
    public static void register() {
    
    }
}
