package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.feature.*;
//import com.freezedown.metallurgica.foundation.worldgen.feature.SandDepositFeature;
import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.TypedDeposit;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaFeatures {
    private static final DeferredRegister<Feature<?>> REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, Metallurgica.ID);
    
    public static final RegistryObject<MOreDepositFeature> ORE_DEPOSIT_SURFACE = REGISTER.register("surface_ore_deposit", MOreDepositFeature::new);
    public static final RegistryObject<MagmaConduitFeature> MAGMA_CONDUIT = REGISTER.register("magma_conduit", MagmaConduitFeature::new);
    public static final RegistryObject<TypedDeposit> LARGE_DEPOSIT = REGISTER.register("large_deposit", TypedDeposit::new);

    public static final RegistryObject<LakeFeature> LAKE = REGISTER.register("lake", LakeFeature::new);
    //IGNORE THIS LOL
//    public static final RegistryObject<SandDepositFeature> SAND_DEPOSIT = REGISTER.register("sand_deposit", SandDepositFeature::new);
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
