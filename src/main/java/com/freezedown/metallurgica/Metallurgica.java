package com.freezedown.metallurgica;

import com.freezedown.metallurgica.compat.cbc.BigCannonsCompat;
import com.freezedown.metallurgica.content.fluids.types.open_ended_pipe.OpenEndedPipeEffects;
import com.freezedown.metallurgica.experimental.ExperimentalEvents;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.data.MetallurgicaDatagen;
import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaFeatures;
import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaPlacementModifiers;
import com.freezedown.metallurgica.infastructure.conductor.ConductorStats;
import com.freezedown.metallurgica.registry.*;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries;
import com.freezedown.metallurgica.world.biome_modifier.SurfaceDepositsModifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static com.simibubi.create.foundation.item.TooltipHelper.styleFromColor;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Metallurgica.ID)
public class Metallurgica
{
    public static final String ID = "metallurgica";
    public static final String DISPLAY_NAME = "Metallurgica";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    
    public static final MetallurgicaRegistrate registrate = MetallurgicaRegistrate.create(ID);
    
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Metallurgica.ID);
    //public static final RegistryObject<Codec<? extends OreModifier>> oreGen_CODEC = BIOME_MODIFIERS.register("generation_ores", () -> Codec.unit(OreModifier.INSTANCE));
    public static final RegistryObject<Codec<? extends SurfaceDepositsModifier>> surfaceDeposits_CODEC = BIOME_MODIFIERS.register("generation_surface_deposits", () -> Codec.unit(SurfaceDepositsModifier.INSTANCE));
    public static final FontHelper.Palette METALLURGICA_PALETTE = new FontHelper.Palette(styleFromColor(0x383d59), styleFromColor(0x717388));
    static {
        registrate.setTooltipModifierFactory((item) -> (new ItemDescription.Modifier(item, METALLURGICA_PALETTE)).andThen(TooltipModifier.mapNull(KineticStats.create(item))).andThen(TooltipModifier.mapNull(ConductorStats.create(item))));
    }
    
    public Metallurgica()
    {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        registrate.registerEventListeners(modEventBus);
        MetallurgicaRegistries.register();
        
        //BIOME_MODIFIERS.register(modEventBus);
        MetallurgicaMaterials.register();
        MetallurgicaLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        MetallurgicaCreativeTab.register(modEventBus);
        MetallurgicaBlockEntities.register();
        //modEventBus.addGenericListener(Conductor.class, MetallurgicaConductors::register);
        MetallurgicaConductors.register();
        MetallurgicaBlocks.register();
        MetallurgicaItems.register();

        MetallurgicaFluids.register();
        MetallurgicaEffects.register(modEventBus);
        MetallurgicaRecipeTypes.register(modEventBus);
        MetallurgicaPackets.registerPackets();
        MetallurgicaOreFeatureConfigEntries.init();
        MetallurgicaFeatures.register(modEventBus);
        MetallurgicaPlacementModifiers.register(modEventBus);
        MetallurgicaEntityTypes.register();

        MetallurgicaConfigs.register(modLoadingContext);
        
        EventHandler commonHandler = new EventHandler();
        MinecraftForge.EVENT_BUS.register(commonHandler);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(Metallurgica::init);
        modEventBus.addListener(EventPriority.LOWEST, MetallurgicaDatagen::gatherData);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MetallurgicaClient::new);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MetallurgicaClient.onCtorClient(modEventBus, forgeEventBus));
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public static void init(final FMLCommonSetupEvent event) {
        LOGGER.info("Setting up Metallurgica's Open Ended Pipe Effects!");
        OpenEndedPipeEffects.init();
        BigCannonsCompat.register();
        MetallurgicaBlockSpoutingBehaviours.registerDefaults();
        MinecraftForge.EVENT_BUS.register(new ExperimentalEvents());
    };
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }
    
    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent event)
    {
        LOGGER.info("Thanks for using Metallurgica! Expect a severe lack of ores in your world :3");
        //if (AllOreFeatureConfigEntries.ZINC_ORE != null)
        //    AllOreFeatureConfigEntries.ZINC_ORE.frequency.set(0.0);
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Double checking our cool little ore frequency thingy :3");
        //if (AllOreFeatureConfigEntries.ZINC_ORE != null)
        //    AllOreFeatureConfigEntries.ZINC_ORE.frequency.set(0.0);
    }

    public static boolean isClientThread() {
        return isClientSide() && Minecraft.getInstance().isSameThread();
    }

    public static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
    public static @NotNull MetallurgicaRegistrate registrate() {
        return registrate;
    }
}
