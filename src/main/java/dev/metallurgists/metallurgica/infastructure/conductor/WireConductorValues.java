package dev.metallurgists.metallurgica.infastructure.conductor;

import com.simibubi.create.api.registry.SimpleRegistry;

import java.util.function.DoubleSupplier;

public class WireConductorValues {

    public static final SimpleRegistry<Conductor, DoubleSupplier> RESISTIVITIES = SimpleRegistry.create();

    public static final SimpleRegistry<Conductor, DoubleSupplier> MAX_LENGTHS = SimpleRegistry.create();

    public static double getResistivity(Conductor conductor) {
        DoubleSupplier supplier = RESISTIVITIES.get(conductor);
        return supplier == null ? 0 : supplier.getAsDouble();
    }

    public static double getMaxLength(Conductor conductor) {
        DoubleSupplier supplier = MAX_LENGTHS.get(conductor);
        return supplier == null ? 0 : supplier.getAsDouble();
    }
}
