package dev.metallurgists.metallurgica.registry;

import static dev.metallurgists.metallurgica.Metallurgica.registrate;

public class MetallurgicaConductors {
    //static {
    //    MetallurgicaRegistries.CONDUCTOR.unfreeze();
    //}
//
    //public static final Conductor COPPER = createAndRegister("copper", 0.0178F);
    //public static final Conductor ALUMINUM = createAndRegister("aluminum", 0.0276F, new int[]{211, 215, 215, 255}, new int[]{176, 182, 186, 255});
    //public static final Conductor SCANDIUM = createAndRegister("scandium", 0.0124F, new int[]{237, 235, 216, 255}, new int[]{218, 209, 198, 255});
//
    //public static Conductor createAndRegister(String name, float resistivity) {
    //    Conductor conductor = new Conductor(resistivity);
    //    MetallurgicaRegistries.CONDUCTOR.register(Metallurgica.asResource(name), conductor);
    //    return conductor;
    //}
    //public static Conductor createAndRegister(String name, float resistivity, int[] color1, int[] color2) {
    //    Conductor conductor = new Conductor(resistivity, color1, color2);
    //    MetallurgicaRegistries.CONDUCTOR.register(Metallurgica.asResource(name), conductor);
    //    return conductor;
    //}

    //public static final ConductorEntry<Conductor> copper = registrate.conductor("copper", Conductor::new)
    //        .properties((p) -> p)
    //        .transform(TFMGConductor.setResistivity(0.0178))
    //        .register();
//
    //public static final ConductorEntry<Conductor> aluminum = registrate.conductor("aluminum", Conductor::new)
    //        .properties((p) -> p.color1(211, 215, 215, 255).color2(176, 182, 186, 255))
    //        .transform(TFMGConductor.setResistivity(0.0276))
    //        .register();
//
    //public static final ConductorEntry<Conductor> scandium = registrate.conductor("scandium", Conductor::new)
    //        .properties((p) -> p.color1(237, 235, 216, 255).color2(218, 209, 198, 255))
    //        .transform(TFMGConductor.setResistivity(0.0124))
    //        .register();

    //public static void register(TFMGConductorRegisterEvent event) {
    //    event.register(copper);
    //    event.register(aluminum);
    //    event.register(scandium);
    //}

    public static void register() {
        registrate.setCreativeTab(MCreativeTabs.MATERIALS);
        //for (Material material : MetMaterials.registeredMaterials.values()) {
        //    if (!material.hasFlag(FlagKey.SPOOL)) continue;
        //    SpoolFlag cableFlag = material.getFlag(FlagKey.SPOOL);
        //    ConductorEntry<Conductor> conductor = registrate.conductor(material.getName(), Conductor::new)
        //            .properties(p -> p.color1(cableFlag.getColors().getFirst()).color2(cableFlag.getColors().getSecond()))
        //            .transform(TFMGConductor.setResistivity(cableFlag.getResistivity()))
        //            .register();
        //    registrate.item("%s_cable".formatted(material.getName()), (p) -> new CableItem(p, conductor))
        //            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
        //            .tag(MetallurgicaTags.modItemTag("cables"))
        //            .register();
        //}
    }

    //public static Conductor get(ResourceLocation key) {
    //    return MetallurgicaRegistries.CONDUCTOR.get(key);
    //}
}
