package com.freezedown.metallurgica.registry;

import com.drmangotea.tfmg.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.OreEntry;
import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.DepositCapacity;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials.*;


public enum MetallurgicaOre {
    //COPPER
    NATIVE_COPPER(26, 7, -3, 97, 64, 25, 63, 34, 0.03f, DepositCapacity.MEDIUM, 180, 230, Couple.create(() -> Blocks.STONE, () -> Blocks.GRANITE), 0.23f, 75, COPPER),
    MALACHITE(true, COPPER),
    //CHALKOPYRITE(Copper),

    //GOLD
    NATIVE_GOLD(12, 4, -12, 56, 67, 45, 90, 68, 0.32f, DepositCapacity.TINY, 23, 75, Couple.create(() -> Blocks.STONE, () -> Blocks.RED_SANDSTONE), 0.1f, 75, GOLD),

    //IRON
    MAGNETITE( false,23, 4, -3, 128, 78, 45, 60, 20, 0.23f, DepositCapacity.MEDIUM, 61, 230, Couple.create(() -> Blocks.STONE, () -> Blocks.TUFF), 0.15f, 75, IRON),
    HEMATITE( false, 23, 4, -3, 128, 78, 45, 60, 20, 0.23f, DepositCapacity.MEDIUM, 61, 230, Couple.create(() -> Blocks.STONE, () -> Blocks.TUFF), 0.15f, 75, IRON),
//    PENTLANDITE(IRON),

    //LITHIUM/ALUMINUM
    BAUXITE(19, 9, -30, 70, 56, 30, 30, 20, 0.23f, DepositCapacity.SMALL, 180, 230, Couple.create(() -> getBlock(CreateTFMG.asResource("bauxite")), () -> Blocks.GRANITE), 0.2f, 75, ALUMINUM),
//    PETALITE(ALUMINUM, LITHIUM),
    SPODUMENE( false,26, 4, 40, 96, 78, 45, 60, 20, 0.23f, DepositCapacity.SMALL, 61, 230, Couple.create(() -> Blocks.STONE, () -> Blocks.TUFF), 0.1f, 75, LITHIUM),

    //LEAD
//    GALENA(LEAD),
//    NATIVE_LEAD(LEAD),
//    PYROMORPHITE(LEAD),

    //ZINC
    SPHALERITE(ZINC),
    SMITHSONITE(ZINC),

    //TUNGSTEN
//    WOLFRAMITE(TUNGSTEN),

    //TITANIUM
    RUTILE(TITANIUM),

    //URANIUM/THORIUM
//    URANINITE(URANIUM),
//    MONAZITE(URANIUM, THORIUM),

    //PLATINUM
    //SPERRYLITE(),
    //SPERRYLITE(PLATINUM),

    //LOTION ()

    //SILVER
//    ARGENTITE(SILVER),
//    CHLORARGYRITE(SILVER),

    //LIMESTONE
    //POTASH()
    //DOLOMITE(),

    //MAGNESIUM
//    MAGNESITE(MAGNESIUM),
    //TIN
    CASSITERITE(TIN),

    //FLUORITE
    FLUORITE(),
    ;

    public final OreEntry ORE;
    public final List<Material> METALS = new ArrayList<>();
    
    private static Block getBlock(ResourceLocation id) {
        return ForgeRegistries.BLOCKS.getValue(id);
    }

    MetallurgicaOre() {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);;
        ORE = registrate.material(this.name().toLowerCase(), false);
    }

    MetallurgicaOre(MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), false);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }

    MetallurgicaOre(boolean richb, MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), richb);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }

    MetallurgicaOre(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int maxWidth, int minWidth, int maxDepth, int minDepth, float depositChance, DepositCapacity capacity, int depositMinHeight, int depositMaxHeight, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance, MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), false);
        //DEPOSIT = ORE.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }
    MetallurgicaOre(boolean richb, int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int maxWidth, int minWidth, int maxDepth, int minDepth, float depositChance, DepositCapacity capacity, int depositMinHeight, int depositMaxHeight, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance, MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), richb);
        //DEPOSIT = ORE.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }

    MetallurgicaOre(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), false);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }
    
    MetallurgicaOre(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, boolean richb, Couple<NonNullSupplier<? extends Block>> accompanyingBlocks, float frequency, int chance, MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), richb);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }

    MetallurgicaOre(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, boolean richb, MetallurgicaMaterials... materials) {
        MetallurgicaRegistrate registrate = Metallurgica.registrate();
        ORE = registrate.material(this.name().toLowerCase(), richb);
        for(MetallurgicaMaterials material : materials) {
            METALS.add(material.getMaterial());
        }
    }
}
