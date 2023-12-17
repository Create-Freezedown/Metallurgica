package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelBlock;
import com.freezedown.metallurgica.content.fluids.channel.channel.ChannelGenerator;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlock;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotGenerator;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_display.DrillDisplayBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.ModelGen;
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
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class MetallurgicaBlocks {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
    
    public static final BlockEntry<DrillActivatorBlock> drillActivator = registrate.block("drill_activator", DrillActivatorBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_activator/block"))))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<DrillDisplayBlock> drillDisplay = registrate.block("drill_display", DrillDisplayBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
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
    
    public static final BlockEntry<MineralDepositBlock> magnetiteDeposit = registrate.block("magnetite_deposit", MineralDepositBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .loot((p, b) -> p.add(b, RegistrateBlockLootTables.createSingleItemTable(Items.COBBLESTONE)
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(MetallurgicaItems.magnetite.get(), LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                            .add(LootItem.lootTableItem(MetallurgicaItems.magnetite.get()))))))
            .simpleItem()
            .register();
    
    public static final BlockEntry<Block> magnetiteStone = registrate.block("magnetite_rich_stone", Block::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(MetallurgicaItems.magnetite.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .blockstate(BlockStateGen.naturalStoneTypeBlock("magnetite"))
            .item()
            .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/natural/magnetite_2")))
            .build()
            .register();
    public static final BlockEntry<MineralDepositBlock> nativeCopperDeposit = registrate.block("native_copper_deposit", MineralDepositBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .loot((p, b) -> p.add(b, RegistrateBlockLootTables.createSingleItemTable(Items.COBBLESTONE)
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(MetallurgicaItems.nativeCopper.get(), LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                            .add(LootItem.lootTableItem(MetallurgicaItems.nativeCopper.get()))))))
            .simpleItem()
            .register();
    
    public static final BlockEntry<Block> nativeCopperStone = registrate.block("native_copper_rich_stone", Block::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(MetallurgicaItems.nativeCopper.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .blockstate(BlockStateGen.naturalStoneTypeBlock("native_copper"))
            .item()
            .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/natural/native_copper_2")))
            .build()
            .register();
    public static final BlockEntry<ChannelDepotBlock> channelDepot = registrate.block("channel_depot", ChannelDepotBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.LODESTONE))
            .transform(pickaxeOnly())
            .blockstate(new ChannelDepotGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel("_", "block"))
            .register();
    
    public static final BlockEntry<ChannelBlock> channel = registrate.block("channel", ChannelBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).sound(SoundType.LODESTONE))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(new ChannelGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(ModelGen.customItemModel())
            .register();
    
    public static void register() {
    
    }
}
