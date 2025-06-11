package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.MetallurgicaCreativeTab;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetMaterials {
    public static final Map<ResourceLocation, Material> registeredMaterials = new HashMap<>();

    //Base Metals
    public static Material IRON;
    public static Material GOLD;
    public static Material COPPER;
    public static Material NETHERIUM;
    public static Material ALUMINUM;
    public static Material SCANDIUM;
    public static Material LEAD;
    public static Material SILVER;
    public static Material NICKEL;
    public static Material TIN;
    public static Material ZINC;
    public static Material PLATINUM;
    public static Material TITANIUM;
    public static Material URANIUM;
    public static Material LITHIUM;
    public static Material MAGNESIUM;
    public static Material TUNGSTEN;
    public static Material OSMIUM;
    public static Material THORIUM;
    public static Material TANTALUM;
    public static Material SODIUM;
    public static Material CHROMIUM;
    public static Material VANADIUM;
    public static Material MANGANESE;

    //Alloys
    public static Material TITANIUM_ALUMINIDE;
    public static Material NETHERITE;
    public static Material BRASS;
    public static Material BRONZE;
    public static Material ARSENICAL_BRONZE;
    public static Material WROUGHT_IRON;

    //Minerals
    public static Material MALACHITE;
    public static Material MAGNETITE;
    public static Material HEMATITE;
    public static Material BAUXITE;
    public static Material SPODUMENE;
    public static Material SPHALERITE;
    public static Material SMITHSONITE;
    public static Material RUTILE;
    public static Material POTASH;
    public static Material CASSITERITE;
    public static Material FLUORITE;
    public static Material CUPRITE;

    //Compounds
    public static Material MAGNESIUM_OXIDE;
    public static Material POTASSIUM_NITRATE;
    public static Material CALCIUM_CARBONATE;

    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);

    public static final Map<Material, List<FluidEntry<?>>> materialFluids = new HashMap<>();


    public static void register(IEventBus modEventBus){

    }
}
