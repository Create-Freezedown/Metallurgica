package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.Optional;

public enum MetallurgicaMaterials {
    //COPPER
    NATIVE_COPPER(7, 7, -3, 97, 8, -3, 97, Couple.create(() -> Blocks.STONE, () -> Blocks.GRANITE), 0.23f, 75),
    MALACHITE(true),
    //CHALKOPYRITE(),

    //GOLD
    NATIVE_GOLD(3, 4, -12, 56, 5, -12, 56, Couple.create(() -> Blocks.STONE, () -> Blocks.DIORITE), 0.1f, 75, MetallurgicaTags.AllBiomeTags.HAS_GOLD_SURFACE_DEPOSIT.tag),

    //IRON
    MAGNETITE(4, 4, -3, 128, 5, -3, 128, true, Couple.create(() -> Blocks.STONE, () -> Blocks.TUFF), 0.15f, 75),
    //HEMATITE(),
    //PENTLANDITE(),

    //LITHIUM/ALUMINUM
    BAUXITE(5, 9, -30, 70, 10, -30, 70, Couple.create(() -> getBlock(CreateTFMG.asResource("bauxite")), () -> Blocks.GRANITE), 0.2f, 75),
    //PETALITE(),
    //SPODUMENE(),

    //LEAD
    //GALENA(),
    //NATIVE_LEAD(),
    //PYROMORPHITE(),

    //ZINC
    //SPHALRITE(),

    //TUNGSTEN
    //WOLFRAMITE(),

    //TITANIUM
    RUTILE(),

    //URANIUM/THORIUM
    //URANINITE(),
    //MONAZITE(),

    //PLATINUM
    //SPERRYLITE(),

    //Silver
    //ARGENTITE(),
    //CHLORARGYRITE(),

    //LOTION (????)
    //CALAMINE(),

    //LIMESTONE
    //DOLOMITE(),

    //MAGNESIUM
    MAGNESITE(),
    CASSITERITE(3, 5, -12, 87, 6, -12, 87, Couple.create(() -> Blocks.STONE, () -> Blocks.GRANITE), 0.22f, 75),
    
    //FLUORITE
    FLUORITE(),
    ;
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
    public MOreFeatureConfigEntry DEPOSIT;
    public MOreFeatureConfigEntry CLUSTER;
    @Nullable
    public MDepositFeatureConfigEntry SURFACE_DEPOSIT;
    public final MaterialEntry MATERIAL;
    
    private static Block getBlock(ResourceLocation id) {
        Block block = Registry.BLOCK.get(id);
        return block;
    }

    MetallurgicaMaterials() {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
    }

    MetallurgicaMaterials(boolean richb) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
    }
    
    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
    }
    
    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, boolean richb) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
    }

    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        SURFACE_DEPOSIT = MATERIAL.surfaceDeposit(frequency, chance, accompanyingBlocks);
    }

    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, boolean richb, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        SURFACE_DEPOSIT = MATERIAL.surfaceDeposit(frequency, chance, accompanyingBlocks);
    }
    
    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance, TagKey<Biome> biomeTag) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        SURFACE_DEPOSIT = MATERIAL.surfaceDeposit(frequency, chance, accompanyingBlocks, biomeTag);
    }
    
    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, boolean richb, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance, TagKey<Biome> biomeTag) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        SURFACE_DEPOSIT = MATERIAL.surfaceDeposit(frequency, chance, accompanyingBlocks, biomeTag);
    }
    
    MetallurgicaMaterials(Optional<MOreFeatureConfigEntry> deposit, Optional<MOreFeatureConfigEntry> cluster, Optional<MDepositFeatureConfigEntry> surfaceDeposit, boolean richb) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        deposit.ifPresent(mOreFeatureConfigEntry -> DEPOSIT = mOreFeatureConfigEntry);
        cluster.ifPresent(mOreFeatureConfigEntry -> CLUSTER = mOreFeatureConfigEntry);
        surfaceDeposit.ifPresent(mDepositFeatureConfigEntry -> SURFACE_DEPOSIT = mDepositFeatureConfigEntry);
    }
}
