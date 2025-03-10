package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.TFMGConductor;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.conductor.ConductorEntry;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import net.minecraft.resources.ResourceLocation;

import static com.freezedown.metallurgica.Metallurgica.registrate;

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

    public static final ConductorEntry<Conductor> copper = registrate.conductor("copper", Conductor::new)
            .properties((p) -> p)
            .transform(TFMGConductor.setResistivity(0.0178))
            .register();

    public static final ConductorEntry<Conductor> aluminum = registrate.conductor("aluminum", Conductor::new)
            .properties((p) -> p.color1(211, 215, 215, 255).color2(176, 182, 186, 255))
            .transform(TFMGConductor.setResistivity(0.0276))
            .register();

    public static final ConductorEntry<Conductor> scandium = registrate.conductor("scandium", Conductor::new)
            .properties((p) -> p.color1(237, 235, 216, 255).color2(218, 209, 198, 255))
            .transform(TFMGConductor.setResistivity(0.0124))
            .register();

    public static void register() {
        //MetallurgicaRegistries.CONDUCTOR.freeze();
    }

    //public static Conductor get(ResourceLocation key) {
    //    return MetallurgicaRegistries.CONDUCTOR.get(key);
    //}
}
